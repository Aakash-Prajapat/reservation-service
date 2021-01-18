package com.epam.incubation.service.reservationbooking.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;
import com.epam.incubation.service.reservationbooking.service.ReservationServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1")
@Api(value = "Booking Reservation Service")
public class ReservationBookingResourceImpl implements ReservationBookingResource {

	@Autowired
	private ReservationServiceImpl reservationService;

	@PostMapping("/reservationservice")
	@ApiOperation(value = "Book Reservation")
	public ReservationDataModel bookReservation(
			@ApiParam(value = "Reservation store in database", required = true) @Valid @RequestBody ReservationRequestModel reservation) {
		return reservationService.bookReservation(reservation);
}

	@DeleteMapping("/reservationservice")
	@ApiOperation(value="Cancle Reservation by Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ReservationDataModel cancelReservation(@PathVariable(name = "id") Integer reservatonId) {
		return reservationService.cancelReservation(reservatonId);
	}

	@GetMapping(value = "/reservationservice/{id}")
	@ApiOperation(value = "Get Reservation Details by Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ReservationDataModel getReservation(@PathVariable(name = "id") Integer id) {
		return reservationService.getReservation(id);
	}

	@GetMapping(value = "/reservationservice/reservationByHotelId/{id}")
	@ApiOperation(value = "Get All Reservation by Hotel Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public List<ReservationDataModel> getReservationByHotelId(@PathVariable(name = "id") Integer hotelId) {
		return reservationService.getReservationByHotelId(hotelId);
	}

}
