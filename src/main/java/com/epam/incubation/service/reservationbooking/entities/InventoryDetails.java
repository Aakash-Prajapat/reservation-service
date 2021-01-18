
package com.epam.incubation.service.reservationbooking.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.epam.incubation.service.reservationbooking.datamodel.InventoryDetailsDataModel;

@Entity
public class InventoryDetails {

	@Id
	@Column(name = "RESERVATION_INVENTORY_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer reservationInventoryId;
	
	@Column(name = "INVENTORY_ID")
	private Integer inventoryId;

	@Column(name = "ROOM_ID")
	private Integer roomId;

	@Column(name = "STAY_DATE")
	private Date stayDate;

	@Column(name = "AMOUNT_PER_NIGHT")
	private Double amoutPerInventory;

	@ManyToOne
	private ReservationLineDetails reservationLineDetails;

	public InventoryDetails() {
	}

	public InventoryDetails(InventoryDetailsDataModel inventoryDetailsDataModel) {
		this.inventoryId = inventoryDetailsDataModel.getInventoryId();
		this.roomId = inventoryDetailsDataModel.getRoomId();
		this.stayDate = inventoryDetailsDataModel.getStayDate();
		this.amoutPerInventory = inventoryDetailsDataModel.getAmoutPerInventory();
	}

	public Integer getReservationInventoryId() {
		return reservationInventoryId;
	}

	public void setReservationInventoryId(Integer reservationInventoryId) {
		this.reservationInventoryId = reservationInventoryId;
	}

	public Integer getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
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

	public void setReservationLineDetails(ReservationLineDetails reservationLineDetails) {
		this.reservationLineDetails = reservationLineDetails;
	}
}
