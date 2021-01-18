package com.epam.incubation.service.reservationbooking.datamodel;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.epam.incubation.service.reservationbooking.entities.PaymentDetails;

public class PaymentDetailsDataModel {

	private Integer paymentId;
	@NotNull(message = "GuestId is mandatory")
	private Integer guestId;
	@NotNull(message = "CreditCardNumber is mandatory")
	private Long creditCardNumber;
	@NotNull(message = "CVV is mandatory")
	private Integer cvv;
	@NotNull(message = "Card Holder Name is mandatory")
	private String cardHolder;
	@NotNull(message = "Expiry Date is mandatory")
	private Date expiryDate;
	@NotNull(message = "Card Type is mandatory")
	private String cardType;

	public PaymentDetailsDataModel() {
	}

	public PaymentDetailsDataModel(PaymentDetails paymentDetails) {
		this.paymentId = paymentDetails.getPaymentId();
		this.guestId = paymentDetails.getGuestId();
		this.creditCardNumber = paymentDetails.getCreditCardNumber();
		this.cvv = paymentDetails.getCvv();
		this.cardHolder = paymentDetails.getCardHolder();
		this.expiryDate = paymentDetails.getExpiryDate();
		this.cardType = paymentDetails.getCardType();
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

}
