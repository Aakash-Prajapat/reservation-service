package com.epam.incubation.service.reservationbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.incubation.service.reservationbooking.datamodel.GuestDetailsRequestModel;
import com.epam.incubation.service.reservationbooking.datamodel.InventoryDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.PaymentDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationLineDetailsRequestModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationRequestModel;
import com.epam.incubation.service.reservationbooking.entities.GuestDetails;
import com.epam.incubation.service.reservationbooking.entities.InventoryDetails;
import com.epam.incubation.service.reservationbooking.entities.PaymentDetails;
import com.epam.incubation.service.reservationbooking.entities.Reservation;
import com.epam.incubation.service.reservationbooking.entities.ReservationLineDetails;
import com.epam.incubation.service.reservationbooking.exception.RecordNotFoundException;
import com.epam.incubation.service.reservationbooking.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

	@Mock
	private ReservationRepository reservationRepository;

	@InjectMocks
	private ReservationServiceImpl reservationService;

	@Test
	void getReservation_ShouldReturnReservation() throws ParseException {
		
		Reservation reservation = getMockedReservation("getById").get(0);

		given(reservationRepository.findById(1)).willReturn(Optional.of(reservation));

		ReservationDataModel expectedReservation = reservationService.getReservation(1);
		assertEquals(1, expectedReservation.getReservationId());
		assertEquals(2, expectedReservation.getReservationLineDetails().get(0).getInventoriesDetails().size());
		assertEquals(1, expectedReservation.getReservationLineDetails().size());

	}

	@Test
	void getReservation_NotFound() throws Exception {
		given(reservationRepository.findById(1)).willReturn(Optional.ofNullable(null));
		assertThrows(RecordNotFoundException.class, () -> reservationService.getReservation(1));
	}

	@Test
	void getReservationByHotelId_ShouldReturnReservations() throws Exception {
		List<Reservation> reservations = getMockedReservation("getByHotelId");
		given(reservationRepository.getReservationByHotelId(1)).willReturn(reservations);
		List<ReservationDataModel> reservationDataModels = reservationService.getReservationByHotelId(1);
		assertEquals(2, reservationDataModels.size());
		assertEquals(2, reservationDataModels.get(0).getReservationLineDetails().get(0).getInventoriesDetails().size());
		assertEquals(1, reservationDataModels.get(0).getReservationId());

	}

	@Test
	void getReservationByHotelId_notFound() throws Exception {
		assertThrows(RecordNotFoundException.class, () -> reservationService.getReservationByHotelId(1));
	}
	
	@Test
	void bookReservation_ShouldBookedReservationAndReturnReservationIdWithOtherDetails() throws Exception {
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		Reservation reservation1 = getMockedReservation("BookReservation").get(0); 
		// Request Reservation
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

		
		doReturn(reservation1).when(reservationRepository).save(any(Reservation.class));
		ReservationDataModel savedReservation = reservationService.bookReservation(requestReservationDataModel);
		assertNotNull(savedReservation);
	}
	
	private List<Reservation> getMockedReservation(String methodType) throws ParseException{
		
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
		//1. Reservation
		Reservation reservation1 = new Reservation();
		reservation1.setReservationId(1);
		reservation1.setGuestId(1);
		reservation1.setHotelId(1);
		reservation1.setCheckInDate(myFormat.parse("01-02-2021"));
		reservation1.setCheckOutDate(myFormat.parse("02-02-2021"));

		PaymentDetails paymentDetails1 = new PaymentDetails();
		paymentDetails1.setGuestId(1);
		paymentDetails1.setCardHolder("Aakash Prajapat");
		paymentDetails1.setCardType("VISA");
		paymentDetails1.setCreditCardNumber(1212121212L);
		paymentDetails1.setCvv(123);
		paymentDetails1.setExpiryDate(myFormat.parse("01-02-2022"));

		reservation1.setPaymentsDetails(paymentDetails1);
		reservation1.setLastUpdateDate(new Date());
		reservation1.setCreateDate(new Date());
		reservation1.setState("BOOKED");
		reservation1.setTotalAmount(200.0);
		ReservationLineDetails reservationLineDetails1 = new ReservationLineDetails();
		reservationLineDetails1.setReservationLineId(1);
		reservationLineDetails1.setCheckInDate(myFormat.parse("01-02-2021"));
		reservationLineDetails1.setCheckInDate(myFormat.parse("02-02-2021"));
		reservationLineDetails1.setAmountPerRoom(200.0);
		reservationLineDetails1.setRoomId(1);
		GuestDetails guestDetails1 = new GuestDetails();
		guestDetails1.setFirstName("Aakash");
		guestDetails1.setMiddleName("Kumar");
		guestDetails1.setLastName("Prajapat");
		guestDetails1.setHouseNumber("1");
		guestDetails1.setStreet("Nagar Road");
		guestDetails1.setCity("Indore");
		guestDetails1.setState("Madhya Pradesh");
		guestDetails1.setZipcode(452006);
		guestDetails1.setCountry("India");
		guestDetails1.setEmail("abc@xyz.com");
		guestDetails1.setPhoneNumber(44444444L);
		guestDetails1.setGender('M');
		reservationLineDetails1.setGuestDetails(Arrays.asList(guestDetails1));
		InventoryDetails inventoryDetails1 = new InventoryDetails();
		inventoryDetails1.setReservationInventoryId(1);
		inventoryDetails1.setInventoryId(101);
		inventoryDetails1.setAmoutPerInventory(100.0);
		inventoryDetails1.setRoomId(1);
		inventoryDetails1.setStayDate(myFormat.parse("01-02-2021"));

		InventoryDetails inventoryDetails2 = new InventoryDetails();
		inventoryDetails1.setReservationInventoryId(2);
		inventoryDetails2.setInventoryId(102);
		inventoryDetails2.setAmoutPerInventory(100.0);
		inventoryDetails2.setRoomId(1);
		inventoryDetails2.setStayDate(myFormat.parse("02-02-2021"));

		reservationLineDetails1.setInventoriesDetails(Arrays.asList(inventoryDetails1, inventoryDetails2));
		reservation1.setReservationLineDetails(Arrays.asList(reservationLineDetails1));
		
		//2. Reservation
		Reservation reservation2 = new Reservation();
		reservation2.setReservationId(2);
		reservation2.setGuestId(1);
		reservation2.setHotelId(1);
		reservation2.setCheckInDate(myFormat.parse("06-02-2021"));
		reservation2.setCheckOutDate(myFormat.parse("07-02-2021"));

		PaymentDetails paymentDetails2 = new PaymentDetails();
		paymentDetails2.setGuestId(2);
		paymentDetails2.setCardHolder("Ankit Sonparote");
		paymentDetails2.setCardType("VISA");
		paymentDetails2.setCreditCardNumber(1212121212L);
		paymentDetails2.setCvv(123);
		paymentDetails2.setExpiryDate(myFormat.parse("01-02-2021"));

		reservation2.setPaymentsDetails(paymentDetails2);
		reservation2.setLastUpdateDate(new Date());
		reservation2.setCreateDate(new Date());
		reservation2.setState("BOOKED");
		reservation2.setTotalAmount(200.0);
		ReservationLineDetails reservationLineDetails2 = new ReservationLineDetails();
		reservationLineDetails2.setReservationLineId(2);
		reservationLineDetails2.setCheckInDate(myFormat.parse("06-02-2021"));
		reservationLineDetails2.setCheckInDate(myFormat.parse("07-02-2021"));
		reservationLineDetails2.setAmountPerRoom(100.0);
		reservationLineDetails2.setRoomId(2);
		GuestDetails guestDetails2 = new GuestDetails();
		guestDetails2.setFirstName("Aakash");
		guestDetails2.setMiddleName("Kumar");
		guestDetails2.setLastName("Prajapat");
		guestDetails2.setHouseNumber("1");
		guestDetails2.setStreet("Nagar Road");
		guestDetails2.setCity("Indore");
		guestDetails2.setState("Madhya Pradesh");
		guestDetails2.setZipcode(452006);
		guestDetails2.setCountry("India");
		guestDetails2.setEmail("abc@xyz.com");
		guestDetails2.setPhoneNumber(1212121212L);
		guestDetails2.setGender('M');
		reservationLineDetails2.setGuestDetails(Arrays.asList(guestDetails2));
		InventoryDetails inventoryDetails3 = new InventoryDetails();
		inventoryDetails3.setReservationInventoryId(3);
		inventoryDetails3.setInventoryId(103);
		inventoryDetails3.setAmoutPerInventory(100.0);
		inventoryDetails3.setRoomId(2);
		inventoryDetails3.setStayDate(myFormat.parse("06-02-2021"));

		InventoryDetails inventoryDetails4 = new InventoryDetails();
		inventoryDetails4.setReservationInventoryId(4);
		inventoryDetails4.setInventoryId(104);
		inventoryDetails4.setAmoutPerInventory(100.0);
		inventoryDetails4.setRoomId(2);
		inventoryDetails4.setStayDate(myFormat.parse("07-02-2021"));

		reservationLineDetails2.setInventoriesDetails(Arrays.asList(inventoryDetails3, inventoryDetails4));
		reservation2.setReservationLineDetails(Arrays.asList(reservationLineDetails2));
		
		switch(methodType) {
			case "getById":
				return Arrays.asList(reservation1);
			case "getByHotelId":
				return Arrays.asList(reservation1, reservation2);
			case "BookReservation":
				return Arrays.asList(reservation1);
			case "CancelReservation":
				return Arrays.asList(reservation1);
			default:
				return Arrays.asList(reservation1, reservation2); //get All Reservation
		}
	}

}
