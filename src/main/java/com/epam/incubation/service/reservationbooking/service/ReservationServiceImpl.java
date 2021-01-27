package com.epam.incubation.service.reservationbooking.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.epam.incubation.service.reservationbooking.constant.ReservationServiceConstant;
import com.epam.incubation.service.reservationbooking.datamodel.ApiError;
import com.epam.incubation.service.reservationbooking.datamodel.InventoryDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationLineDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;
import com.epam.incubation.service.reservationbooking.datamodel.TransactionDetails;
import com.epam.incubation.service.reservationbooking.datamodel.UserReservationData;
import com.epam.incubation.service.reservationbooking.datamodel.UserReservationDataResponse;
import com.epam.incubation.service.reservationbooking.entities.Reservation;
import com.epam.incubation.service.reservationbooking.exception.InventoryNotAvailableException;
import com.epam.incubation.service.reservationbooking.exception.RecordNotFoundException;
import com.epam.incubation.service.reservationbooking.facade.HotelInfoServiceProxy;
import com.epam.incubation.service.reservationbooking.facade.PaymentServiceProxy;
import com.epam.incubation.service.reservationbooking.repository.ReservationRepository;
import com.epam.incubation.service.reservationbooking.requestmodel.InventoryRequestModel;
import com.epam.incubation.service.reservationbooking.responsemodel.InventoryDetailsResponseModel;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import brave.sampler.Sampler;

/**
 * Service layer to make, update and retrieve reservation information.
 */
@Service
public class ReservationServiceImpl implements ReservationService {

	private final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	HotelInfoServiceProxy hotelInfoServiceProxy;

	@Autowired
	PaymentServiceProxy paymentServiceProxy;

	@Autowired
	ReservationServiceImpl service;

	/**
	 * Responsible to book the reservation.
	 * 
	 * @param reservation , reservation information needs to book reservation.
	 * @return ReservationDataModel, Holds reservation information.
	 * @throws InventoryNotAvailableException
	 */
	public ReservationDataModel bookReservation(ReservationRequestModel reservation) {
		logger.info("Constructing the inventory request model");
		InventoryRequestModel inventoryRequestModel = new InventoryRequestModel();
		inventoryRequestModel.setCheckInDate(reservation.getCheckInDate());
		inventoryRequestModel.setCheckOutDate(reservation.getCheckOutDate());
		inventoryRequestModel.setHotelId(reservation.getHotelId());
		inventoryRequestModel.setOperation(ReservationServiceConstant.GET);
		inventoryRequestModel.setRoomId(reservation.getReservationLineDetails().get(0).getRoomId());
		logger.info("Calling hotel service for inventory availability");
		InventoryDetailsResponseModel result = hotelInfoServiceProxy.getInventoryDetails(inventoryRequestModel);
		if (null == result) {
			throw new InventoryNotAvailableException("Exception while calling inventory services");
		}
		logger.info("Receiving the hotel service for inventory availability");
		if (null == result.getError() && null != result.getResponseModel()) {
			List<InventoryDetailsDataModel> inventoryDetailsDataModels = result.getResponseModel().stream()
					.map(InventoryDetailsDataModel::new).collect(Collectors.toList());
			reservation.getReservationLineDetails().get(0).setInventoriesDetails(inventoryDetailsDataModels);
		} else {
			if (result.getError().getStatus().value() == 404) {
				throw new InventoryNotAvailableException("Inventory is not available");
			}
		}
		ReservationDataModel requestReservationDataModel = convertRequestModeltoDataModel(reservation);
		logger.info("Receiving the hotel service for inventory availability");

		logger.info("Calling the Payment service payments");
		String paymentTransaction = doPayment(requestReservationDataModel.getPaymentsDetails().getCreditCardNumber(),
				requestReservationDataModel.getTotalAmount());
		logger.info("Payment done with status {}", paymentTransaction);
		logger.info("Confirming the reservation");
		Reservation savedReservation = reservationRepository
				.save(convertDataModeltoEntity(requestReservationDataModel));
		logger.info("Reservation done");
		// call to update the inventory quantity
		inventoryRequestModel.setOperation(ReservationServiceConstant.BOOKING);
		logger.info("Calling the update inventory service");
		savedReservation = updateInventoryAfterReservationSaved(savedReservation, inventoryRequestModel,
				ReservationServiceConstant.BOOKED);
		return convertEntitytoDataModel(savedReservation);
	}

