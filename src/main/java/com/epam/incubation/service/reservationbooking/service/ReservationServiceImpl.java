package com.epam.incubation.service.reservationbooking.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epam.incubation.service.reservationbooking.constant.ReservationServiceConstant;
import com.epam.incubation.service.reservationbooking.datamodel.InventoryDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationLineDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;
import com.epam.incubation.service.reservationbooking.entities.Reservation;
import com.epam.incubation.service.reservationbooking.exception.InventoryNotAvailableException;
import com.epam.incubation.service.reservationbooking.exception.RecordNotFoundException;
import com.epam.incubation.service.reservationbooking.facade.HotelInfoServiceProxy;
import com.epam.incubation.service.reservationbooking.repository.ReservationRepository;
import com.epam.incubation.service.reservationbooking.requestmodel.InventoryRequestModel;
import com.epam.incubation.service.reservationbooking.responsemodel.InventoryDetailsResponseModel;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	HotelInfoServiceProxy hotelInfoServiceProxy;

	public ReservationDataModel bookReservation(ReservationRequestModel reservation) {
		InventoryRequestModel inventoryRequestModel = new InventoryRequestModel();
		inventoryRequestModel.setCheckInDate(reservation.getCheckInDate());
		inventoryRequestModel.setCheckOutDate(reservation.getCheckOutDate());
		inventoryRequestModel.setHotelId(reservation.getHotelId());
		inventoryRequestModel.setOperation(ReservationServiceConstant.GET);
		inventoryRequestModel.setRoomId(reservation.getReservationLineDetails().get(0).getRoomId());
		InventoryDetailsResponseModel result = hotelInfoServiceProxy.getInventoryDetails(inventoryRequestModel);
		if (null == result) {
			throw new InventoryNotAvailableException("Exception while calling inventory services");
		}
		if (null == result.getError() && null != result.getResponseModel()) {
				List<InventoryDetailsDataModel> inventoryDetailsDataModels = result.getResponseModel()
						.stream().map(InventoryDetailsDataModel::new).collect(Collectors.toList());
				reservation.getReservationLineDetails().get(0).setInventoriesDetails(inventoryDetailsDataModels);
		} else {
			if (result.getError().getStatus().value() == 404) {
				throw new InventoryNotAvailableException("Inventory is not available");
			}
		}
		ReservationDataModel requestReservationDataModel = convertRequestModeltoDataModel(reservation);
		Reservation savedReservation = reservationRepository
				.save(convertDataModeltoEntity(requestReservationDataModel));
		// call to update the inventory quantity
		inventoryRequestModel.setOperation(ReservationServiceConstant.BOOKING);
		savedReservation = updateInventoryAfterReservationSaved(savedReservation, inventoryRequestModel, ReservationServiceConstant.BOOKED);
		return convertEntitytoDataModel(savedReservation);
	}

	public ReservationDataModel cancelReservation(Integer id) {
		Optional<Reservation> reservation = reservationRepository.findById(id);
		if (reservation.isPresent()) {
			if (ReservationServiceConstant.CANCELLED.equals(reservation.get().getState())) {
				throw new RecordNotFoundException(
						reservation.get().getReservationId() + "Number Reservation is already cancelled");
			}
			reservation.get().setState(ReservationServiceConstant.CANCELLED);
			Reservation savedReservation = reservationRepository.save(reservation.get());
			// update the inventory
			InventoryRequestModel inventoryRequestModel = new InventoryRequestModel();
			inventoryRequestModel.setCheckInDate(savedReservation.getCheckInDate());
			inventoryRequestModel.setCheckOutDate(savedReservation.getCheckOutDate());
			inventoryRequestModel.setHotelId(savedReservation.getHotelId());
			inventoryRequestModel.setRoomId(savedReservation.getReservationLineDetails().get(0).getRoomId());
			inventoryRequestModel.setOperation(ReservationServiceConstant.CANCEL);
			savedReservation = updateInventoryAfterReservationSaved(savedReservation, inventoryRequestModel, ReservationServiceConstant.CANCELLED);
			return convertEntitytoDataModel(savedReservation);
		} else
			throw new RecordNotFoundException("Reservation is not present with " + id);
	}

	public ReservationDataModel getReservation(Integer id) {
		Optional<Reservation> reservation = reservationRepository.findById(id);
		if (reservation.isEmpty())
			throw new RecordNotFoundException("Reservation not found with" + id);
		return new ReservationDataModel(reservation.get());
	}

	public List<ReservationDataModel> getReservationByHotelId(Integer hotelId) {
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

	private Reservation updateInventoryAfterReservationSaved(Reservation savedReservation, InventoryRequestModel inventoryRequestModel, String status) {
		if (null != savedReservation.getReservationId() && savedReservation.getReservationId() > 0) {
			InventoryDetailsResponseModel updateInventoryResponse = hotelInfoServiceProxy.updateInventory(inventoryRequestModel);
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
}
