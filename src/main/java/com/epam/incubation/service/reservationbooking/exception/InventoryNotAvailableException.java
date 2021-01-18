package com.epam.incubation.service.reservationbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventoryNotAvailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InventoryNotAvailableException(String message) {
		super(message);
	}
}
