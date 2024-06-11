package com.hostfully.booking.application.booking;

import com.hostfully.booking.base.TestBase;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.function.Supplier;

import static com.hostfully.booking.base.FixtureHelper.*;
import static com.hostfully.booking.model.BookingStatus.CANCELLED;
import static com.hostfully.booking.model.BookingStatus.REQUESTED;
import static com.hostfully.booking.model.ReservationType.BOOKING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingFacadeTest extends TestBase {

    @Test
    @DisplayName("Create booking")
    public void createBookingWithValidData() {
        when(guestService.exists(record.guestId())).thenReturn(true);
        when(reservationService.checkReservationLockedAndExecuteSupplier(eq(record.propertyId()), eq(record.checkIn()), eq(record.checkOut()), supplierCaptor.capture())).thenReturn(booking);
        when(propertyService.findById(record.propertyId())).thenReturn(Optional.of(property));
        when(reservationService.save(any())).thenReturn(reservation);
        when(guestService.findById(record.guestId())).thenReturn(Optional.of(guest));

        bookingFacade.create(record);

        supplierCaptor.getValue().get();

        verify(reservationService).save(reservationCaptor.capture());
        Reservation reservationResult = reservationCaptor.getValue();
        assertEquals(BOOKING, reservationResult.getType());
        assertEquals(record.checkIn(), reservationResult.getDateStart());
        assertEquals(record.checkOut(), reservationResult.getDateEnd());
        assertEquals(property, reservationResult.getProperty());

        verify(bookingService).save(bookingCaptor.capture());
        Booking bookingResult = bookingCaptor.getValue();
        assertEquals(record.checkIn(), bookingResult.getCheckIn());
        assertEquals(record.checkOut(), bookingResult.getCheckOut());
        assertEquals(record.numberOfGuests(), bookingResult.getNumberOfGuests());
        assertEquals(REQUESTED, bookingResult.getStatus());
        assertEquals(property, bookingResult.getProperty());
        assertEquals(reservation, bookingResult.getReservation());
        assertEquals(guest, bookingResult.getGuest());
    }

    @Test
    @DisplayName("Update booking")
    public void updateBooking() {
        when(bookingService.findById(id)).thenReturn(booking);
        when(reservationService.checkReservationLockedAndExecuteSupplier(eq(booking.getProperty().getId()), eq(updateRecord.checkIn()), eq(updateRecord.checkOut()), supplierCaptor.capture())).thenReturn(booking);
        when(reservationService.save(any())).thenReturn(reservation);

        bookingFacade.update(id, updateRecord);

        supplierCaptor.getValue().get();

        verify(reservationService).save(reservationCaptor.capture());
        Reservation reservationResult = reservationCaptor.getValue();
        assertEquals(updateRecord.checkIn(), reservationResult.getDateStart());
        assertEquals(updateRecord.checkOut(), reservationResult.getDateEnd());

        verify(bookingService).save(bookingCaptor.capture());
        Booking bookingResult = bookingCaptor.getValue();
        assertEquals(updateRecord.checkIn(), bookingResult.getCheckIn());
        assertEquals(updateRecord.checkOut(), bookingResult.getCheckOut());
        assertEquals(updateRecord.numberOfGuests(), bookingResult.getNumberOfGuests());
    }

    @Test
    @DisplayName("Cancel booking")
    public void cancel() {
        when(bookingService.findById(id)).thenReturn(booking);
        Long reservationId = booking.getReservation().getId();

        bookingFacade.cancel(id);

        verify(reservationService).deleteById(reservationId);
        verify(bookingService).save(bookingCaptor.capture());
        Booking bookingResult = bookingCaptor.getValue();
        assertEquals(CANCELLED, bookingResult.getStatus());
        assertNull(bookingResult.getReservation());
    }

    @Test
    @DisplayName("Delete booking")
    public void delete() {
        booking.setStatus(CANCELLED);
        booking.setReservation(null);
        when(bookingService.findById(id)).thenReturn(booking);

        bookingFacade.delete(id);

        verify(bookingService).deleteById(id);
    }

    @Test
    @DisplayName("Rebook cancelled booking")
    public void rebookCancelled() {
        booking.setStatus(CANCELLED);
        when(bookingService.findById(id)).thenReturn(booking);
        when(reservationService.checkReservationLockedAndExecuteSupplier(eq(booking.getProperty().getId()), eq(rebookRecord.checkIn()), eq(rebookRecord.checkOut()), supplierCaptor.capture())).thenReturn(booking);
        when(reservationService.save(any())).thenReturn(reservation);

        bookingFacade.rebookCancelled(id, rebookRecord);

        supplierCaptor.getValue().get();

        verify(reservationService).save(reservationCaptor.capture());
        Reservation reservationResult = reservationCaptor.getValue();
        assertEquals(BOOKING, reservationResult.getType());
        assertEquals(rebookRecord.checkIn(), reservationResult.getDateStart());
        assertEquals(rebookRecord.checkOut(), reservationResult.getDateEnd());
        assertEquals(booking.getProperty(), reservationResult.getProperty());

        verify(bookingService).save(bookingCaptor.capture());
        Booking bookingResult = bookingCaptor.getValue();
        assertEquals(rebookRecord.checkIn(), bookingResult.getCheckIn());
        assertEquals(rebookRecord.checkOut(), bookingResult.getCheckOut());
        assertEquals(rebookRecord.numberOfGuests(), bookingResult.getNumberOfGuests());
        assertEquals(REQUESTED, bookingResult.getStatus());
    }

    @BeforeEach
    public void setUp() {
        record = createBookingRecord();
        updateRecord = createUpdateBookingRecord();
        rebookRecord = createRebookBookingRecord();
        booking = createBooking();
        property = createProperty();
        reservation = createReservation();
        guest = createGuest();
    }

    private Long id = 1L;
    private BookingRecord record;
    private UpdateBookingRecord updateRecord;
    private RebookBookingRecord rebookRecord;
    private Booking booking;
    private Property property;
    private Reservation reservation;
    private Guest guest;
    @Mock
    private GuestService guestService;
    @Mock
    private PropertyService propertyService;
    @Mock
    private ReservationService reservationService;
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingFacade bookingFacade;
    @Captor
    private ArgumentCaptor<Supplier<Booking>> supplierCaptor;
    @Captor
    private ArgumentCaptor<Booking> bookingCaptor;
    @Captor
    private ArgumentCaptor<Reservation> reservationCaptor;
}
