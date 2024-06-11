package com.hostfully.booking.controller;

import com.hostfully.booking.application.booking.BookingFacade;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.record.BookingRecord;
import com.hostfully.booking.record.RebookBookingRecord;
import com.hostfully.booking.record.UpdateBookingRecord;
import com.hostfully.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingFacade bookingFacade;
    private final BookingService service;

    @Operation(summary = "Create a booking")
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody BookingRecord request) {
        Booking booking = bookingFacade.create(request);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(booking.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Find a booking by id")
    @GetMapping("/{id}")
    public Booking findById(final @PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Update a booking")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UpdateBookingRecord request) {
        bookingFacade.update(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Confirm requested booking")
    @PutMapping("/confirm/{id}")
    public ResponseEntity<Void> confirm(@PathVariable("id") Long id) {
        service.confirm(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancel a booking")
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id) {
        bookingFacade.cancel(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Rebook a cancelled booking")
    @PutMapping("/rebook-cancelled/{id}")
    public ResponseEntity<Void> rebook(@PathVariable("id") Long id, @Valid @RequestBody RebookBookingRecord request) {
        bookingFacade.rebookCancelled(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a booking")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        bookingFacade.delete(id);
        return ResponseEntity.ok().build();
    }
}
