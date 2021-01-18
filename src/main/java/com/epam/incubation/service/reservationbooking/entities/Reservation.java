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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;

@Entity
public class Reservation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "RESERVATION_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer reservationId;
	@Column(name = "GUEST_ID")
	private Integer guestId;
	@Column(name = "HOTEL_ID")
	private Integer hotelId;

	@Column(name = "CHECK_IN_DATE")
	private Date checkInDate;

	@Column(name = "CHECK_OUT_DATE")
	private Date checkOutDate;

	@OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PaymentDetails paymentsDetails;

	@OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ReservationLineDetails> reservationLineDetails;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name = "STATE")
	private String state;

	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;

	public Reservation() {

	}

	public Reservation(ReservationDataModel reservationDataModel) {
		this.guestId = reservationDataModel.getGuestId();
		this.hotelId = reservationDataModel.getHotelId();
		this.checkInDate = reservationDataModel.getCheckInDate();
		this.checkOutDate = reservationDataModel.getCheckOutDate();
		this.paymentsDetails = new PaymentDetails(reservationDataModel.getPaymentsDetails());
		this.reservationLineDetails = reservationDataModel.getReservationLineDetails().stream()
				.map(ReservationLineDetails::new).collect(Collectors.toList());
		this.createDate = reservationDataModel.getCreateDate();
		this.lastUpdateDate = reservationDataModel.getLastUpdateDate();
		this.state = reservationDataModel.getState();
		this.totalAmount = reservationDataModel.getTotalAmount();
		this.reservationLineDetails.forEach(r->r.setReservation(this));
		this.paymentsDetails.setReservation(this);
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

	public PaymentDetails getPaymentsDetails() {
		return paymentsDetails;
	}

	public void setPaymentsDetails(PaymentDetails paymentsDetails) {
		this.paymentsDetails = paymentsDetails;
	}

	public List<ReservationLineDetails> getReservationLineDetails() {
		return reservationLineDetails;
	}

	public void setReservationLineDetails(List<ReservationLineDetails> reservationLineDetails) {
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
