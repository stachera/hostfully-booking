package com.hostfully.booking.model;

import com.hostfully.booking.base.FixtureHelper;
import com.hostfully.booking.base.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hostfully.booking.base.FixtureHelper.createGuest;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GuestTest extends TestBase {

    private Guest guest;

    @BeforeEach
    void setUp() {
        guest = createGuest();
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void shouldSetAndGetIdCorrectly() {
        Long id = 1L;
        guest.setId(id);
        assertEquals(id, guest.getId());
    }

    @Test
    @DisplayName("Should have a Not Null violation when setting null name")
    void shouldHaveANotNullViolationWhenSettingNullName() {
        guest.setName(null);
        FixtureHelper.validateNotNull(guest, "name");
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void shouldSetAndGetNameCorrectly() {
        String name = "John Doe";
        guest.setName(name);
        assertEquals(name, guest.getName());
    }
}
