package com.hostfully.booking.model;

import com.hostfully.booking.base.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hostfully.booking.base.FixtureHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class PropertyTest extends TestBase {

    private Property property;

    @BeforeEach
    void setUp() {
        property = createProperty();
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void shouldSetAndGetIdCorrectly() {
        Long id = 1L;
        property.setId(id);
        assertEquals(id, property.getId());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null Name")
    void shouldHaveANotNullViolationWhenSettingNullName() {
        property.setName(null);
        validateNotNull(property, "name");
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void shouldSetAndGetNameCorrectly() {
        String name = "Villa";
        property.setName(name);
        assertEquals(name, property.getName());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null NumberOfGuests")
    void shouldHaveANotNullViolationWhenSettingNullNumberOfGuests() {
        property.setNumberOfGuests(null);
        validateNotNull(property, "numberOfGuests");
    }

    @Test
    @DisplayName("Should have a Min violation when setting numberOfGuests less than 1")
    void shouldHaveAMinViolationWhenSettingNumberOfGuestsLessThanOne() {
        property.setNumberOfGuests(0);
        validateMin(property, "numberOfGuests", 1);
    }

    @Test
    @DisplayName("Should set and get numberOfGuests correctly")
    void shouldSetAndGetNumberOfGuestsCorrectly() {
        Integer numberOfGuests = 5;
        property.setNumberOfGuests(numberOfGuests);
        assertEquals(numberOfGuests, property.getNumberOfGuests());
    }

    @Test
    @DisplayName("Should set and get description correctly")
    void shouldSetAndGetDescriptionCorrectly() {
        String description = "A beautiful villa";
        property.setDescription(description);
        assertEquals(description, property.getDescription());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null Owner")
    void shouldHaveANotNullViolationWhenSettingNullOwner() {
        property.setOwner(null);
        validateNotNull(property, "owner");
    }

    @Test
    @DisplayName("Should set and get owner correctly")
    void shouldSetAndGetOwnerCorrectly() {
        Owner owner = new Owner();
        property.setOwner(owner);
        assertEquals(owner, property.getOwner());
    }

    @Test
    @DisplayName("Should allow booking when requested guests is equal to property capacity")
    void shouldAllowBookingWhenRequestedGuestsIsEqualToPropertyCapacity() {
        property.setNumberOfGuests(5);
        assertTrue(property.canBeBooked(5));
    }

    @Test
    @DisplayName("Should not allow booking when requested guests is more than property capacity")
    void shouldNotAllowBookingWhenRequestedGuestsIsMoreThanPropertyCapacity() {
        property.setNumberOfGuests(5);
        assertFalse(property.canBeBooked(6));
    }
}
