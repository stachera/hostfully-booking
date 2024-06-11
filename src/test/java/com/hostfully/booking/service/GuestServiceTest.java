package com.hostfully.booking.service;

import com.hostfully.booking.base.TestBase;
import com.hostfully.booking.model.Guest;
import com.hostfully.booking.repository.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.hostfully.booking.base.FixtureHelper.createGuest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class GuestServiceTest extends TestBase {

    @Test
    public void exists() {
        when(repository.existsById(guest.getId())).thenReturn(true);
        assertTrue(service.exists(guest.getId()));
    }

    @Test
    public void findById() {
        when(repository.findById(guest.getId())).thenReturn(java.util.Optional.of(guest));
        assertEquals(guest, service.findById(guest.getId()).get());
    }

    @BeforeEach
    void setUp() {
        guest = createGuest();
    }

    private Guest guest;
    @Mock
    private GuestRepository repository;
    @InjectMocks
    private GuestService service;

}
