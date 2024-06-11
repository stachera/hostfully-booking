package com.hostfully.booking.application.booking;

import com.hostfully.booking.base.TestBase;
import com.hostfully.booking.model.Property;
import com.hostfully.booking.model.Reservation;
import com.hostfully.booking.record.BlockRecord;
import com.hostfully.booking.record.UpdateBlockRecord;
import com.hostfully.booking.service.PropertyService;
import com.hostfully.booking.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.function.Supplier;

import static com.hostfully.booking.base.FixtureHelper.*;
import static com.hostfully.booking.model.ReservationType.BLOCK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BlockFacadeTest extends TestBase {

    @Test
    @DisplayName("Create block")
    public void createBlock() {
        BlockRecord blockRecord = createBlockRecord();
        Property property = createProperty();
        Reservation reservation = createReservation();
        when(reservationService.checkReservationLockedAndExecuteSupplier(eq(blockRecord.propertyId()), eq(blockRecord.checkIn()), eq(blockRecord.checkOut()), supplierCaptor.capture())).thenReturn(reservation);
        when(propertyService.findById(blockRecord.propertyId())).thenReturn(Optional.of(property));

        blockFacade.create(blockRecord);
        supplierCaptor.getValue().get();

        verify(reservationService).save(reservationCaptor.capture());

        Reservation result = reservationCaptor.getValue();
        assertEquals(BLOCK, result.getType());
        assertEquals(property.getId(), result.getProperty().getId());
        assertEquals(blockRecord.checkIn(), result.getDateStart());
        assertEquals(blockRecord.checkOut(), result.getDateEnd());
    }

    @Test
    @DisplayName("Create block with null block record")
    public void createBlockWithNullBlockRecord() {
        assertThrows(IllegalArgumentException.class, () -> blockFacade.create(null));
    }

    @Test
    @DisplayName("Update block")
    public void updateBlock() {
        UpdateBlockRecord updateBlockRecord = createUpdateBlockRecord();
        Reservation reservation = createReservation();

        when(reservationService.findById(reservation.getId())).thenReturn(reservation);
        when(reservationService.checkReservationLockedAndExecuteSupplier(eq(reservation.getProperty().getId()), eq(updateBlockRecord.checkIn()), eq(updateBlockRecord.checkOut()), supplierCaptor.capture())).thenReturn(reservation);

        blockFacade.update(reservation.getId(), updateBlockRecord);
        supplierCaptor.getValue().get();

        verify(reservationService).save(reservationCaptor.capture());

        Reservation result = reservationCaptor.getValue();
        assertEquals(updateBlockRecord.checkIn(), result.getDateStart());
        assertEquals(updateBlockRecord.checkOut(), result.getDateEnd());
    }

    @Test
    @DisplayName("Delete block")
    public void deleteBlock() {
        Reservation reservation = createReservation();

        when(reservationService.findById(reservation.getId())).thenReturn(reservation);

        blockFacade.delete(reservation.getId());

        verify(reservationService).deleteById(reservation.getId());
    }

    @Mock
    private PropertyService propertyService;
    @Mock
    private ReservationService reservationService;
    @InjectMocks
    private BlockFacade blockFacade;
    @Captor
    private ArgumentCaptor<Supplier<Reservation>> supplierCaptor;
    @Captor
    private ArgumentCaptor<Reservation> reservationCaptor;
}
