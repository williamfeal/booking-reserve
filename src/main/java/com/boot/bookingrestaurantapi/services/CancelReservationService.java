package com.boot.bookingrestaurantapi.services;

import com.boot.bookingrestaurantapi.exceptions.BookingException;

public interface CancelReservationService {
	
	/**
	 * the deleteReservation
	 * 
	 * description
	 * 
	 * @param locator the locator
	 * @return String
	 * @throws BookingException the BookingException
	 */
	public String deleteReservation(String locator) throws BookingException;

}