	/**
	 * Responsible to cancel the reservation.
	 * 
	 * @param reservation id , fetch reservation by id and cancel the same.
	 * @return ReservationDataModel, Holds reservation information.
	 * @throws RecordNotFoundException
	 */
	public ReservationDataModel cancelReservation(Integer id) {
		logger.info("Fetching reservation for cancel");
		Optional<Reservation> reservation = reservationRepository.findById(id);
		if (reservation.isPresent()) {
			if (ReservationServiceConstant.CANCELLED.equals(reservation.get().getState())) {
				throw new RecordNotFoundException(
						reservation.get().getReservationId() + "Number Reservation is already cancelled");
			}
			reservation.get().setState(ReservationServiceConstant.CANCELLED);
			logger.info("canecelling the reservation");
			Reservation savedReservation = reservationRepository.save(reservation.get());
			logger.info("calling payment service for credit transaction");
			/*
			 * String paymentTransaction =
			 * creditTrasaction(savedReservation.getPaymentsDetails().getCreditCardNumber(),
			 * savedReservation.getTotalAmount());
			 * logger.info("Response from payment service {}",paymentTransaction);
			 */
			// update the inventory
			InventoryRequestModel inventoryRequestModel = new InventoryRequestModel();
			inventoryRequestModel.setCheckInDate(savedReservation.getCheckInDate());
			inventoryRequestModel.setCheckOutDate(savedReservation.getCheckOutDate());
			inventoryRequestModel.setHotelId(savedReservation.getHotelId());
			inventoryRequestModel.setRoomId(savedReservation.getReservationLineDetails().get(0).getRoomId());
			inventoryRequestModel.setOperation(ReservationServiceConstant.CANCEL);
			logger.info("calling hotel service for inventory update");
			savedReservation = updateInventoryAfterReservationSaved(savedReservation, inventoryRequestModel,
					ReservationServiceConstant.CANCELLED);
			return convertEntitytoDataModel(savedReservation);
		} else
			throw new RecordNotFoundException("Reservation is not present with " + id);
	}

	/**
	 * Responsible to return the Reservation by Id.
	 * 
	 * @param reservationId , Id to which Reservation information get fetched.
	 * @return ReservationDataModel, Holds Reservation information.
	 * @throws RecordNotFoundException
	 */
	public ReservationDataModel getReservation(Integer id) {
		logger.info("Fetching reservation by id");
		Optional<Reservation> reservation = reservationRepository.findById(id);
		if (reservation.isEmpty())
			throw new RecordNotFoundException("Reservation not found with" + id);
		return new ReservationDataModel(reservation.get());
	}

	/**
	 * Responsible to return the reservation by hotel Id.
	 * 
	 * @param hotelId , hotel Id to which reservation information get fetched.
	 * @return List of ReservationDataModel, Holds reservation information.
	 * @throws RecordNotFoundException
	 */
	public List<ReservationDataModel> getReservationByHotelId(Integer hotelId) {
		logger.info("Fetching reservation by hotelid");
		List<Reservation> reservations = reservationRepository.getReservationByHotelId(hotelId);
		if (reservations.isEmpty())
			throw new RecordNotFoundException("No reservations found for hotel id " + hotelId);
		return convertEntitytoDataModel(reservations);
	}

	private Reservation convertDataModeltoEntity(ReservationDataModel reservation) {
		return new Reservation(reservation);
	}

