package com.hostfully.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostfully.booking.application.booking.BookingFacade;
import com.hostfully.booking.base.TestBase;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.record.BookingRecord;
import com.hostfully.booking.record.RebookBookingRecord;
import com.hostfully.booking.record.UpdateBookingRecord;
import com.hostfully.booking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.hostfully.booking.base.FixtureHelper.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
class BookingControllerTest extends TestBase {

    public static final String URL_TEMPLATE = "/booking";

    @Test
    void create() throws Exception {
        when(bookingFacade.create(bookingRecord)).thenReturn(booking);

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingRecord)))
                .andExpect(status().isCreated());

        verify(bookingFacade).create(bookingRecord);
    }

    @Test
    void findById() throws Exception {
        when(service.findById(ID)).thenReturn(booking);

        mockMvc.perform(get(URL_TEMPLATE + "/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(booking)));

        verify(service).findById(ID);
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(put(URL_TEMPLATE + "/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateBookingRecord)))
                .andExpect(status().isOk());

        verify(bookingFacade).update(ID, updateBookingRecord);
    }

    @Test
    void confirm() throws Exception {
        mockMvc.perform(put(URL_TEMPLATE + "/confirm/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service).confirm(ID);
    }

    @Test
    void cancel() throws Exception {
        mockMvc.perform(put(URL_TEMPLATE + "/cancel/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingFacade).cancel(ID);
    }

    @Test
    void rebook() throws Exception {
        mockMvc.perform(put(URL_TEMPLATE + "/rebook-cancelled/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rebookBookingRecord)))
                .andExpect(status().isOk());

        verify(bookingFacade).rebookCancelled(ID, rebookBookingRecord);
    }

    @Test
    void deleteBlock() throws Exception {
        mockMvc.perform(delete(URL_TEMPLATE + "/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingFacade).delete(ID);
    }

    @BeforeEach
    void setUp() {
        booking = createBooking();
        bookingRecord = createBookingRecord();
        rebookBookingRecord = createRebookBookingRecord();
        updateBookingRecord = createUpdateBookingRecord();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    public static final long ID = 1L;
    private Booking booking;
    private BookingRecord bookingRecord;
    private RebookBookingRecord rebookBookingRecord;
    private UpdateBookingRecord updateBookingRecord;
    @MockBean
    private BookingFacade bookingFacade;
    @MockBean
    private BookingService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper mapper;
}
