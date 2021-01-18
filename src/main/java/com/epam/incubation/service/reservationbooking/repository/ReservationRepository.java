package com.epam.incubation.service.reservationbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epam.incubation.service.reservationbooking.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	public List<Reservation> getReservationByHotelId(Integer id);
}
