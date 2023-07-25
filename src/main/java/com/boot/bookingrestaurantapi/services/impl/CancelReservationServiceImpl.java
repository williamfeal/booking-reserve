package com.boot.bookingrestaurantapi.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.bookingrestaurantapi.constants.Constants;
import com.boot.bookingrestaurantapi.exceptions.BookingException;
import com.boot.bookingrestaurantapi.exceptions.InternalServerErrorException;
import com.boot.bookingrestaurantapi.exceptions.NotFountException;
import com.boot.bookingrestaurantapi.repositories.ReservationRespository;
import com.boot.bookingrestaurantapi.services.CancelReservationService;

@Service
public class CancelReservationServiceImpl implements CancelReservationService {

	

	private static final Logger LOGGER = LoggerFactory.getLogger(CancelReservationServiceImpl.class);

	@Autowired
	private ReservationRespository reservationRespository;

	/**
	 * the deleteReservation
	 * 
	 * description
	 * 
	 * @param locator the locator
	 * @return String
	 * @throws BookingException the BookingException
	 */
	public String deleteReservation(String locator) throws BookingException {

		// busqueda de identificador para cancelar la reserva
		reservationRespository.findByLocator(locator)
				.orElseThrow(() -> new NotFountException("LOCATOR_NOT_FOUND", "LOCATOR_NOT_FOUND"));

		deleteReservationTry(locator);

		return Constants.LOCATOR_DELETED;
	}

	private void deleteReservationTry(String locator) throws InternalServerErrorException {
		try {
			reservationRespository.deleteByLocator(locator);
		} catch (Exception e) {
			LOGGER.error("INTERNAL_SERVER_ERROR", e);
			throw new InternalServerErrorException("INTERNAL_SERVER_ERROR", "INTERNAL_SERVER_ERROR");
		}
	}

}
