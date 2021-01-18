package com.epam.incubation.service.reservationbooking.exception;

public class ReservationAlreadyCancelled extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ReservationAlreadyCancelled(String message) {
		super(message);
	}
}
