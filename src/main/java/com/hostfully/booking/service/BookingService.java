package com.hostfully.booking.service;

import com.hostfully.booking.model.Booking;
import com.hostfully.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository repository;

    @Transactional
    public Booking save(Booking booking) {
        return repository.save(booking);
    }

    @Transactional(readOnly = true)
    public Booking findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Booking not found"));
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void confirm(Long id) {
        Booking booking = repository.findById(id).orElseThrow();
        booking.confirm();
        repository.save(booking);
    }
}
