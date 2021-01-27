package com.epam.incubation.service.reservationbooking.datamodel;

import com.epam.incubation.service.reservationbooking.entities.GuestDetails;

public class UserGuestDetails {
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private Long phoneNumber;
	private Character gender;

	public UserGuestDetails() {

	}

	public UserGuestDetails(GuestDetails guestDetails) {
		this.firstName = guestDetails.getFirstName();
		this.middleName = guestDetails.getMiddleName();
		this.lastName = guestDetails.getLastName();
		this.email = guestDetails.getEmail();
		this.phoneNumber = guestDetails.getPhoneNumber();
		this.gender = guestDetails.getGender();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}
}
