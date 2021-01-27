
package com.epam.incubation.service.reservationbooking.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;

import com.epam.incubation.service.reservationbooking.datamodel.GuestDetailsDataModel;

@Entity
public class GuestDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 101122120L;

	@Id
	@Column(name = "GUEST_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer guestId;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "MIDDEL_NAME")
	private String middleName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "HOUSE_NUMBER")
	private String houseNumber;

	@Column(name = "STREET")
	private String street;

	@Column(name = "CITY")
	private String city;

	@Column(name = "STATE")
	private String state;

	@Column(name = "ZIPCODE")
	private long zipcode;

	@Column(name = "COUNTRY")
	private String country;

	@Email
	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PHONE_NUMBER")
	private Long phoneNumber;

	@Column(name = "GENDER")
	private Character gender;

	@ManyToOne
	private ReservationLineDetails reservationLineDetails;

	public GuestDetails() {
	}

	public GuestDetails(GuestDetailsDataModel guestDetailsDataModel) {
		this.firstName = guestDetailsDataModel.getFirstName();
		this.middleName = guestDetailsDataModel.getMiddleName();
		this.lastName = guestDetailsDataModel.getLastName();
		this.houseNumber = guestDetailsDataModel.getHouseNumber();
		this.street = guestDetailsDataModel.getStreet();
		this.city = guestDetailsDataModel.getCity();
		this.state = guestDetailsDataModel.getState();
		this.zipcode = guestDetailsDataModel.getZipcode();
		this.country = guestDetailsDataModel.getCountry();
		this.email = guestDetailsDataModel.getEmail();
		this.phoneNumber = guestDetailsDataModel.getPhoneNumber();
		this.gender = guestDetailsDataModel.getGender();
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

	public void setReservationLineDetails(ReservationLineDetails reservationLineDetails) {
		this.reservationLineDetails = reservationLineDetails;
	}

}
