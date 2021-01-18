package com.epam.incubation.service.reservationbooking.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.epam.incubation.service.reservationbooking.datamodel.GuestDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.InventoryDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.PaymentDetailsDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationDataModel;
import com.epam.incubation.service.reservationbooking.datamodel.ReservationLineDetailsDataModel;
import com.epam.incubation.service.reservationbooking.entities.Reservation;

@DataJpaTest
class ReservationRepositoryTest {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private TestEntityManager entityManager;

	Reservation savedReservation;
	
	@Test
	void findById_ShouldReturnReservation() throws ParseException {
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
		ReservationDataModel reservationDataModel = new ReservationDataModel();
		reservationDataModel.setReservationId(1);
		reservationDataModel.setGuestId(1);
		reservationDataModel.setHotelId(1);
		reservationDataModel.setCheckInDate(myFormat.parse("01-02-2021"));
		reservationDataModel.setCheckOutDate(myFormat.parse("04-02-2021"));

		PaymentDetailsDataModel paymentDetails = new PaymentDetailsDataModel();
		paymentDetails.setPaymentId(1);
		paymentDetails.setGuestId(1);
		paymentDetails.setCardHolder("Aakash Prajapat");
		paymentDetails.setCardType("VISA");
		paymentDetails.setCreditCardNumber(1212121212L);
		paymentDetails.setCvv(123);
		paymentDetails.setExpiryDate(myFormat.parse("01-02-2021"));

		reservationDataModel.setPaymentsDetails(paymentDetails);
		reservationDataModel.setLastUpdateDate(new Date());
		reservationDataModel.setCreateDate(new Date());
		reservationDataModel.setState("BOOKED");
		reservationDataModel.setTotalAmount(100.0);
		ReservationLineDetailsDataModel reservationLineDetailsDataModel = new ReservationLineDetailsDataModel();
		reservationLineDetailsDataModel.setLineId(1);
		reservationLineDetailsDataModel.setReservationId(1);
		reservationLineDetailsDataModel.setCheckInDate(myFormat.parse("01-02-2021"));
		reservationLineDetailsDataModel.setCheckInDate(myFormat.parse("02-02-2021"));
		reservationLineDetailsDataModel.setAmountPerRoom(100.0);
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
		guestDetailsDataModel.setEmail("abc@xyz.com");
		guestDetailsDataModel.setPhoneNumber(1212121212L);
		guestDetailsDataModel.setGender('M');
		reservationLineDetailsDataModel.setGuestDetails(Arrays.asList(guestDetailsDataModel));
		InventoryDetailsDataModel inventoryDetailsDataModel = new InventoryDetailsDataModel();
		inventoryDetailsDataModel.setInventoryId(1);
		inventoryDetailsDataModel.setAmoutPerInventory(100.0);
		inventoryDetailsDataModel.setRoomId(1);
		inventoryDetailsDataModel.setStayDate(myFormat.parse("01-02-2021"));

		InventoryDetailsDataModel inventoryDetailsDataModel1 = new InventoryDetailsDataModel();
		inventoryDetailsDataModel1.setInventoryId(2);
		inventoryDetailsDataModel1.setAmoutPerInventory(100.0);
		inventoryDetailsDataModel1.setRoomId(1);
		inventoryDetailsDataModel1.setStayDate(myFormat.parse("02-02-2021"));

		reservationLineDetailsDataModel
				.setInventoriesDetails(Arrays.asList(inventoryDetailsDataModel, inventoryDetailsDataModel1));
		reservationDataModel.setReservationLineDetails(Arrays.asList(reservationLineDetailsDataModel));
		savedReservation = new Reservation(reservationDataModel);
		entityManager.persistAndFlush(savedReservation);
		Optional<Reservation> reservationFound = reservationRepository.findById(1);
		assertEquals(savedReservation.getReservationId(), reservationFound.get().getReservationId());
		assertEquals(savedReservation.getReservationLineDetails().size(), reservationFound.get().getReservationLineDetails().size());
		assertEquals(savedReservation.getReservationLineDetails().get(0).getInventoriesDetails().size(), reservationFound.get().getReservationLineDetails().get(0).getInventoriesDetails().size());
	}
	
	@Test
	void findById_ShouldThrowException() {
		//given(reservationRepository.findById(10)).willReturn(reservation);
		Optional<Reservation> reservationFound = reservationRepository.findById(1);
		assertFalse(reservationFound.isPresent());
	}
}
