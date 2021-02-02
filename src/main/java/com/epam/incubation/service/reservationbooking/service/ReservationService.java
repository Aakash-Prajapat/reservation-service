package com.epam.incubation.service.reservationbooking.service;

import java.util.List;

import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;
import com.epam.incubation.service.reservationbooking.datamodel.UserReservationDataResponse;
import com.epam.incubation.service.reservationbooking.responsemodel.ReservationApiResponse;

public interface ReservationService {

	public ReservationDataModel bookReservation(ReservationRequestModel reservation);

	public ReservationDataModel cancelReservation(Integer id);

	public ReservationDataModel getReservation(Integer id);

	public List<ReservationDataModel> getReservationByHotelId(Integer hotelId);
	
	public ReservationApiResponse<UserReservationDataResponse> getGuestReservationHisotry(Integer guestId);
}
