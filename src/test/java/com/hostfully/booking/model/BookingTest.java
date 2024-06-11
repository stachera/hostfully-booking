package com.hostfully.booking.model;

import com.hostfully.booking.base.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.hostfully.booking.base.FixtureHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingTest extends TestBase {

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = createBooking();
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void shouldSetAndGetIdCorrectly() {
        Long id = 1L;
        booking.setId(id);
        assertEquals(id, booking.getId());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null CheckIn")
    void shouldHaveANotNullViolationWhenSettingNullCheckIn() {
        booking.setCheckIn(null);
        validateNotNull(booking, "checkIn");
    }

    @Test
    @DisplayName("Should set and get checkIn correctly")
    void shouldSetAndGetCheckInCorrectly() {
        LocalDateTime checkIn = LocalDateTime.now();
        booking.setCheckIn(checkIn);
        assertEquals(checkIn, booking.getCheckIn());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null CheckOut")
    void shouldHaveANotNullViolationWhenSettingNullCheckOut() {
        booking.setCheckOut(null);
        validateNotNull(booking, "checkOut");
    }

    @Test
    @DisplayName("Should set and get checkOut correctly")
    void shouldSetAndGetCheckOutCorrectly() {
        LocalDateTime checkOut = LocalDateTime.now();
        booking.setCheckOut(checkOut);
        assertEquals(checkOut, booking.getCheckOut());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null NumberOfGuests")
    void shouldHaveANotNullViolationWhenSettingNullNumberOfGuests() {
        booking.setNumberOfGuests(null);
        validateNotNull(booking, "numberOfGuests");
    }

    @Test
    @DisplayName("Should have a Min violation when setting numberOfGuests less than 1")
    void shouldHaveAMinViolationWhenSettingNumberOfGuestsLessThanOne() {
        booking.setNumberOfGuests(0);
        validateMin(booking, "numberOfGuests", 1);
    }

    @Test
    @DisplayName("Should set and get numberOfGuests correctly")
    void shouldSetAndGetNumberOfGuestsCorrectly() {
        Integer numberOfGuests = 5;
        booking.setNumberOfGuests(numberOfGuests);
        assertEquals(numberOfGuests, booking.getNumberOfGuests());
    }

    @Test
    @DisplayName("Should set and get status correctly")
    void shouldSetAndGetStatusCorrectly() {
        BookingStatus status = BookingStatus.CONFIRMED;
        booking.setStatus(status);
        assertEquals(status, booking.getStatus());
    }

    @Test
    @DisplayName("Should set and get reservation correctly")
    void shouldSetAndGetReservationCorrectly() {
        Reservation reservation = new Reservation();
        booking.setReservation(reservation);
        assertEquals(reservation, booking.getReservation());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null Guest")
    void shouldHaveANotNullViolationWhenSettingNullGuest() {
        booking.setGuest(null);
        validateNotNull(booking, "guest");
    }

    @Test
    @DisplayName("Should set and get guest correctly")
    void shouldSetAndGetGuestCorrectly() {
        Guest guest = new Guest();
        booking.setGuest(guest);
        assertEquals(guest, booking.getGuest());
    }
}
