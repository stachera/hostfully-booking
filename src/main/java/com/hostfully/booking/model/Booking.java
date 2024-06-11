package com.hostfully.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.hostfully.booking.model.BookingStatus.*;

@Entity
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Builder
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime checkIn;

    @NotNull
    private LocalDateTime checkOut;

    @NotNull
    @Min(1)
    private Integer numberOfGuests;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    public boolean canBeUpdated() {
        return !this.status.equals(CONFIRMED) && LocalDateTime.now().isBefore(checkIn);
    }

    public void update(LocalDateTime checkIn, LocalDateTime checkOut, Integer numberOfGuests) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests;
    }

    public void rebook(LocalDateTime checkIn, LocalDateTime checkOut, Integer numberOfGuests, Reservation reservation) {
        this.update(checkIn, checkOut, numberOfGuests);
        this.reservation = reservation;
        this.status = REQUESTED;
    }

    public boolean canBeCancelled() {
        return !status.equals(CANCELLED) && (LocalDateTime.now().isBefore(checkIn) || LocalDateTime.now().isAfter(checkOut));
    }

    public void cancel() {
        Assert.isTrue(canBeCancelled(), "The booking cannot be cancelled");
        this.status = CANCELLED;
        this.reservation = null;
    }

    public boolean canBeDeleted() {
        return status.equals(CANCELLED) && this.reservation == null;
    }

    public boolean canBeRebooked() {
        return status.equals(CANCELLED);
    }

    public void confirm() {
        Assert.isTrue(status.equals(REQUESTED), "The booking is not in a requested status");
        this.status = CONFIRMED;
    }

    public Booking(Long id, LocalDateTime checkIn, LocalDateTime checkOut, Integer numberOfGuests, BookingStatus status, Reservation reservation, Property property, Guest guest) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.reservation = reservation;
        this.guest = guest;
        this.property = property;
        Assert.isTrue(checkIn.isBefore(checkOut), "The check-in date must be before the check-out date");
    }
}





