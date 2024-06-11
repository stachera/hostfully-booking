package com.hostfully.booking.application.booking;

import com.hostfully.booking.model.Property;
import com.hostfully.booking.model.Reservation;
import com.hostfully.booking.record.BlockRecord;
import com.hostfully.booking.record.UpdateBlockRecord;
import com.hostfully.booking.service.PropertyService;
import com.hostfully.booking.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.hostfully.booking.model.ReservationType.BLOCK;

@Component
@RequiredArgsConstructor
public class BlockFacade {

    private final PropertyService propertyService;
    private final ReservationService reservationService;

    public Reservation create(BlockRecord block) {
        Assert.notNull(block, "The block request cannot be null");
        return reservationService.checkReservationLockedAndExecuteSupplier(
                block.propertyId(),
                block.checkIn(),
                block.checkOut(),
                () -> internalCreateBlock(block));
    }

    public Reservation update(Long id, UpdateBlockRecord newBlock) {
        Reservation reservation = reservationService.findById(id);

        return reservationService.checkReservationLockedAndExecuteSupplier(
                reservation.getProperty().getId(),
                newBlock.checkIn(),
                newBlock.checkOut(),
                () -> internalUpdateBlock(reservation, newBlock));
    }

    @Transactional
    public void delete(Long id) {
        Reservation reservation = reservationService.findById(id);
        reservationService.deleteById(reservation.getId());
    }

    @Transactional
    public Reservation internalCreateBlock(BlockRecord block) {
        Property property = propertyService.findById(block.propertyId()).orElseThrow();
        return reservationService.save(Reservation.builder()
                .dateStart(block.checkIn())
                .dateEnd(block.checkOut())
                .type(BLOCK)
                .property(property)
                .build());
    }

    @Transactional
    public Reservation internalUpdateBlock(Reservation reservation, UpdateBlockRecord newBlock) {
        reservation.updateDates(newBlock.checkIn(), newBlock.checkOut());
        return reservationService.save(reservation);
    }
}
