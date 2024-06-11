package com.hostfully.booking.service;

import com.hostfully.booking.base.TestBase;
import com.hostfully.booking.concurrency.LockHandler;
import com.hostfully.booking.model.Reservation;
import com.hostfully.booking.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static com.hostfully.booking.base.FixtureHelper.createReservation;
import static com.hostfully.booking.model.ReservationType.BLOCK;
import static com.hostfully.booking.service.ReservationService.LOCK_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservationServiceTest extends TestBase {

    @Test
    @DisplayName("Has reservation")
    public void hasReservation() {
        when(repository.findByPropertyIdAndDateStartGreaterThanEqualAndDateEndLessThanEqual(ID, dateStart, dateEnd)).thenReturn(Optional.of(reservation));

        boolean result = service.hasReservation(ID, dateStart, dateEnd);

        assertTrue(result);
    }

    @Test
    @DisplayName("Save reservation")
    public void save() {
        when(repository.save(reservation)).thenReturn(reservation);

        Reservation result = service.save(reservation);

        assertEquals(reservation, result);
    }

    @Test
    @DisplayName("Check reservation locked and execute supplier")
    public void checkReservationLockedAndExecuteSupplier() {
        when(lockHandler.lockAndExecuteSupplier(eq(LOCK_KEY), eq(ID), supplierCaptor.capture())).thenReturn("bar");
        when(repository.findByPropertyIdAndDateStartGreaterThanEqualAndDateEndLessThanEqual(ID, dateStart, dateEnd)).thenReturn(Optional.empty());

        String result = service.checkReservationLockedAndExecuteSupplier(ID, dateStart, dateEnd, () -> "foo");
        assertEquals("bar", result);
        assertEquals("foo", supplierCaptor.getValue().get());
    }

    @Test
    @DisplayName("Delete reservation by id")
    public void deleteById() {
        service.deleteById(ID);

        verify(repository).deleteById(ID);
    }

    @Test
    @DisplayName("Find reservation by id")
    public void findById() {
        when(repository.findById(ID)).thenReturn(Optional.of(reservation));

        Reservation result = service.findById(ID);

        assertEquals(reservation, result);
    }

    @Test
    @DisplayName("Find block by id")
    public void findBlockById() {
        when(repository.findByTypeAndId(BLOCK, ID)).thenReturn(Optional.of(reservation));

        Reservation result = service.findBlockById(ID);

        assertEquals(reservation, result);
    }

    @BeforeEach
    public void setUp() {
        reservation = createReservation();
    }

    public static final Long ID = 1L;
    LocalDateTime dateStart = LocalDateTime.now();
    LocalDateTime dateEnd = LocalDateTime.now().plusDays(1);
    private Reservation reservation;
    @Captor
    private ArgumentCaptor<Supplier<String>> supplierCaptor;
    @Mock
    private ReservationRepository repository;
    @Mock
    private LockHandler lockHandler;
    @InjectMocks
    private ReservationService service;


}
