package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.epam.incubation.service.reservationbooking.entities.ReservationLineDetails;

public class ReservationLineDetailsDataModel {

	private Integer lineId;
	private Integer reservationId;
	private Integer roomId;
	private Date checkInDate;
	private Date checkOutDate;
	private List<GuestDetailsDataModel> guestDetails;
	private List<InventoryDetailsDataModel> inventoriesDetails;
	private Double amountPerRoom;

	public ReservationLineDetailsDataModel() {

	}

	public ReservationLineDetailsDataModel(ReservationLineDetails reservationLineDetails) {
		this.lineId = reservationLineDetails.getReservationLineId();
		this.roomId = reservationLineDetails.getRoomId();
		this.checkInDate = reservationLineDetails.getCheckInDate();
		this.checkOutDate = reservationLineDetails.getCheckOutDate();
		this.guestDetails = reservationLineDetails.getGuestDetails().stream().map(GuestDetailsDataModel::new)
				.collect(Collectors.toList());
		this.inventoriesDetails = reservationLineDetails.getInventoriesDetails().stream()
				.map(InventoryDetailsDataModel::new).collect(Collectors.toList());
		this.amountPerRoom = reservationLineDetails.getAmountPerRoom();
	}
	
	public ReservationLineDetailsDataModel(ReservationLineDetailsRequestModel request) {
		this.roomId = request.getRoomId();
		this.checkInDate = request.getCheckInDate();
		this.checkOutDate = request.getCheckOutDate();
		this.guestDetails = request.getGuestList().stream().map(GuestDetailsDataModel::new).collect(Collectors.toList());
		this.amountPerRoom = request.getInventoriesDetails().stream().mapToDouble(InventoryDetailsDataModel::getAmoutPerInventory).sum();
		this.inventoriesDetails = request.getInventoriesDetails();
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
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

	public List<GuestDetailsDataModel> getGuestDetails() {
		return guestDetails;
	}

	public void setGuestDetails(List<GuestDetailsDataModel> guestDetails) {
		this.guestDetails = guestDetails;
	}

	public List<InventoryDetailsDataModel> getInventoriesDetails() {
		return inventoriesDetails;
	}

	public void setInventoriesDetails(List<InventoryDetailsDataModel> inventoriesDetails) {
		this.inventoriesDetails = inventoriesDetails;
	}

	public Double getAmountPerRoom() {
		return amountPerRoom;
	}

	public void setAmountPerRoom(Double amountPerRoom) {
		this.amountPerRoom = amountPerRoom;
	}

}
