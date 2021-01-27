package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

public class ApiError {
	private HttpStatus status;
	private String message;
	private List<FieldError> subErrors;

	public ApiError() {
	}
	
	public ApiError(HttpStatus status) {
		this();
		this.status = status;
	}

	public ApiError(HttpStatus status, String message, List<FieldError> subErrors) {
		this();
		this.status = status;
		this.message = message;
		this.subErrors = subErrors;
	}

	public ApiError(HttpStatus status, String message) {
		this();
		this.status = status;
		this.message = message;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<FieldError> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<FieldError> subErrors) {
		this.subErrors = subErrors;
	}
}
