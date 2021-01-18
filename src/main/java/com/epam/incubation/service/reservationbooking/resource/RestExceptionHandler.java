package com.epam.incubation.service.reservationbooking.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.epam.incubation.service.reservationbooking.datamodel.ApiError;
import com.epam.incubation.service.reservationbooking.exception.RecordNotFoundException;
import com.epam.incubation.service.reservationbooking.exception.ReservationAlreadyCancelled;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(RecordNotFoundException.class)
	protected ResponseEntity<ApiError> handleEntityNotFound(RecordNotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> globalExceptionHandling(Exception ex, WebRequest request) {
		ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ReservationAlreadyCancelled.class)
	protected ResponseEntity<ApiError> handleEntityNotFound(ReservationAlreadyCancelled ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
}
