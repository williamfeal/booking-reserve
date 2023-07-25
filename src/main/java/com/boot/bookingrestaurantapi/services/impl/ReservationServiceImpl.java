package com.boot.bookingrestaurantapi.services.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.bookingrestaurantapi.constants.Constants;
import com.boot.bookingrestaurantapi.entities.Reservation;
import com.boot.bookingrestaurantapi.entities.Restaurant;
import com.boot.bookingrestaurantapi.entities.Turn;
import com.boot.bookingrestaurantapi.exceptions.BookingException;
import com.boot.bookingrestaurantapi.exceptions.InternalServerErrorException;
import com.boot.bookingrestaurantapi.exceptions.NotFountException;
import com.boot.bookingrestaurantapi.jsons.CreateReservationRest;
import com.boot.bookingrestaurantapi.jsons.ReservationRest;
import com.boot.bookingrestaurantapi.repositories.ReservationRespository;
import com.boot.bookingrestaurantapi.repositories.RestaurantRepository;
import com.boot.bookingrestaurantapi.repositories.TurnRepository;
import com.boot.bookingrestaurantapi.services.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private TurnRepository turnRepository;

	@Autowired
	private ReservationRespository reservationRepository;

	public static final ModelMapper modelMapper = new ModelMapper();

	// Inyectar la dependencias atraves del constructor
//	ReservationServiceImpl(RestaurantRepository restaurantRepository){
//		this.restaurantRepository = restaurantRepository;
//	}
	
	public ReservationRest getReservation(Long reservationId) throws BookingException {
		return modelMapper.map(getReservationEntity(reservationId), ReservationRest.class);
	}

	public String createReservation(final CreateReservationRest createReservationRest) throws BookingException {

		final Restaurant restaurantId = restaurantRepository.findById(createReservationRest.getRestaurantId())
				.orElseThrow(() -> new NotFountException("RESTAURANT_NOT_FOUND", "RESTAURANT_NOT_FOUND"));

		final Turn turn = turnRepository.findById(createReservationRest.getTurnId())
				.orElseThrow(() -> new NotFountException("TURN_NOT_FOUND", "TURN_NOT_FOUND"));

		if (reservationRepository.findByTurnAndRestaurantId(turn.getName(), restaurantId.getId()).isPresent()) {
			throw new NotFountException("RESERVATION_ALREADT_EXIST", "RESERVATION_ALREADT_EXIST");
		}

		String locator = generateLocator(restaurantId, createReservationRest);

		final Reservation reservation = setReservation(createReservationRest, restaurantId, turn, locator);

		saveReservation(reservation);
		return locator;
	}

	private Reservation setReservation(final CreateReservationRest createReservationRest, final Restaurant restaurantId,
			final Turn turn, String locator) {
		final Reservation reservation = new Reservation();
		reservation.setLocator(locator);
		reservation.setPerson(createReservationRest.getPerson());
		reservation.setDate(createReservationRest.getDate());
		reservation.setRestaurant(restaurantId);
		reservation.setTurn(turn.getName());
		return reservation;
	}

	private void saveReservation(final Reservation reservation) throws InternalServerErrorException {
		try {
			reservationRepository.save(reservation);
		} catch (final Exception e) {
			LOGGER.error(Constants.INTERNAL_SERVER_ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR, Constants.INTERNAL_SERVER_ERROR);
		}
	}

	private String generateLocator(final Restaurant restaurantId, final CreateReservationRest createReservationRest)
			throws BookingException {
		return restaurantId.getName() + createReservationRest.getTurnId();
	}

	private Reservation getReservationEntity(Long reservationId) throws BookingException {
		return reservationRepository.findById(reservationId)
				.orElseThrow(() -> new NotFountException("SNOT-404-1", "RESERVATION_NOT_FOUND"));
	}
}
