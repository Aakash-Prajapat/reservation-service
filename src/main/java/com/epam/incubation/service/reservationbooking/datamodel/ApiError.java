package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

public class ApiError {
	private HttpStatus status;
	private String message;
	private String details;
	private List<FieldError> subErrors;

	public ApiError() {
	}
	
	public ApiError(HttpStatus status) {
		this();
		this.status = status;
	}

	public ApiError(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.message = "Unexpected error";
		this.details = ex.getLocalizedMessage();
	}

	public ApiError(HttpStatus status, String message, Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.details = ex.getLocalizedMessage();
	}

	public ApiError(HttpStatus status, String message, String details, List<FieldError> list) {
		this();
		this.status = status;
		this.message = message;
		this.details = details;
		this.subErrors = list;
	}

	public ApiError(HttpStatus status, String message, String details) {
		this();
		this.status = status;
		this.message = message;
		this.details = details;
	}

	public ApiError(HttpStatus status, String message, List<FieldError> subErrors) {
		this();
		this.status = status;
		this.message = message;
		this.subErrors = subErrors;
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public List<FieldError> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<FieldError> subErrors) {
		this.subErrors = subErrors;
	}
}
