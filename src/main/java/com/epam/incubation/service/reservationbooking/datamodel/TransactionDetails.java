package com.epam.incubation.service.reservationbooking.datamodel;

public class TransactionDetails {

	private Long cardNumber;
	private Double amount;

	public TransactionDetails(Long cardNumber, Double amount) {
		this.cardNumber = cardNumber;
		this.amount = amount;
	}

	public TransactionDetails() {
	}

	public Long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
