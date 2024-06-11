package com.hostfully.booking.base;

import com.hostfully.booking.model.*;
import com.hostfully.booking.record.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static com.hostfully.booking.model.BookingStatus.REQUESTED;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FixtureHelper {

    private static Validator validator;

    public static Long generateRandomLong() {
        return (long) (Math.random() * 1000);
    }

    public static Owner createOwer() {
        return Owner.builder()
                .id(generateRandomLong())
                .name("Robby Derren").build();
    }

    public static Property createProperty() {
        return Property.builder()
                .id(generateRandomLong())
                .name("My beach house")
                .numberOfGuests(9999)
                .description("A beautiful beach house")
                .owner(createOwer())
                .build();
    }

    public static Reservation createReservation() {
        return Reservation.builder()
                .id(generateRandomLong())
                .dateStart(LocalDateTime.now())
                .dateEnd(LocalDateTime.now().plusDays(1))
                .type(ReservationType.BOOKING)
                .property(createProperty())
                .build();
    }

    public static Guest createGuest() {
        return Guest.builder()
                .id(generateRandomLong())
                .name("Katherine Carmen").build();
    }

    public static Booking createBooking() {
        Reservation reservation = createReservation();
        return Booking.builder()
                .id(generateRandomLong())
                .checkIn(LocalDateTime.now().plusHours(1))
                .checkOut(LocalDateTime.now().plusDays(1))
                .numberOfGuests(2)
                .status(REQUESTED)
                .reservation(reservation)
                .property(reservation.getProperty())
                .guest(createGuest())
                .build();
    }

    public static <T> Set<ConstraintViolation<T>> validate(T entity) {
        return getValidator().validate(entity);
    }

    public static <T> void validateConstraint(T entity, String property, String constraint) {
        Set<ConstraintViolation<T>> violations = validate(entity);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(constraint) && v.getPropertyPath().toString().equals(property)));
    }

    public static <T> void validateNotNull(T entity, String property) {
        validateConstraint(entity, property, "must not be null");
    }

    public static <T> void validateMin(T entity, String property, int min) {
        validateConstraint(entity, property, "must be greater than or equal to " + min);
    }

    private static Validator getValidator() {
        if (validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        return validator;
    }

    public static BlockRecord createBlockRecord() {
        return new BlockRecord(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(5), generateRandomLong());
    }

    public static UpdateBlockRecord createUpdateBlockRecord() {
        return new UpdateBlockRecord(LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(6));
    }

    public static BookingRecord createBookingRecord() {
        return new BookingRecord(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(5), 2, generateRandomLong(), generateRandomLong());
    }

    public static UpdateBookingRecord createUpdateBookingRecord() {
        return new UpdateBookingRecord(LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(6), 3);
    }

    public static RebookBookingRecord createRebookBookingRecord() {
        return new RebookBookingRecord(LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(6), 1);
    }
}
