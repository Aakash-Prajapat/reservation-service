package com.epam.incubation.service.reservationbooking.contract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.epam.incubation.service.reservationbooking.resource.ReservationBookingResourceImpl;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WithMockUser(username = "Guest", password = "test", roles = { "GUEST" })
public class BaseTestClass {
	
	@Autowired
	private ReservationBookingResourceImpl reservationBookingResourceImpl;
	
	@Autowired
	WebApplicationContext context;

	@BeforeEach
	void setup() {
		StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(reservationBookingResourceImpl);
		RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
		RestAssuredMockMvc.webAppContextSetup(context);
	}
}