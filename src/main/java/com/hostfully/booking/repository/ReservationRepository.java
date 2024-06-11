package com.hostfully.booking.repository;

import com.hostfully.booking.model.Reservation;
import com.hostfully.booking.model.ReservationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByPropertyIdAndDateStartGreaterThanEqualAndDateEndLessThanEqual(Long propertyId, LocalDateTime dateEnd, LocalDateTime dateStart);

    Optional<Reservation> findByTypeAndId(ReservationType reservationType, Long id);
}
