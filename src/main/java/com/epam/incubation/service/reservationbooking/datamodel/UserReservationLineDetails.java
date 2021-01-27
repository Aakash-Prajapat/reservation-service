package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.List;
import java.util.stream.Collectors;

import com.epam.incubation.service.reservationbooking.entities.ReservationLineDetails;

public class UserReservationLineDetails {
	private Integer roomId;
	private List<UserGuestDetails> guestDetails;

	public UserReservationLineDetails() {
		
	}
	
	public UserReservationLineDetails(ReservationLineDetails lineDetails) {
		this.roomId = lineDetails.getRoomId();
		this.guestDetails = lineDetails.getGuestDetails().stream().map(UserGuestDetails::new).collect(Collectors.toList());
	}
	
	
	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public List<UserGuestDetails> getGuestDetails() {
		return guestDetails;
	}

	public void setGuestDetails(List<UserGuestDetails> guestDetails) {
		this.guestDetails = guestDetails;
	}

}
