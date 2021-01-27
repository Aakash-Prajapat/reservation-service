package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.epam.incubation.service.reservationbooking.entities.Reservation;

public class UserReservationData {
	private Integer reservationId;
	private Integer guestId;
	private Integer hotelId;
	private Date checkInDate;
	private Date checkOutDate;
	private Date createDate;
	private String state;
	private Double totalAmount;
	private List<UserReservationLineDetails> reservationLineDetails;

	public UserReservationData() {
		
	}
	
	public UserReservationData(Reservation reservation) {
		this.reservationId = reservation.getReservationId();
		this.guestId = reservation.getGuestId();
		this.hotelId = reservation.getHotelId();
		this.checkInDate = reservation.getCheckInDate();
		this.checkOutDate = reservation.getCheckOutDate();
		this.state = reservation.getState();
		this.totalAmount = reservation.getTotalAmount();
		this.reservationLineDetails = reservation.getReservationLineDetails().stream().map(UserReservationLineDetails::new).collect(Collectors.toList());
	}
	
	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}
	
	public Integer getGuestId() {
		return guestId;
	}

	public void setGuestId(Integer guestId) {
		this.guestId = guestId;
	}

	public Integer getHotelId() {
		return hotelId;
	}

	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<UserReservationLineDetails> getReservationLineDetails() {
		return reservationLineDetails;
	}

	public void setReservationLineDetails(List<UserReservationLineDetails> reservationLineDetails) {
		this.reservationLineDetails = reservationLineDetails;
	}
}
