package com.hostfully.booking.service;

import com.hostfully.booking.base.TestBase;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.hostfully.booking.base.FixtureHelper.createBooking;
import static com.hostfully.booking.model.BookingStatus.CONFIRMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingServiceTest extends TestBase {

    @Test
    void save() {
        when(repository.save(booking)).thenReturn(booking);
        Booking saved = bookingService.save(booking);
        assertEquals(booking, saved);
    }

    @Test
    void findById() {
        when(repository.findById(ID)).thenReturn(Optional.of(booking));
        Booking found = bookingService.findById(ID);
        assertEquals(booking, found);
    }

    @Test
    void deleteById() {
        bookingService.deleteById(ID);
        verify(repository).deleteById(ID);
    }

    @Test
    void confirm() {
        when(repository.findById(ID)).thenReturn(Optional.of(booking));

        bookingService.confirm(ID);

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(repository).save(captor.capture());
        assertEquals(captor.getValue().getStatus(), CONFIRMED);
    }

    @BeforeEach
    void setUp() {
        booking = createBooking();
    }

    private static final Long ID = 1L;
    private Booking booking;
    @Mock
    private BookingRepository repository;
    @InjectMocks
    private BookingService bookingService;

}
