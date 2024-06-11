package com.hostfully.booking.service;

import com.hostfully.booking.concurrency.LockHandler;
import com.hostfully.booking.model.Reservation;
import com.hostfully.booking.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static com.hostfully.booking.model.ReservationType.BLOCK;

@Service
@RequiredArgsConstructor
public class ReservationService {

    public static final String LOCK_KEY = "RESERVATION";
    private final ReservationRepository repository;
    private final LockHandler lockHandler;

    @Transactional(readOnly = true)
    public boolean hasReservation(Long propertyId, LocalDateTime dateStart, LocalDateTime dateEnd) {
        Optional<Reservation> reservation = repository.findByPropertyIdAndDateStartGreaterThanEqualAndDateEndLessThanEqual(propertyId, dateStart, dateEnd);
        return reservation.isPresent();
    }

    @Transactional
    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }

    public <T> T checkReservationLockedAndExecuteSupplier(Long propertyId, LocalDateTime dateStart, LocalDateTime dateEnd, Supplier<T> supplier) {
        return lockHandler.lockAndExecuteSupplier(LOCK_KEY, propertyId, () -> {
            Assert.state(!hasReservation(propertyId, dateStart, dateEnd), "The property is already reserved for the given dates");
            return supplier.get();
        });
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Reservation findBlockById(Long id) {
        return repository.findByTypeAndId(BLOCK, id).orElseThrow();
    }
}
