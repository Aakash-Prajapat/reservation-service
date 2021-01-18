package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

public class ReservationRequestModel {

	@NotNull(message = "GuestId is mandatory")
	private Integer guestId;
	@NotNull(message = "HotelId is mandatory")
	private Integer hotelId;
	@NotNull(message = "Check In Date is mandatory")
	private Date checkInDate;
	@NotNull(message = "Check Out Date is mandatory")
	private Date checkOutDate;
	private PaymentDetailsDataModel paymentsDetails;
	private List<ReservationLineDetailsRequestModel> reservationLineDetails;

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

	public List<ReservationLineDetailsRequestModel> getReservationLineDetails() {
		return reservationLineDetails;
	}

	public void setReservationLineDetails(List<ReservationLineDetailsRequestModel> reservationLineDetails) {
		this.reservationLineDetails = reservationLineDetails;
	}
}
