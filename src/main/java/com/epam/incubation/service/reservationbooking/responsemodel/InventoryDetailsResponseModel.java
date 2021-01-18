package com.epam.incubation.service.reservationbooking.responsemodel;

import java.util.List;

import com.epam.incubation.service.reservationbooking.datamodel.ApiError;

public class InventoryDetailsResponseModel {
	List<InventoryResponseModel> responseModel;
	ApiError error;

	public List<InventoryResponseModel> getResponseModel() {
		return responseModel;
	}

	public void setResponseModel(List<InventoryResponseModel> responseModel) {
		this.responseModel = responseModel;
	}

	public ApiError getError() {
		return error;
	}

	public void setError(ApiError error) {
		this.error = error;
	}

}
