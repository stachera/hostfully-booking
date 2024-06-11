package com.hostfully.booking.service;

import com.hostfully.booking.model.Guest;
import com.hostfully.booking.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GuestService {

    private final GuestRepository repository;

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Guest> findById(Long id) {
        return repository.findById(id);
    }
}
