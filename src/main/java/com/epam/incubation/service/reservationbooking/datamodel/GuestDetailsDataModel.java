package com.epam.incubation.service.reservationbooking.datamodel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.epam.incubation.service.reservationbooking.entities.GuestDetails;

public class GuestDetailsDataModel {

	private Integer guestId;
	@NotBlank(message = "First Name is mandatory")
	private String firstName;
	private String middleName;
	@NotBlank(message = "Last Name is mandatory")
	private String lastName;
	@NotBlank(message = "house number is mandatory")
	private String houseNumber;
	@NotBlank(message = "street is mandatory")
	private String street;
	@NotBlank(message = "City is mandatory")
	private String city;
	@NotBlank(message = "State is mandatory")
	private String state;
	@NotNull(message = "Zipcode is mandatory")
	private long zipcode;
	@NotBlank(message = "Country is mandatory")
	private String country;
	@Email
	@NotBlank(message = "Email is mandatory")
	private String email;
	@NotNull(message = "Phone Number is mandatory")
	private Long phoneNumber;
	@NotNull(message = "Gender is mandatory")
	private Character gender;

	public GuestDetailsDataModel() {

	}

	public GuestDetailsDataModel(GuestDetails guest) {
		this.guestId = guest.getGuestId();
		this.firstName = guest.getFirstName();
		this.middleName = guest.getMiddleName();
		this.lastName = guest.getLastName();
		this.houseNumber = guest.getHouseNumber();
		this.street = guest.getStreet();
		this.city = guest.getCity();
		this.state = guest.getState();
		this.zipcode = guest.getZipcode();
		this.country = guest.getCity();
		this.email = guest.getEmail();
		this.phoneNumber = guest.getPhoneNumber();
		this.gender = guest.getGender();
	}

	public GuestDetailsDataModel(GuestDetailsRequestModel request) {
		this.firstName = request.getFirstName();
		this.middleName = request.getMiddleName();
		this.lastName = request.getLastName();
		this.houseNumber = request.getHouseNumber();
		this.street = request.getStreet();
		this.city = request.getCity();
		this.state = request.getState();
		this.zipcode = request.getZipcode();
		this.country = request.getCity();
		this.email = request.getEmail();
		this.phoneNumber = request.getPhoneNumber();
		this.gender = request.getGender();
	}
	
	public Integer getGuestId() {
		return guestId;
	}

	public void setGuestId(Integer guestId) {
		this.guestId = guestId;
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

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getZipcode() {
		return zipcode;
	}

	public void setZipcode(long zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
