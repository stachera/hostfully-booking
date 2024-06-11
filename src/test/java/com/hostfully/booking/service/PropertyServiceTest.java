package com.hostfully.booking.service;

import com.hostfully.booking.base.TestBase;
import com.hostfully.booking.model.Property;
import com.hostfully.booking.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.hostfully.booking.base.FixtureHelper.createProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class PropertyServiceTest extends TestBase {

    @Test
    @DisplayName("Find property by id")
    public void findById() {
        when(repository.findById(1L)).thenReturn(Optional.of(property));
        Optional<Property> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(property, result.get());
    }

    @BeforeEach
    public void setUp() {
        property = createProperty();
    }

    private Property property;
    @Mock
    private PropertyRepository repository;
    @InjectMocks
    private PropertyService service;

}
