package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.Date;

import com.epam.incubation.service.reservationbooking.entities.InventoryDetails;
import com.epam.incubation.service.reservationbooking.responsemodel.InventoryResponseModel;

public class InventoryDetailsDataModel {

	private Integer roomId;
	private Integer inventoryId;
	private Date stayDate;
	private Double amoutPerInventory;

	public InventoryDetailsDataModel() {

	}

	public InventoryDetailsDataModel(InventoryDetails inventoryDetails) {
		this.inventoryId = inventoryDetails.getInventoryId();
		this.roomId = inventoryDetails.getRoomId();
		this.stayDate = inventoryDetails.getStayDate();
		this.amoutPerInventory = inventoryDetails.getAmoutPerInventory();
	}

	public InventoryDetailsDataModel(InventoryResponseModel responseModel) {
		this.inventoryId = responseModel.getInventoryId();
		this.roomId = responseModel.getRoomId();
		this.stayDate = responseModel.getStayDate();
		this.amoutPerInventory = responseModel.getRateRoom();
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Date getStayDate() {
		return stayDate;
	}

	public void setStayDate(Date stayDate) {
		this.stayDate = stayDate;
	}

	public Double getAmoutPerInventory() {
		return amoutPerInventory;
	}

	public void setAmoutPerInventory(Double amoutPerInventory) {
		this.amoutPerInventory = amoutPerInventory;
	}

}
