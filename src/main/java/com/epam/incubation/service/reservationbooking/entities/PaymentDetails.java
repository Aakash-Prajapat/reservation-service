
package com.epam.incubation.service.reservationbooking.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.epam.incubation.service.reservationbooking.datamodel.PaymentDetailsDataModel;

@Entity
public class PaymentDetails {

	@Id
	@Column(name = "PAYMENT_ID")
	private Integer paymentId;

	@Column(name = "GUEST_ID")
	private Integer guestId;

	@Column(name = "CREDIT_CARD_NUMBER")
	private Long creditCardNumber;

	@Column(name = "CC_CVV_NUMBER")
	private Integer cvv;

	@Column(name = "CARD_HOLDER_NAME")
	private String cardHolder;

	@Column(name = "EXPIRY_DATE")
	private Date expiryDate;

	@Column(name = "CC_TYPE")
	private String cardType;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "RESERVATION_ID")
	private Reservation reservation;

	public PaymentDetails() {
	}

	public PaymentDetails(PaymentDetailsDataModel paymentDetailsDataModel) {
		this.paymentId = paymentDetailsDataModel.getPaymentId();
		this.guestId = paymentDetailsDataModel.getGuestId();
		this.creditCardNumber = paymentDetailsDataModel.getCreditCardNumber();
		this.cvv = paymentDetailsDataModel.getCvv();
		this.cardHolder = paymentDetailsDataModel.getCardHolder();
		this.expiryDate = paymentDetailsDataModel.getExpiryDate();
		this.cardType = paymentDetailsDataModel.getCardType();
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getGuestId() {
		return guestId;
	}

	public void setGuestId(Integer guestId) {
		this.guestId = guestId;
	}

	public Long getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(Long creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public Integer getCvv() {
		return cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
}
