package com.epam.incubation.service.reservationbooking.resource;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;
import com.epam.incubation.service.reservationbooking.datamodel.UserReservationDataResponse;
import com.epam.incubation.service.reservationbooking.service.ReservationServiceImpl;

@RestController
@RequestMapping("/reservationservice")
public class ReservationBookingResourceImpl implements ReservationBookingResource {

	private final Logger logger = LoggerFactory.getLogger(ReservationBookingResourceImpl.class);
	
	@Autowired
	private ReservationServiceImpl reservationService;

	@PostMapping("/book")
	@PreAuthorize("hasRole('GUEST')")
	public ReservationDataModel bookReservation(@Valid @RequestBody ReservationRequestModel reservation) {
		logger.info("booking reservation api calling service for book the reservation");
		return reservationService.bookReservation(reservation);
	}

	@DeleteMapping("/cancelreservation/{id}")
	@PreAuthorize("hasRole('GUEST')")
	public ReservationDataModel cancelReservation(@PathVariable(name = "id") Integer reservatonId) {
		logger.info("cancelling reservation api calling service for cancel the reservation");
		return reservationService.cancelReservation(reservatonId);
	}

	@GetMapping(value = "/getreservation/{id}")
	@PreAuthorize("hasRole('GUEST')")
	public ReservationDataModel getReservation(@PathVariable(name = "id") Integer id) {
		logger.info("Fetching the reservation by id");
		return reservationService.getReservation(id);
	}

	@GetMapping(value = "/getreservationsByHotelId/{id}")
	@PreAuthorize("hasRole('GUEST')")
	public List<ReservationDataModel> getReservationByHotelId(@PathVariable(name = "id") Integer hotelId) {
		logger.info("Fetching the reservation by hotel id");
		return reservationService.getReservationByHotelId(hotelId);
	}

	@GetMapping(value = "/guestreservationshistory/{id}")
	@PreAuthorize("hasRole('GUEST')")
	public UserReservationDataResponse getGuestReservationHistory(@PathVariable(name = "id") Integer guestId) {
		logger.info("Fetching the guest history by id");
		return reservationService.getGuestReservationHisotry(guestId);
	}
}
