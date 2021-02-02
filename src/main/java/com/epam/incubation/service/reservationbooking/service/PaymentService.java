package com.epam.incubation.service.reservationbooking.service;

public interface PaymentService {

	public String doPayment(Long cardNumber, double amount);

	public String creditTrasaction(Long cardNumber, double amount);
}
