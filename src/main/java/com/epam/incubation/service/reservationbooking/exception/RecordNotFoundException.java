package com.epam.incubation.service.reservationbooking.exception;

public class RecordNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RecordNotFoundException(String message) {
		super(message);
	}

}
