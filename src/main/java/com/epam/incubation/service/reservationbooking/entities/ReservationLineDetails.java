
package com.epam.incubation.service.reservationbooking.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.epam.incubation.service.reservationbooking.datamodel.ReservationLineDetailsDataModel;

@Entity
public class ReservationLineDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 12102120L;

	@Id
	@Column(name = "RESERVATION_LINE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer reservationLineId;

	@Column(name = "ROOM_ID")
	private Integer roomId;

	@Column(name = "CHECK_IN_DATE")
	private Date checkInDate;

	@Column(name = "CHECK_OUT_DATE")
	private Date checkOutDate;

	@OneToMany(mappedBy = "reservationLineDetails", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private List<GuestDetails> guestDetails;

	@OneToMany(mappedBy = "reservationLineDetails", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private List<InventoryDetails> inventoriesDetails;

	@Column(name = "AMOUNT_PER_ROOM")
	private Double amountPerRoom;

	@ManyToOne
	private Reservation reservation;

	public ReservationLineDetails() {

	}

	public ReservationLineDetails(ReservationLineDetailsDataModel reservationLineDetailsDataModel) {
		this.roomId = reservationLineDetailsDataModel.getRoomId();
		this.checkInDate = reservationLineDetailsDataModel.getCheckInDate();
		this.checkOutDate = reservationLineDetailsDataModel.getCheckOutDate();
		this.guestDetails = reservationLineDetailsDataModel.getGuestDetails().stream().map(GuestDetails::new)
				.collect(Collectors.toList());
		this.inventoriesDetails = reservationLineDetailsDataModel.getInventoriesDetails().stream()
				.map(InventoryDetails::new).collect(Collectors.toList());
		this.amountPerRoom = reservationLineDetailsDataModel.getAmountPerRoom();
		this.guestDetails.forEach(g->g.setReservationLineDetails(this));
		this.inventoriesDetails.forEach(r->r.setReservationLineDetails(this));
	}

	public Integer getReservationLineId() {
		return reservationLineId;
	}

	public void setReservationLineId(Integer reservationLineId) {
		this.reservationLineId = reservationLineId;
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

	public List<GuestDetails> getGuestDetails() {
		return guestDetails;
	}

	public void setGuestDetails(List<GuestDetails> guestDetails) {
		this.guestDetails = guestDetails;
	}

	public List<InventoryDetails> getInventoriesDetails() {
		return inventoriesDetails;
	}

	public void setInventoriesDetails(List<InventoryDetails> inventoriesDetails) {
		this.inventoriesDetails = inventoriesDetails;
	}

	public Double getAmountPerRoom() {
		return amountPerRoom;
	}

	public void setAmountPerRoom(Double amountPerRoom) {
		this.amountPerRoom = amountPerRoom;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
}
