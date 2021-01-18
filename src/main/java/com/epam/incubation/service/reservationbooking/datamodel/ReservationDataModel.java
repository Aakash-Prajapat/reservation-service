package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.epam.incubation.service.reservationbooking.entities.Reservation;

public class ReservationDataModel {

	private Integer reservationId;
	@NotNull(message = "GuestId is mandatory")
	private Integer guestId;
	@NotNull(message = "HotelId is mandatory")
	private Integer hotelId;
	@NotNull(message = "Check In Date is mandatory")
	private Date checkInDate;
	@NotNull(message = "Check Out Date is mandatory")
	private Date checkOutDate;
	private PaymentDetailsDataModel paymentsDetails;
	private List<ReservationLineDetailsDataModel> reservationLineDetails;
	private Date createDate;
	private Date lastUpdateDate;
	private String state;
	private Double totalAmount;

	public ReservationDataModel() {

	}

	public ReservationDataModel(Reservation reservation) {
		this.reservationId = reservation.getReservationId();
		this.guestId = reservation.getGuestId();
		this.hotelId = reservation.getHotelId();
		this.checkInDate = reservation.getCheckInDate();
		this.checkOutDate = reservation.getCheckOutDate();
		this.createDate = reservation.getCreateDate();
		this.lastUpdateDate = reservation.getLastUpdateDate();
		this.totalAmount = reservation.getTotalAmount();
		this.paymentsDetails = new PaymentDetailsDataModel(reservation.getPaymentsDetails());
		this.reservationLineDetails = reservation.getReservationLineDetails().stream()
				.map(ReservationLineDetailsDataModel::new).collect(Collectors.toList());
		this.state = reservation.getState();
	}

	public ReservationDataModel(Integer reservationId, Integer hotelId, Integer guestId) {
		this.reservationId = reservationId;
		this.hotelId = hotelId;
		this.guestId = guestId;
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

	public PaymentDetailsDataModel getPaymentsDetails() {
		return paymentsDetails;
	}

	public void setPaymentsDetails(PaymentDetailsDataModel paymentsDetails) {
		this.paymentsDetails = paymentsDetails;
	}

	public List<ReservationLineDetailsDataModel> getReservationLineDetails() {
		return reservationLineDetails;
	}

	public void setReservationLineDetails(List<ReservationLineDetailsDataModel> reservationLineDetails) {
		this.reservationLineDetails = reservationLineDetails;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
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

}
