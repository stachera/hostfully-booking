package com.hostfully.booking.application.booking;

import com.hostfully.booking.model.Booking;
import com.hostfully.booking.model.Guest;
import com.hostfully.booking.model.Property;
import com.hostfully.booking.model.Reservation;
import com.hostfully.booking.record.BookingRecord;
import com.hostfully.booking.record.RebookBookingRecord;
import com.hostfully.booking.record.UpdateBookingRecord;
import com.hostfully.booking.service.BookingService;
import com.hostfully.booking.service.GuestService;
import com.hostfully.booking.service.PropertyService;
import com.hostfully.booking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.hostfully.booking.model.BookingStatus.REQUESTED;
import static com.hostfully.booking.model.ReservationType.BOOKING;

@Component
@RequiredArgsConstructor
public class BookingFacade {

    private final GuestService guestService;
    private final PropertyService propertyService;
    private final ReservationService reservationService;
    private final BookingService bookingService;

    public Booking create(BookingRecord booking) {
        Assert.notNull(booking, "Booking cannot be null");
        Assert.isTrue(guestService.exists(booking.guestId()), String.format("The guest '%s' does not exist", booking.guestId()));
        return reservationService.checkReservationLockedAndExecuteSupplier(
                booking.propertyId(),
                booking.checkIn(),
                booking.checkOut(),
                () -> internalCreateReservationAndBooking(booking));
    }

    public Booking update(Long id, UpdateBookingRecord newBooking) {
        Booking booking = bookingService.findById(id);
        Assert.isTrue(booking.canBeUpdated(), "The booking cannot be updated");

        return reservationService.checkReservationLockedAndExecuteSupplier(
                booking.getProperty().getId(),
                newBooking.checkIn(),
                newBooking.checkOut(),
                () -> internalUpdateReservationAndBooking(booking, newBooking));
    }

    @Transactional
    public void cancel(Long id) {
        Booking booking = bookingService.findById(id);
        reservationService.deleteById(booking.getReservation().getId());
        booking.cancel();
        bookingService.save(booking);
    }

    @Transactional
    public void delete(Long id) {
        Booking booking = bookingService.findById(id);
        Assert.isTrue(booking.canBeDeleted(), "The booking cannot be deleted");
        bookingService.deleteById(id);
    }

    public Booking rebookCancelled(Long id, RebookBookingRecord newBooking) {
        Booking booking = bookingService.findById(id);
        Assert.isTrue(booking.canBeRebooked(), "The booking cannot be rebooked");
        return reservationService.checkReservationLockedAndExecuteSupplier(
                booking.getProperty().getId(),
                newBooking.checkIn(),
                newBooking.checkOut(),
                () -> internalRebookReservationAndBooking(booking, newBooking));
    }

    @Transactional
    public Booking internalCreateReservationAndBooking(BookingRecord booking) {
        Property property = propertyService.findById(booking.propertyId()).orElseThrow();
        Assert.isTrue(property.canBeBooked(booking.numberOfGuests()), String.format("The property '%s' cannot be booked", booking.propertyId()));
        Reservation reservation = reservationService.save(Reservation.builder()
                .dateStart(booking.checkIn())
                .dateEnd(booking.checkOut())
                .type(BOOKING)
                .property(property)
                .build());

        Guest guest = guestService.findById(booking.guestId()).orElseThrow();
        return bookingService.save(Booking.builder()
                .checkIn(booking.checkIn())
                .checkOut(booking.checkOut())
                .numberOfGuests(booking.numberOfGuests())
                .status(REQUESTED)
                .property(property)
                .reservation(reservation)
                .guest(guest)
                .build());
    }

    @Transactional
    public Booking internalUpdateReservationAndBooking(Booking booking, UpdateBookingRecord newBooking) {
        Reservation reservation = booking.getReservation();
        Assert.isTrue(reservation.getProperty().canBeBooked(newBooking.numberOfGuests()), String.format("The property '%s' cannot be booked", reservation.getProperty().getId()));

        reservation.updateDates(newBooking.checkIn(), newBooking.checkOut());
        reservationService.save(reservation);

        booking.update(newBooking.checkIn(), newBooking.checkOut(), newBooking.numberOfGuests());
        return bookingService.save(booking);
    }

    @Transactional
    public Booking internalRebookReservationAndBooking(Booking booking, RebookBookingRecord newBooking) {
        Assert.isTrue(booking.getProperty().canBeBooked(newBooking.numberOfGuests()), String.format("The property '%s' cannot be booked", booking.getProperty()));

        Reservation reservation = reservationService.save(Reservation.builder()
                .dateStart(newBooking.checkIn())
                .dateEnd(newBooking.checkOut())
                .type(BOOKING)
                .property(booking.getProperty())
                .build());

        booking.rebook(newBooking.checkIn(), newBooking.checkOut(), newBooking.numberOfGuests(), reservation);
        return bookingService.save(booking);
    }
}
