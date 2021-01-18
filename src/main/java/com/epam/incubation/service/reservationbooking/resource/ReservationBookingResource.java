package com.epam.incubation.service.reservationbooking.resource;

import java.util.List;

import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;

public interface ReservationBookingResource {

	public ReservationDataModel bookReservation(ReservationRequestModel reservation);

	public ReservationDataModel cancelReservation(Integer id);

	public ReservationDataModel getReservation(Integer id);

	public List<ReservationDataModel> getReservationByHotelId(Integer hotelId);
}
