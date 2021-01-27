package com.epam.incubation.service.reservationbooking.resource;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
import com.epam.incubation.service.reservationbooking.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReservationBookingResourceImplTest {

	private MockMvc mockMvc;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private ReservationServiceImpl reservationService;

	@InjectMocks
	ReservationBookingResourceImpl reservationBookingResourceImpl;

	@Autowired
	FilterChainProxy springSecurityFilterChain;

	@Autowired
	JwtUtil jwtUtil;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(reservationBookingResourceImpl)
				.setControllerAdvice(new RestExceptionHandler()).apply(springSecurity(springSecurityFilterChain))
				.build();
	}

	@Test
	void getReservation_ShouldReturnReservation() throws Exception {
		ReservationDataModel reservationDataModel = getMockedReservation("getById").get(0);
		given(reservationService.getReservation(1)).willReturn(reservationDataModel);
		mockMvc.perform(MockMvcRequestBuilders.get("/reservationservice/getreservation/1")
				.with(user("Guest").password("password").roles("GUEST"))).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print()).andExpect(jsonPath("$.hotelId").value("1"))
				.andExpect(jsonPath("$.guestId").value("1")).andExpect(jsonPath("$.reservationLineDetails", hasSize(1)))
				.andExpect(jsonPath("$.reservationLineDetails[0].inventoriesDetails", hasSize(2)));
	}

	@Test
	void getReservation_notFound() throws Exception {
		given(reservationService.getReservation(1)).willThrow(new RecordNotFoundException("Record Not Found with 1"));

		mockMvc.perform(MockMvcRequestBuilders.get("/reservationservice/getreservation/1")
				.with(user("Guest").password("password").roles("GUEST"))).andExpect(status().isNotFound());
	}

	@Test
	void getReservation_ShouldThrowException() throws Exception {
		ReservationDataModel reservationDataModel = getMockedReservation("getById").get(0);
		lenient().doReturn(reservationDataModel).when(reservationService).getReservation(1);
		mockMvc.perform(MockMvcRequestBuilders.get("/reservationservice/getreservation/1")).andExpect(status().isForbidden())
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	void getReservationByHotelId_ShouldReturnReservations() throws Exception {
		List<ReservationDataModel> reservations = getMockedReservation("getByHotelId");
		given(reservationService.getReservationByHotelId(1)).willReturn(reservations);
		mockMvc.perform(MockMvcRequestBuilders.get("/reservationservice/getreservationsByHotelId/1")
				.with(user("Guest").password("password").roles("GUEST"))).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].reservationId").value("1"))
				.andExpect(jsonPath("$[1].reservationId").value("2"))
				.andExpect(jsonPath("$[0].reservationLineDetails", hasSize(1)))
				.andExpect(jsonPath("$[1].reservationLineDetails[0].inventoriesDetails", hasSize(2)));
	}

	@Test
	void getReservationByHotelId_notFound() throws Exception {
		lenient().doThrow(new RecordNotFoundException("Record Not Found with 1")).when(reservationService).getReservationByHotelId(1);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/reservationservice/reservationByHotelId/1").with(user("Guest").password("password").roles("GUEST")))
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
		UserDetails userDetails = new User("Guest", "test", Arrays.asList(() -> "ROLE_GUEST"));
		String token = jwtUtil.generateToken(userDetails);
		lenient().doReturn(reservationDataModel).when(reservationService).bookReservation(requestReservationDataModel);
		mockMvc.perform(MockMvcRequestBuilders.post("/reservationservice/book").header("Authorization", "Bearer "+token)
				.content(asJsonString(requestReservationDataModel)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).with(user("Guest").password("password").roles("GUEST")))
		.andExpect(status().isOk()).andDo(print());

	}

	@Test
	void whenNullValue_thenReturns400() throws Exception {
		// Request Reservation
				SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
				ReservationRequestModel requestReservationDataModel = new ReservationRequestModel();
				requestReservationDataModel.setHotelId(1);
				requestReservationDataModel.setCheckInDate(myFormat.parse("01-02-2021"));
				requestReservationDataModel.setCheckOutDate(myFormat.parse("02-02-2021"));

				PaymentDetailsDataModel requestPaymentDetails = new PaymentDetailsDataModel();
				requestPaymentDetails.setPaymentId(1);
				requestPaymentDetails.setCardHolder(null);
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
				requestGuestDetailsDataModel.setFirstName(null);
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
				UserDetails userDetails = new User("Guest", "test", Arrays.asList(() -> "ROLE_GUEST"));
				String token = jwtUtil.generateToken(userDetails);
				lenient().doReturn(reservationDataModel).when(reservationService).bookReservation(requestReservationDataModel);
				mockMvc.perform(MockMvcRequestBuilders.post("/reservationservice/book").header("Authorization", "Bearer "+token)
						.content(asJsonString(requestReservationDataModel)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON).with(user("Guest").password("password").roles("GUEST")))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	void cancelReservation_ShouldCancelReservation() throws Exception {

		ReservationDataModel reservationDataModel = getMockedReservation("CancelReservation").get(0);
		given(reservationService.cancelReservation(1)).willReturn(reservationDataModel);

		mockMvc.perform(MockMvcRequestBuilders.delete("/reservationservice/cancelreservation/1")
				.with(user("Guest").password("password").roles("GUEST"))).andExpect(status().isOk())
				.andDo(print()).andExpect(jsonPath("$.reservationLineDetails", hasSize(1)));

		verify(reservationService, times(1)).cancelReservation(1);
	}

	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<ReservationDataModel> getMockedReservation(String methodType) throws ParseException {

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

		switch (methodType) {
		case "getById":
			return Arrays.asList(reservationDataModel);
		case "getByHotelId":
			return Arrays.asList(reservationDataModel, reservationDataModel1);
		case "BookReservation":
			return Arrays.asList(reservationDataModel);
		case "CancelReservation":
			reservationDataModel.setState("CANCELLED");
			return Arrays.asList(reservationDataModel);
		default:
			return Arrays.asList(reservationDataModel, reservationDataModel1); // get All Reservation
		}
	}
}
