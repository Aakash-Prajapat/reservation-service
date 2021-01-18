package com.epam.incubation.service.reservationbooking.requestmodel;

import java.util.List;

public class InventoryDetailsRequestModel {
	List<InventoryRequestModel> getInventoryDetails;

	public List<InventoryRequestModel> getGetInventoryDetails() {
		return getInventoryDetails;
	}

	public void setGetInventoryDetails(List<InventoryRequestModel> getInventoryDetails) {
		this.getInventoryDetails = getInventoryDetails;
	}

}
