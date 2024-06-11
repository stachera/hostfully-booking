package com.hostfully.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostfully.booking.application.booking.BlockFacade;
import com.hostfully.booking.base.TestBase;
import com.hostfully.booking.model.Reservation;
import com.hostfully.booking.record.BlockRecord;
import com.hostfully.booking.record.UpdateBlockRecord;
import com.hostfully.booking.service.ReservationService;
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


@WebMvcTest(BlockController.class)
class BlockControllerTest extends TestBase {

    public static final String URL_TEMPLATE = "/block";

    @Test
    void create() throws Exception {
        when(blockFacade.create(blockRecord)).thenReturn(reservation);

        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(blockRecord)))
                .andExpect(status().isCreated());

        verify(blockFacade).create(blockRecord);
    }

    @Test
    void findById() throws Exception {
        when(service.findBlockById(ID)).thenReturn(reservation);

        mockMvc.perform(get(URL_TEMPLATE + "/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(reservation)));

        verify(service).findBlockById(ID);
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(put(URL_TEMPLATE + "/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateBlockRecord)))
                .andExpect(status().isOk());

        verify(blockFacade).update(ID, updateBlockRecord);
    }

    @Test
    void deleteBlock() throws Exception {
        mockMvc.perform(delete(URL_TEMPLATE + "/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(blockFacade).delete(ID);
    }

    @BeforeEach
    void setUp() {
        reservation = createReservation();
        blockRecord = createBlockRecord();
        updateBlockRecord = createUpdateBlockRecord();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    public static final long ID = 1L;
    private Reservation reservation;
    private BlockRecord blockRecord;
    private UpdateBlockRecord updateBlockRecord;
    @MockBean
    private BlockFacade blockFacade;
    @MockBean
    private ReservationService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper mapper;

}
