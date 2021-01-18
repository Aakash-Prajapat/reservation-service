package com.epam.incubation.service.reservationbooking;

import java.text.ParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootApplication
//@EnableEurekaClient
public class ReservationServiceApplication {

	public static void main(String[] args) throws ParseException, JsonProcessingException {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

}
