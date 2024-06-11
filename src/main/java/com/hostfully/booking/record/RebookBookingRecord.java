package com.hostfully.booking.record;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RebookBookingRecord(
        @NotNull LocalDateTime checkIn,
        @NotNull LocalDateTime checkOut,
        @NotNull Integer numberOfGuests) {
}
