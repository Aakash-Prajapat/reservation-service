package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.List;

public class UserReservationDataResponse {

	private List<UserReservationData> reservations;
	
	public List<UserReservationData> getReservations() {
		return reservations;
	}

	public void setReservations(List<UserReservationData> reservations) {
		this.reservations = reservations;
	}

}
