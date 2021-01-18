package com.epam.incubation.service.reservationbooking.resource;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.epam.incubation.service.reservationbooking.datamodel.GuestDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.GuestDetailsRequestModel;
import com.epam.incubation.service.reservationbooking.datamodel.InventoryDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.PaymentDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationLineDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationLineDetailsRequestModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;
import com.epam.incubation.service.reservationbooking.exception.RecordNotFoundException;
import com.epam.incubation.service.reservationbooking.service.ReservationServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ReservationBookingResourceImpl.class)
@ExtendWith(MockitoExtension.class)
class ReservationBookingResourceImplTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private ObjectMapper objectMapper;

	@MockBean
	private ReservationServiceImpl reservationService;

	@BeforeEach
	public void Setup() throws ParseException {
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
		// 1. Reservation
		ReservationDataModel reservationDataModel = new ReservationDataModel();
		reservationDataModel.setReservationId(1);
		reservationDataModel.setGuestId(1);
		reservationDataModel.setHotelId(1);
		reservationDataModel.setCheckInDate(myFormat.parse("01-02-2021"));
		reservationDataModel.setCheckOutDate(myFormat.parse("02-02-2021"));

		PaymentDetailsDataModel paymentDetails = new PaymentDetailsDataModel();
		paymentDetails.setGuestId(1);
		paymentDetails.setCardHolder("Aakash Prajapat");
		paymentDetails.setCardType("VISA");
		paymentDetails.setCreditCardNumber(1212121212L);
		paymentDetails.setCvv(123);
		paymentDetails.setExpiryDate(myFormat.parse("01-02-2022"));

		reservationDataModel.setPaymentsDetails(paymentDetails);
		reservationDataModel.setLastUpdateDate(new Date());
		reservationDataModel.setCreateDate(new Date());
		reservationDataModel.setState("BOOKED");
		reservationDataModel.setTotalAmount(200.0);
		ReservationLineDetailsDataModel reservationLineDetailsDataModel = new ReservationLineDetailsDataModel();
		reservationLineDetailsDataModel.setLineId(1);
		reservationLineDetailsDataModel.setReservationId(1);
		reservationLineDetailsDataModel.setCheckInDate(myFormat.parse("01-02-2021"));
		reservationLineDetailsDataModel.setCheckInDate(myFormat.parse("02-02-2021"));
		reservationLineDetailsDataModel.setAmountPerRoom(200.0);
		reservationLineDetailsDataModel.setRoomId(1);
		GuestDetailsDataModel guestDetailsDataModel = new GuestDetailsDataModel();
		guestDetailsDataModel.setFirstName("Aakash");
		guestDetailsDataModel.setMiddleName("Kumar");
		guestDetailsDataModel.setLastName("Prajapat");
		guestDetailsDataModel.setHouseNumber("1");
		guestDetailsDataModel.setStreet("Nagar Road");
		guestDetailsDataModel.setCity("Indore");
		guestDetailsDataModel.setState("Madhya Pradesh");
		guestDetailsDataModel.setZipcode(452006);
		guestDetailsDataModel.setCountry("India");
		guestDetailsDataModel.setEmail("abc.xyz.com");
		guestDetailsDataModel.setPhoneNumber(1212121212L);
		guestDetailsDataModel.setGender('M');
		reservationLineDetailsDataModel.setGuestDetails(Arrays.asList(guestDetailsDataModel));
		InventoryDetailsDataModel inventoryDetailsDataModel1 = new InventoryDetailsDataModel();
		inventoryDetailsDataModel1.setInventoryId(1);
		inventoryDetailsDataModel1.setAmoutPerInventory(100.0);
		inventoryDetailsDataModel1.setRoomId(1);
		inventoryDetailsDataModel1.setStayDate(myFormat.parse("01-02-2021"));

		InventoryDetailsDataModel inventoryDetailsDataModel2 = new InventoryDetailsDataModel();
		inventoryDetailsDataModel2.setInventoryId(2);
		inventoryDetailsDataModel2.setAmoutPerInventory(100.0);
		inventoryDetailsDataModel2.setRoomId(1);
		inventoryDetailsDataModel2.setStayDate(myFormat.parse("02-02-2021"));

		reservationLineDetailsDataModel
				.setInventoriesDetails(Arrays.asList(inventoryDetailsDataModel1, inventoryDetailsDataModel2));
		reservationDataModel.setReservationLineDetails(Arrays.asList(reservationLineDetailsDataModel));

		// 2. Reservation

		ReservationDataModel reservationDataModel1 = new ReservationDataModel();
		reservationDataModel1.setReservationId(2);
		reservationDataModel1.setGuestId(2);
		reservationDataModel1.setHotelId(1);
		reservationDataModel1.setCheckInDate(myFormat.parse("06-02-2021"));
		reservationDataModel1.setCheckOutDate(myFormat.parse("07-02-2021"));

		PaymentDetailsDataModel paymentDetails1 = new PaymentDetailsDataModel();
		paymentDetails1.setGuestId(2);
		paymentDetails1.setCardHolder("Ankit Sonparate");
		paymentDetails1.setCardType("VISA");
		paymentDetails1.setCreditCardNumber(21212121L);
		paymentDetails1.setCvv(123);
		paymentDetails1.setExpiryDate(myFormat.parse("01-02-2022"));

		reservationDataModel1.setPaymentsDetails(paymentDetails1);
		reservationDataModel1.setLastUpdateDate(new Date());
		reservationDataModel1.setCreateDate(new Date());
		reservationDataModel1.setState("BOOKED");
		reservationDataModel1.setTotalAmount(200.0);
		ReservationLineDetailsDataModel reservationLineDetailsDataModel1 = new ReservationLineDetailsDataModel();
		reservationLineDetailsDataModel1.setLineId(1);
		reservationLineDetailsDataModel1.setReservationId(2);
		reservationLineDetailsDataModel1.setCheckInDate(myFormat.parse("06-02-2021"));
		reservationLineDetailsDataModel1.setCheckInDate(myFormat.parse("07-02-2021"));
		reservationLineDetailsDataModel1.setAmountPerRoom(100.0);
		reservationLineDetailsDataModel1.setRoomId(2);
		GuestDetailsDataModel guestDetailsDataModel1 = new GuestDetailsDataModel();
		guestDetailsDataModel1.setFirstName("Ankit");
		guestDetailsDataModel1.setMiddleName("Kumar");
		guestDetailsDataModel1.setLastName("Sonparate");
		guestDetailsDataModel1.setHouseNumber("1");
		guestDetailsDataModel1.setStreet("Nagar Road");
		guestDetailsDataModel1.setCity("Pune");
		guestDetailsDataModel1.setState("Maharashtra");
		guestDetailsDataModel1.setZipcode(411014);
		guestDetailsDataModel1.setCountry("India");
		guestDetailsDataModel1.setEmail("abc.xyz.com");
		guestDetailsDataModel1.setPhoneNumber(2121212121L);
		guestDetailsDataModel1.setGender('M');
		reservationLineDetailsDataModel1.setGuestDetails(Arrays.asList(guestDetailsDataModel1));
		InventoryDetailsDataModel inventoryDetailsDataModel3 = new InventoryDetailsDataModel();
		inventoryDetailsDataModel3.setInventoryId(3);
		inventoryDetailsDataModel3.setAmoutPerInventory(100.0);
		inventoryDetailsDataModel3.setRoomId(2);
		inventoryDetailsDataModel3.setStayDate(myFormat.parse("06-02-2021"));

		InventoryDetailsDataModel inventoryDetailsDataModel4 = new InventoryDetailsDataModel();
		inventoryDetailsDataModel4.setInventoryId(4);
		inventoryDetailsDataModel4.setAmoutPerInventory(100.0);
		inventoryDetailsDataModel4.setRoomId(2);
		inventoryDetailsDataModel4.setStayDate(myFormat.parse("07-02-2021"));

		reservationLineDetailsDataModel1
				.setInventoriesDetails(Arrays.asList(inventoryDetailsDataModel3, inventoryDetailsDataModel4));
		reservationDataModel1.setReservationLineDetails(Arrays.asList(reservationLineDetailsDataModel1));

		given(reservationService.getReservation(1)).willReturn(reservationDataModel);
		given(reservationService.getReservationByHotelId(1))
				.willReturn(Arrays.asList(reservationDataModel, reservationDataModel1));

	}

	@Test
	@Disabled
	void getReservation_ShouldReturnReservation() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservationservice/1")).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print()).andExpect(jsonPath("$.hotelId").value("1"))
				.andExpect(jsonPath("$.guestId").value("1")).andExpect(jsonPath("$.reservationLineDetails", hasSize(1)))
				.andExpect(jsonPath("$.reservationLineDetails[0].inventoriesDetails", hasSize(2)));
	}

	@Test
	@Disabled
	void getReservation_notFound() throws Exception {
		given(reservationService.getReservation(1)).willThrow(new RecordNotFoundException("Record Not Found with 1"));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservationservice/1")).andExpect(status().isNotFound());
	}

	@Test
	@Disabled
	void getReservationByHotelId_ShouldReturnReservations() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservationservice/reservationByHotelId/1"))
				.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].reservationId").value("1"))
				.andExpect(jsonPath("$[1].reservationId").value("2"))
				.andExpect(jsonPath("$[0].reservationLineDetails", hasSize(1)))
				.andExpect(jsonPath("$[1].reservationLineDetails[0].inventoriesDetails", hasSize(2)));
	}

	@Test
	@Disabled
	void getReservationByHotelId_notFound() throws Exception {
		given(reservationService.getReservationByHotelId(1))
				.willThrow(new RecordNotFoundException("Record Not Found with 1"));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservationservice/reservationByHotelId/1"))
				.andExpect(status().isNotFound());
	}

	@Test
	void bookReservation_ShouldBookedReservationAndReturnReservationIdWithOtherDetails()
			throws JsonProcessingException, Exception {
		
		// Request Reservation
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
		ReservationRequestModel requestReservationDataModel = new ReservationRequestModel();
		requestReservationDataModel.setGuestId(1);
		requestReservationDataModel.setHotelId(1);
		requestReservationDataModel.setCheckInDate(myFormat.parse("01-02-2021"));
		requestReservationDataModel.setCheckOutDate(myFormat.parse("02-02-2021"));

		PaymentDetailsDataModel requestPaymentDetails = new PaymentDetailsDataModel();
		requestPaymentDetails.setPaymentId(1);
		requestPaymentDetails.setGuestId(1);
		requestPaymentDetails.setCardHolder("Aakash Prajapat");
		requestPaymentDetails.setCardType("VISA");
		requestPaymentDetails.setCreditCardNumber(1212121212L);
		requestPaymentDetails.setCvv(123);
		requestPaymentDetails.setExpiryDate(myFormat.parse("01-02-2022"));

		requestReservationDataModel.setPaymentsDetails(requestPaymentDetails);
		ReservationLineDetailsRequestModel requestReservationLineDetailsDataModel = new ReservationLineDetailsRequestModel();
		requestReservationLineDetailsDataModel.setCheckInDate(myFormat.parse("01-02-2021"));
		requestReservationLineDetailsDataModel.setCheckOutDate(myFormat.parse("02-02-2021"));
		requestReservationLineDetailsDataModel.setRoomId(1);
		GuestDetailsRequestModel requestGuestDetailsDataModel = new GuestDetailsRequestModel();
		requestGuestDetailsDataModel.setFirstName("Aakash");
		requestGuestDetailsDataModel.setMiddleName("Kumar");
		requestGuestDetailsDataModel.setLastName("Prajapat");
		requestGuestDetailsDataModel.setHouseNumber("1");
		requestGuestDetailsDataModel.setStreet("Nagar Road");
		requestGuestDetailsDataModel.setCity("Indore");
		requestGuestDetailsDataModel.setState("Madhya Pradesh");
		requestGuestDetailsDataModel.setZipcode(452006);
		requestGuestDetailsDataModel.setCountry("India");
		requestGuestDetailsDataModel.setEmail("abc.xyz.com");
		requestGuestDetailsDataModel.setPhoneNumber(1212121212L);
		requestGuestDetailsDataModel.setGender('M');
		requestReservationLineDetailsDataModel.setGuestList(Arrays.asList(requestGuestDetailsDataModel));
		InventoryDetailsDataModel requestInventoryDetailsDataModel1 = new InventoryDetailsDataModel();
		requestInventoryDetailsDataModel1.setInventoryId(1);
		requestInventoryDetailsDataModel1.setAmoutPerInventory(100.0);
		requestInventoryDetailsDataModel1.setRoomId(1);
		requestInventoryDetailsDataModel1.setStayDate(myFormat.parse("01-02-2021"));

		InventoryDetailsDataModel requestInventoryDetailsDataModel2 = new InventoryDetailsDataModel();
		requestInventoryDetailsDataModel2.setInventoryId(2);
		requestInventoryDetailsDataModel2.setAmoutPerInventory(100.0);
		requestInventoryDetailsDataModel2.setRoomId(1);
		requestInventoryDetailsDataModel2.setStayDate(myFormat.parse("02-02-2021"));

		requestReservationLineDetailsDataModel.setInventoriesDetails(
				Arrays.asList(requestInventoryDetailsDataModel1, requestInventoryDetailsDataModel2));
		requestReservationDataModel.setReservationLineDetails(Arrays.asList(requestReservationLineDetailsDataModel));

		// Return Reservation
		ReservationDataModel reservationDataModel = new ReservationDataModel();
		reservationDataModel.setReservationId(1);
		reservationDataModel.setGuestId(1);
		reservationDataModel.setHotelId(1);
		reservationDataModel.setCheckInDate(myFormat.parse("01-02-2021"));
		reservationDataModel.setCheckOutDate(myFormat.parse("02-02-2021"));

		PaymentDetailsDataModel paymentDetails = new PaymentDetailsDataModel();
		paymentDetails.setGuestId(1);
		paymentDetails.setCardHolder("Aakash Prajapat");
		paymentDetails.setCardType("VISA");
		paymentDetails.setCreditCardNumber(1212121212L);
		paymentDetails.setCvv(123);
		paymentDetails.setExpiryDate(myFormat.parse("01-02-2022"));

		reservationDataModel.setPaymentsDetails(paymentDetails);
		reservationDataModel.setLastUpdateDate(new Date());
		reservationDataModel.setCreateDate(new Date());
		reservationDataModel.setState("BOOKED");
		reservationDataModel.setTotalAmount(200.0);
		ReservationLineDetailsDataModel reservationLineDetailsDataModel = new ReservationLineDetailsDataModel();
		reservationLineDetailsDataModel.setLineId(1);
		reservationLineDetailsDataModel.setReservationId(1);
		reservationLineDetailsDataModel.setCheckInDate(myFormat.parse("01-02-2021"));
		reservationLineDetailsDataModel.setCheckOutDate(myFormat.parse("02-02-2021"));
		reservationLineDetailsDataModel.setAmountPerRoom(200.0);
		reservationLineDetailsDataModel.setRoomId(1);
		GuestDetailsDataModel guestDetailsDataModel = new GuestDetailsDataModel();
		guestDetailsDataModel.setFirstName("Aakash");
		guestDetailsDataModel.setMiddleName("Kumar");
		guestDetailsDataModel.setLastName("Prajapat");
		guestDetailsDataModel.setHouseNumber("1");
		guestDetailsDataModel.setStreet("Nagar Road");
		guestDetailsDataModel.setCity("Indore");
		guestDetailsDataModel.setState("Madhya Pradesh");
		guestDetailsDataModel.setZipcode(452006);
		guestDetailsDataModel.setCountry("India");
		guestDetailsDataModel.setEmail("abc.xyz.com");
		guestDetailsDataModel.setPhoneNumber(1212121212L);
		guestDetailsDataModel.setGender('M');
		reservationLineDetailsDataModel.setGuestDetails(Arrays.asList(guestDetailsDataModel));
		InventoryDetailsDataModel inventoryDetailsDataModel1 = new InventoryDetailsDataModel();
		inventoryDetailsDataModel1.setInventoryId(1);
		inventoryDetailsDataModel1.setAmoutPerInventory(100.0);
		inventoryDetailsDataModel1.setRoomId(1);
		inventoryDetailsDataModel1.setStayDate(myFormat.parse("01-02-2021"));

		InventoryDetailsDataModel inventoryDetailsDataModel2 = new InventoryDetailsDataModel();
		inventoryDetailsDataModel2.setInventoryId(2);
		inventoryDetailsDataModel2.setAmoutPerInventory(100.0);
		inventoryDetailsDataModel2.setRoomId(1);
		inventoryDetailsDataModel2.setStayDate(myFormat.parse("02-02-2021"));

		reservationLineDetailsDataModel
				.setInventoriesDetails(Arrays.asList(inventoryDetailsDataModel1, inventoryDetailsDataModel2));
		reservationDataModel.setReservationLineDetails(Arrays.asList(reservationLineDetailsDataModel));
		
		given(reservationService.bookReservation(requestReservationDataModel)).willReturn(reservationDataModel);
		//when(reservationService.bookReservation(requestReservationDataModel)).thenReturn(reservationDataModel);
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/v1/reservationservice").content(asJsonString(requestReservationDataModel))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.reservationLineDetails", hasSize(1)));

		verify(reservationService, times(1)).bookReservation(requestReservationDataModel);

	}

	@Test
	@Disabled
	void whenNullValue_thenReturns400() throws Exception {
		String requestReservationDataModelString = "{\"guestId\":null,\"hotelId\": null,\"checkInDate\":1612117800000,\"checkOutDate\":1612204200000,\"paymentsDetails\":{\"paymentId\":1,\"guestId\":1,\"creditCardNumber\":1212121212,\"cvv\":123,\"cardHolder\":\"Aakash Prajapat\",\"expiryDate\":1643653800000,\"cardType\":\"VISA\"},\"reservationLineDetails\":[{\"roomId\":1,\"checkInDate\":1612204200000,\"checkOutDate\":null,\"guestList\":[{\"firstName\":\"Aakash\",\"middleName\":\"Kumar\",\"lastName\":\"Prajapat\",\"houseNumber\":\"1\",\"street\":\"Nagar Road\",\"city\":\"Indore\",\"state\":\"Madhya Pradesh\",\"zipcode\":452006,\"country\":\"India\",\"email\":\"abc.xyz.com\",\"phoneNumber\":1212121212,\"gender\":\"M\"}],\"inventoriesDetails\":[{\"roomId\":1,\"inventoryId\":1,\"stayDate\":1612117800000,\"amoutPerInventory\":100.0},{\"roomId\":1,\"inventoryId\":2,\"stayDate\":1612204200000,\"amoutPerInventory\":100.0}]}]}\r\n"
				+ "";
		mockMvc.perform(MockMvcRequestBuilders.post("/reservationservice").content(requestReservationDataModelString)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
