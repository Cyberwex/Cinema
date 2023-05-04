package com.aist.cinema.controller;

import com.aist.cinema.entity.Hall;
import com.aist.cinema.service.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class HallController {
    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping("/halls")
    public CollectionModel<EntityModel<Hall>> getAllHalls() {
        List<Hall> halls = hallService.getAllHalls();
        List<EntityModel<Hall>> hallEntities = halls.stream()
                .map(hall -> EntityModel.of(hall,
                        linkTo(methodOn(HallController.class).getHallById(hall.getId())).withSelfRel(),
                        linkTo(methodOn(HallController.class).getAllHalls()).withRel("halls")))
                .collect(Collectors.toList());
        return CollectionModel.of(hallEntities,
                linkTo(methodOn(HallController.class).getAllHalls()).withSelfRel());
    }

    @GetMapping("/halls/{id}")
    public EntityModel<Hall> getHallById(@PathVariable("id") Long id) {
        Hall hall = hallService.getHallById(id);
        return EntityModel.of(hall,
                linkTo(methodOn(HallController.class).getHallById(id)).withSelfRel(),
                linkTo(methodOn(HallController.class).getAllHalls()).withRel("halls"));
    }

    @PostMapping("/halls")
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public ResponseEntity<EntityModel<Hall>> createHall(@RequestBody Hall hall) {
        String name = hall.getName();
        Integer capacity = hall.getCapacity();
        Hall createdHall = hallService.createHall(name, capacity);
        EntityModel<Hall> hallEntity = EntityModel.of(createdHall,
                linkTo(methodOn(HallController.class).getHallById(createdHall.getId())).withSelfRel(),
                linkTo(methodOn(HallController.class).getAllHalls()).withRel("halls"));
        return ResponseEntity.created(linkTo(methodOn(HallController.class).getHallById(createdHall.getId())).toUri())
                .body(hallEntity);
    }

    @PutMapping("/halls/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public ResponseEntity<EntityModel<Hall>> updateHall(@PathVariable("id") Long id, @RequestBody Hall hall) {
        Hall updatedHall = hallService.updateHall(id, hall);
        EntityModel<Hall> hallEntity = EntityModel.of(updatedHall,
                linkTo(methodOn(HallController.class).getHallById(updatedHall.getId())).withSelfRel(),
                linkTo(methodOn(HallController.class).getAllHalls()).withRel("halls"));
        return ResponseEntity.ok(hallEntity);
    }

    @DeleteMapping("/halls/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteHall(@PathVariable("id") Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent()
                .build();
    }
}