	private ReservationDataModel convertEntitytoDataModel(Reservation reservation) {
		return new ReservationDataModel(reservation);
	}

	private List<ReservationDataModel> convertEntitytoDataModel(List<Reservation> reservations) {
		return reservations.stream().map(ReservationDataModel::new).collect(Collectors.toList());
	}

	private ReservationDataModel convertRequestModeltoDataModel(ReservationRequestModel request) {
		ReservationDataModel reservationDataModel = new ReservationDataModel();
		reservationDataModel.setGuestId(request.getGuestId());
		reservationDataModel.setHotelId(request.getHotelId());
		reservationDataModel.setCheckInDate(request.getCheckInDate());
		reservationDataModel.setCheckOutDate(request.getCheckOutDate());
		reservationDataModel.setCreateDate(new Date());
		reservationDataModel.setLastUpdateDate(new Date());
		reservationDataModel.setPaymentsDetails(request.getPaymentsDetails());
		reservationDataModel.setState(ReservationServiceConstant.DRAFT);
		reservationDataModel.setReservationLineDetails(request.getReservationLineDetails().stream()
				.map(ReservationLineDetailsDataModel::new).collect(Collectors.toList()));
		reservationDataModel.setTotalAmount(reservationDataModel.getReservationLineDetails().stream()
				.mapToDouble(ReservationLineDetailsDataModel::getAmountPerRoom).sum());
		return reservationDataModel;
	}

	private Reservation updateInventoryAfterReservationSaved(Reservation savedReservation,
			InventoryRequestModel inventoryRequestModel, String status) {
		if (null != savedReservation.getReservationId() && savedReservation.getReservationId() > 0) {
			InventoryDetailsResponseModel updateInventoryResponse = hotelInfoServiceProxy
					.updateInventory(inventoryRequestModel);
			if (null == updateInventoryResponse) {
				throw new InventoryNotAvailableException("Inventory service is not working");
			}
			if (null == updateInventoryResponse.getError()) {
				savedReservation.setState(status);
				savedReservation = reservationRepository.save(savedReservation);
			} else if (updateInventoryResponse.getError().getStatus().value() == 404) {
				throw new InventoryNotAvailableException("Inventory is not available");
			}
		}
		return savedReservation;
	}

	/**
	 * Responsible to return the reservation history by guest Id.
	 * 
	 * @param guestId , Id to which reservation information get fetched.
	 * @return UserReservationDataResponse, Holds reservation information.
	 */
	public UserReservationDataResponse getGuestReservationHisotry(Integer guestId) {
		UserReservationDataResponse response = new UserReservationDataResponse();
		logger.info("Fetching reservation by guest id");
		List<Reservation> reservations = reservationRepository.getReservationsByGuestId(guestId);
		if (reservations.isEmpty()) {
			response.setError(new ApiError(HttpStatus.NOT_FOUND, "Reservation is not present with "+guestId));
		} else {
			response.setReservations(reservations.stream().map(UserReservationData::new).collect(Collectors.toList()));
			
		}
		return response;
	}

	@HystrixCommand(fallbackMethod = "doPaymentFallback")
	public String doPayment(Long cardNumber, double amount) {
		return paymentServiceProxy.doPayment(new TransactionDetails(cardNumber, amount));
	}

	public String doPaymentFallback(Long cardNumber, double amount) {
		logger.info("In fall back of do payment with {} {}", cardNumber, amount);
		return "success";
	}

	@HystrixCommand(fallbackMethod = "creditTrasactioFallback")
	public String creditTrasaction(Long cardNumber, double amount) {
		return paymentServiceProxy.creditTransaction(new TransactionDetails(cardNumber, amount));
	}

	public String creditTrasactioFallback(Long cardNumber, double amount) {
		logger.info("In fall back of credit transaction with {} {}", cardNumber, amount);
		return "success";
	}

	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

}
