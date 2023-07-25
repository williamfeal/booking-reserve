package com.boot.bookingrestaurantapi.services;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.boot.bookingrestaurantapi.entities.Reservation;
import com.boot.bookingrestaurantapi.exceptions.BookingException;
import com.boot.bookingrestaurantapi.exceptions.InternalServerErrorException;
import com.boot.bookingrestaurantapi.repositories.ReservationRespository;
import com.boot.bookingrestaurantapi.services.impl.CancelReservationServiceImpl;

public class CancelReservationServiceImplTest {
	@InjectMocks
	CancelReservationServiceImpl cancelReservationServiceImpl;
	
	@Mock
	private ReservationRespository reservationRespository;
	
	@Before
	public void init() throws BookingException {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void deleteReservationTest() throws BookingException {
		Mockito.when(reservationRespository.findByLocator(Mockito.anyString())).thenReturn(Optional.of(new Reservation()));
		cancelReservationServiceImpl.deleteReservation("x");
	}
	
	@Test(expected=BookingException.class)
	public void deleteReservationTest2() throws BookingException {
		Mockito.when(reservationRespository.findByLocator(Mockito.anyString())).thenReturn(Optional.empty());
		cancelReservationServiceImpl.deleteReservation("x");
	}
	
	@Test(expected=InternalServerErrorException.class)
	public void deleteReservationTest3() throws BookingException {
		Mockito.when(reservationRespository.deleteByLocator(Mockito.anyString())).thenThrow(new NullPointerException("x"));
		Mockito.when(reservationRespository.findByLocator(Mockito.anyString())).thenReturn(Optional.of(new Reservation()));
		cancelReservationServiceImpl.deleteReservation("x");
	}
	
}
