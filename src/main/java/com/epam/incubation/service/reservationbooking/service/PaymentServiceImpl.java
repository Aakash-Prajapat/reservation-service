package com.epam.incubation.service.reservationbooking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.incubation.service.reservationbooking.datamodel.TransactionDetails;
import com.epam.incubation.service.reservationbooking.facade.PaymentServiceProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class PaymentServiceImpl implements PaymentService{

	private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
	
	@Autowired
	PaymentServiceProxy paymentServiceProxy;

	@HystrixCommand(fallbackMethod = "doPaymentFallback")
	public String doPayment(Long cardNumber, double amount) {
		return paymentServiceProxy.doPayment(new TransactionDetails(cardNumber, amount));
	}

	public String doPaymentFallback(Long cardNumber, double amount) {
		logger.info("In fall back of do payment with {} {}", cardNumber, amount);
		return "success";
	}

	@HystrixCommand(fallbackMethod = "creditTrasactioFallback")
	public String creditTrasaction(Long cardNumber, double amount) {
		return paymentServiceProxy.creditTransaction(new TransactionDetails(cardNumber, amount));
	}

	public String creditTrasactioFallback(Long cardNumber, double amount) {
		logger.info("In fall back of credit transaction with {} {}", cardNumber, amount);
		return "success";
	}

}
