package com.epam.incubation.service.reservationbooking.facade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.epam.incubation.service.reservationbooking.datamodel.TransactionDetails;

@FeignClient(name = "gatewayserver")
public interface PaymentServiceProxy {

	@PostMapping("/paymentservice/dopayment")
	public String doPayment(TransactionDetails paymentDetails);

	@PostMapping("/paymentservice/credittransaction")
	public String creditTransaction(TransactionDetails paymentDetails);

}
