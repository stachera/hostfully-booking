package com.hostfully.booking.model;

import com.hostfully.booking.base.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.hostfully.booking.base.FixtureHelper.createReservation;
import static com.hostfully.booking.base.FixtureHelper.validateNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationTest extends TestBase {

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = createReservation();
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void shouldSetAndGetIdCorrectly() {
        Long id = 1L;
        reservation.setId(id);
        assertEquals(id, reservation.getId());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null DateStart")
    void shouldHaveANotNullViolationWhenSettingNullDateStart() {
        reservation.setDateStart(null);
        validateNotNull(reservation, "dateStart");
    }


    @Test
    @DisplayName("Should set and get dateStart correctly")
    void shouldSetAndGetDateStartCorrectly() {
        LocalDateTime dateStart = LocalDateTime.now();
        reservation.setDateStart(dateStart);
        assertEquals(dateStart, reservation.getDateStart());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null dateEnd")
    void shouldHaveANotNullViolationWhenSettingNullDateEnd() {
        reservation.setDateEnd(null);
        validateNotNull(reservation, "dateEnd");
    }

    @Test
    @DisplayName("Should set and get dateEnd correctly")
    void shouldSetAndGetDateEndCorrectly() {
        LocalDateTime dateEnd = LocalDateTime.now();
        reservation.setDateEnd(dateEnd);
        assertEquals(dateEnd, reservation.getDateEnd());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null type")
    void shouldHaveANotNullViolationWhenSettingNullType() {
        reservation.setType(null);
        validateNotNull(reservation, "type");
    }

    @Test
    @DisplayName("Should set and get type correctly")
    void shouldSetAndGetTypeCorrectly() {
        ReservationType type = ReservationType.BOOKING;
        reservation.setType(type);
        assertEquals(type, reservation.getType());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null Property")
    void shouldHaveANotNullViolationWhenSettingNullProperty() {
        reservation.setProperty(null);
        validateNotNull(reservation, "property");
    }

    @Test
    @DisplayName("Should set and get property correctly")
    void shouldSetAndGetPropertyCorrectly() {
        Property property = new Property();
        reservation.setProperty(property);
        assertEquals(property, reservation.getProperty());
    }
}
