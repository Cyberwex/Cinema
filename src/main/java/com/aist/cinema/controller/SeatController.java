package com.aist.cinema.controller;

import com.aist.cinema.config.HATEOAS.SeatModelAssembler;
import com.aist.cinema.entity.Seat;
import com.aist.cinema.service.SeatService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class SeatController {

    private final SeatService seatService;
    private final SeatModelAssembler seatModelAssembler;

    public SeatController(SeatService seatService, SeatModelAssembler seatModelAssembler) {
        this.seatService = seatService;
        this.seatModelAssembler = seatModelAssembler;
    }

    @GetMapping("/seats")
    public CollectionModel<EntityModel<Seat>> getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        List<EntityModel<Seat>> seatModels = seats.stream()
                .map(seatModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(seatModels,
                linkTo(methodOn(SeatController.class).getAllSeats()).withSelfRel());
    }

    @GetMapping("/seats/{id}")
    public EntityModel<Seat> getSeatById(@PathVariable("id") Long id) {
        Seat seat = seatService.getSeatById(id);
        return seatModelAssembler.toModel(seat);
    }

    @PostMapping("/seats")
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public ResponseEntity<EntityModel<Seat>> createSeat(@RequestBody Seat seat) {
        Seat createdSeat = seatService.saveSeat(seat);
        EntityModel<Seat> seatModel = seatModelAssembler.toModel(createdSeat);

        return ResponseEntity.created(seatModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(seatModel);
    }

    @PutMapping("/seats/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public ResponseEntity<EntityModel<Seat>> updateSeat(@PathVariable("id") Long id, @RequestBody Seat seat) {
        Seat updatedSeat = seatService.updateSeat(id, seat);
        EntityModel<Seat> seatModel = seatModelAssembler.toModel(updatedSeat);

        return ResponseEntity.ok(seatModel);
    }

    @DeleteMapping("/seats/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteSeat(@PathVariable("id") Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
