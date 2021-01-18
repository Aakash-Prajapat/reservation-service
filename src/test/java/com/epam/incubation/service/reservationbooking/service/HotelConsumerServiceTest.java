package com.epam.incubation.service.reservationbooking.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

class HotelConsumerServiceTest {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@InjectMocks
	HotelConsumerService hotelConsumerService;
	
	@Value("${hotel.inventory.service}")
	private String inventoryServiceUrl;

	@Test
	

}
