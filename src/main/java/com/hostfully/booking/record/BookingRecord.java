package com.hostfully.booking.record;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingRecord(
        @NotNull LocalDateTime checkIn,
        @NotNull LocalDateTime checkOut,
        @NotNull Integer numberOfGuests,
        @NotNull Long propertyId,
        @NotNull Long guestId) {
}
