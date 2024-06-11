package com.hostfully.booking.controller;

import com.hostfully.booking.application.booking.BlockFacade;
import com.hostfully.booking.model.Reservation;
import com.hostfully.booking.record.BlockRecord;
import com.hostfully.booking.record.UpdateBlockRecord;
import com.hostfully.booking.service.ReservationService;
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
@RequestMapping("/block")
public class BlockController {

    private final BlockFacade blockFacade;
    private final ReservationService service;

    @Operation(summary = "Create a block")
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody BlockRecord request) {
        Reservation reservation = blockFacade.create(request);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservation.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Find a block by id")
    @GetMapping("/{id}")
    public Reservation findById(final @PathVariable("id") Long id) {
        return service.findBlockById(id);
    }

    @Operation(summary = "Update a block")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UpdateBlockRecord request) {
        blockFacade.update(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a block")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        blockFacade.delete(id);
        return ResponseEntity.ok().build();
    }
}
