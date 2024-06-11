package com.hostfully.booking.service;

import com.hostfully.booking.model.Property;
import com.hostfully.booking.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository repository;

    @Transactional(readOnly = true)
    public Optional<Property> findById(Long id) {
        return repository.findById(id);
    }
}
