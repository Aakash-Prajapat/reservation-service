package com.epam.incubation.service.reservationbooking.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epam.incubation.service.reservationbooking.datamodel.InventoryDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationLineDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;
import com.epam.incubation.service.reservationbooking.entities.Reservation;
import com.epam.incubation.service.reservationbooking.exception.InventoryNotAvailableException;
import com.epam.incubation.service.reservationbooking.exception.RecordNotFoundException;
import com.epam.incubation.service.reservationbooking.repository.ReservationRepository;
import com.epam.incubation.service.reservationbooking.requestmodel.InventoryRequestModel;
import com.epam.incubation.service.reservationbooking.responsemodel.InventoryDetailsResponseModel;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	HotelConsumerService hotelConsumerService;

	public ReservationDataModel bookReservation(ReservationRequestModel reservation) {
		InventoryRequestModel inventoryRequestModel = new InventoryRequestModel();
		inventoryRequestModel.setCheckInDate(reservation.getCheckInDate());
		inventoryRequestModel.setCheckOutDate(reservation.getCheckOutDate());
		inventoryRequestModel.setHotelId(reservation.getHotelId());
		inventoryRequestModel.setOperation("GET");
		inventoryRequestModel.setRoomId(reservation.getReservationLineDetails().get(0).getRoomId());
		ResponseEntity<InventoryDetailsResponseModel> result = hotelConsumerService.getInventoryDetails(inventoryRequestModel);
		if (null == result.getBody()) {
			throw new InventoryNotAvailableException("Exception while calling inventory services");
		}
		if (null != result.getBody() && null == result.getBody().getError()) {
			InventoryDetailsResponseModel inventoryDetails = result.getBody();
			if (null != inventoryDetails.getResponseModel()) {
				List<InventoryDetailsDataModel> inventoryDetailsDataModels = inventoryDetails.getResponseModel()
						.stream().map(InventoryDetailsDataModel::new).collect(Collectors.toList());
				reservation.getReservationLineDetails().get(0).setInventoriesDetails(inventoryDetailsDataModels);
			}
		} else {
			if (result.getBody().getError().getStatus().value() == 404) {
				throw new InventoryNotAvailableException("Inventory is not available");
			}
		}
		ReservationDataModel requestReservationDataModel = convertRequestModeltoDataModel(reservation);
		Reservation savedReservation = reservationRepository
				.save(convertDataModeltoEntity(requestReservationDataModel));
		// call to update the inventory quantity
		savedReservation = updateInventory(savedReservation, inventoryRequestModel);
		return convertEntitytoDataModel(savedReservation);
	}

	public ReservationDataModel cancelReservation(Integer id) {
		Optional<Reservation> reservation = reservationRepository.findById(id);
		if (reservation.isPresent()) {
			if ("Cancel".equals(reservation.get().getState())) {
				throw new RecordNotFoundException(
						reservation.get().getReservationId() + "Number Reservation is already cancelled");
			}
			reservation.get().setState("Cancel");
			Reservation savedReservation = reservationRepository.save(reservation.get());
			// update the inventory
			if (null != savedReservation.getReservationId() && savedReservation.getReservationId() > 0) {
				InventoryRequestModel inventoryRequestModel = new InventoryRequestModel();
				inventoryRequestModel.setCheckInDate(savedReservation.getCheckInDate());
				inventoryRequestModel.setCheckOutDate(savedReservation.getCheckOutDate());
				inventoryRequestModel.setHotelId(savedReservation.getHotelId());
				inventoryRequestModel.setRoomId(savedReservation.getReservationLineDetails().get(0).getRoomId());
				inventoryRequestModel.setOperation("Cancel");
				ResponseEntity<InventoryDetailsResponseModel> updateInventoryResponse = hotelConsumerService
						.updateInventoryDetails(inventoryRequestModel);

				// Exception occurs, Need to update the reservation status
				if (updateInventoryResponse.getBody().getError().getStatus().value() == 404) {
					savedReservation.setState("BOOKED");
					savedReservation = reservationRepository.save(savedReservation);
				}
			}
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
		reservationDataModel.setState("DRAFT");
		reservationDataModel.setReservationLineDetails(request.getReservationLineDetails().stream()
				.map(ReservationLineDetailsDataModel::new).collect(Collectors.toList()));
		reservationDataModel.setTotalAmount(reservationDataModel.getReservationLineDetails().stream()
				.mapToDouble(ReservationLineDetailsDataModel::getAmountPerRoom).sum());
		return reservationDataModel;
	}

	private Reservation updateInventory(Reservation savedReservation, InventoryRequestModel inventoryRequestModel) {
		if (null != savedReservation.getReservationId() && savedReservation.getReservationId() > 0) {
			inventoryRequestModel.setOperation("Booking");
			inventoryRequestModel.setCheckInDate(savedReservation.getCheckInDate());
			ResponseEntity<InventoryDetailsResponseModel> updateInventoryResponse = hotelConsumerService
					.updateInventoryDetails(inventoryRequestModel);
			if (null == updateInventoryResponse.getBody()) {
				throw new InventoryNotAvailableException("Inventory service is not working");
			}
			if (null == updateInventoryResponse.getBody().getError()) {
				savedReservation.setState("BOOKED");
				savedReservation = reservationRepository.save(savedReservation);
			} else {
				if (updateInventoryResponse.getBody().getError().getStatus().value() == 404) {
					throw new InventoryNotAvailableException("Inventory is not available");
				}
			}
		}
		return savedReservation;
	}
}
