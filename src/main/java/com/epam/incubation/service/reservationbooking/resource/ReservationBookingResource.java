package com.epam.incubation.service.reservationbooking.resource;

import java.util.List;

import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;
import com.epam.incubation.service.reservationbooking.datamodel.UserReservationDataResponse;
import com.epam.incubation.service.reservationbooking.exception.RecordNotFoundException;
import com.epam.incubation.service.reservationbooking.responsemodel.ReservationApiResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller APIs to make, update and retrieve reservation information.
 */
@Api(value = "Booking Reservation Service")
public interface ReservationBookingResource {

	/**
	 * Responsible to book the reservation.
	 * 
	 * @param ReservationRequestModel , reservation information needs to book
	 *                                reservation.
	 * @return ReservationDataModel, Holds reservation information.
	 * @throws InventoryNotAvailableException
	 */
	@ApiOperation(value = "Book Reservation")
	public ReservationDataModel bookReservation(
			@ApiParam(value = "Reservation store in database", required = true) ReservationRequestModel reservation);

	/**
	 * Responsible to cancel the reservation.
	 * 
	 * @param reservation id , fetch reservation by id and cancel the same.
	 * @return ReservationDataModel, Holds reservation information.
	 * @throws RecordNotFoundException
	 */
	@ApiOperation(value = "Cancle Reservation by Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ReservationDataModel cancelReservation(Integer id);

	/**
	 * Responsible to return the Reservation by Id.
	 * 
	 * @param reservationId , Id to which Reservation information get fetched.
	 * @return ReservationDataModel, Holds Reservation information.
	 * @throws RecordNotFoundException
	 */
	@ApiOperation(value = "Get Reservation Details by Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ReservationDataModel getReservation(Integer id);

	/**
	 * Responsible to return the reservation by hotel Id.
	 * 
	 * @param hotelId , hotel Id to which reservation information get fetched.
	 * @return List of ReservationDataModel, Holds reservation information.
	 * @throws RecordNotFoundException
	 */
	@ApiOperation(value = "Get All Reservation by Hotel Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public List<ReservationDataModel> getReservationByHotelId(Integer hotelId);

	/**
	 * Responsible to return the reservation history by guest Id.
	 * 
	 * @param guestId , Id to which reservation information get fetched.
	 * @return UserReservationDataResponse, Holds reservation information.
	 */
	@ApiOperation(value = "Get All Reservation by Guest Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ReservationApiResponse<UserReservationDataResponse> getGuestReservationHistory(Integer guestId);
}
