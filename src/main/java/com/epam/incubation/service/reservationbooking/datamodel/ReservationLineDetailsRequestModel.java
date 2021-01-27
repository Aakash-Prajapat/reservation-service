package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

public class ReservationLineDetailsRequestModel {

	@NotNull(message = "Room Id is mandatory")
	private Integer roomId;
	@NotNull(message = "Check In Date is mandatory")
	private Date checkInDate;
	@NotNull(message = "Check Out Date is mandatory")
	private Date checkOutDate;
	private List<GuestDetailsRequestModel> guestList;
	private List<InventoryDetailsDataModel> inventoriesDetails;

	public List<InventoryDetailsDataModel> getInventoriesDetails() {
		return inventoriesDetails;
	}

	public void setInventoriesDetails(List<InventoryDetailsDataModel> inventoriesDetails) {
		this.inventoriesDetails = inventoriesDetails;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public List<GuestDetailsRequestModel> getGuestList() {
		return guestList;
	}

	public void setGuestList(List<GuestDetailsRequestModel> guestList) {
		this.guestList = guestList;
	}
}
