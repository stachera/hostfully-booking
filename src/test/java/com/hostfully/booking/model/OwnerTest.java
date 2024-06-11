package com.hostfully.booking.model;

import com.hostfully.booking.base.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hostfully.booking.base.FixtureHelper.createOwer;
import static com.hostfully.booking.base.FixtureHelper.validateNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OwnerTest extends TestBase {

    private Owner owner;

    @BeforeEach
    void setUp() {
        owner = createOwer();
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void shouldSetAndGetIdCorrectly() {
        Long id = 1L;
        owner.setId(id);
        assertEquals(id, owner.getId());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null name")
    void shouldHaveANotNullViolationWhenSettingNullName() {
        owner.setName(null);
        validateNotNull(owner, "name");
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void shouldSetAndGetNameCorrectly() {
        String name = "John Doe";
        owner.setName(name);
        assertEquals(name, owner.getName());
    }
}
