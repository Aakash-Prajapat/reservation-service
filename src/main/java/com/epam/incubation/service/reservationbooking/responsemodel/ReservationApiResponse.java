package com.epam.incubation.service.reservationbooking.responsemodel;

import org.springframework.http.HttpStatus;

import com.epam.incubation.service.reservationbooking.datamodel.ApiError;

public class ReservationApiResponse<T> {

	private T data;
	private ApiError error;
	private HttpStatus status;

	public ReservationApiResponse() {
	}

	public ReservationApiResponse(T data, HttpStatus status, ApiError error) {
		this.data = data;
		this.error = error;
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ApiError getError() {
		return error;
	}

	public void setError(ApiError error) {
		this.error = error;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
