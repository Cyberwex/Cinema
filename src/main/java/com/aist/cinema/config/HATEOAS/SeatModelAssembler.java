package com.aist.cinema.config.HATEOAS;

import com.aist.cinema.controller.SeatController;
import com.aist.cinema.entity.Seat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SeatModelAssembler implements RepresentationModelAssembler<Seat, EntityModel<Seat>> {
    @Override
    public EntityModel<Seat> toModel(Seat seat) {
        return EntityModel.of(seat,
                linkTo(methodOn(SeatController.class).getSeatById(seat.getId())).withSelfRel(),
                linkTo(methodOn(SeatController.class).getAllSeats()).withRel("seats"));
    }
}
