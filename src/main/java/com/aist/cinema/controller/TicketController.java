package com.aist.cinema.controller;

import com.aist.cinema.entity.Ticket;
import com.aist.cinema.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public CollectionModel<EntityModel<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        List<EntityModel<Ticket>> ticketModels = new ArrayList<>();

        for (Ticket ticket : tickets) {
            System.out.println(ticket);
            EntityModel<Ticket> ticketModel = EntityModel.of(ticket,
                    linkTo(methodOn(TicketController.class).getTicketById(ticket.getId())).withSelfRel(),
                    linkTo(methodOn(TicketController.class).getAllTickets()).withRel("tickets"));
            ticketModels.add(ticketModel);
        }

        return CollectionModel.of(ticketModels, linkTo(methodOn(TicketController.class).getAllTickets()).withSelfRel());
    }

    @GetMapping("/tickets/{id}")
    public EntityModel<Ticket> getTicketById(@PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        System.out.println(ticket);
        return EntityModel.of(ticket,
                linkTo(methodOn(TicketController.class).getTicketById(id)).withSelfRel(),
                linkTo(methodOn(TicketController.class).getAllTickets()).withRel("tickets"));
    }

    @PostMapping("/tickets")
    @SneakyThrows
    public ResponseEntity<EntityModel<Ticket>> createTicket(@RequestParam Integer seatNumber, Long sessionId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization").substring(7);
        Ticket createdTicket = ticketService.createTicket(seatNumber, sessionId, authorizationHeader);
        EntityModel<Ticket> ticketModel = EntityModel.of(createdTicket,
                linkTo(methodOn(TicketController.class).getTicketById(createdTicket.getId())).withSelfRel(),
                linkTo(methodOn(TicketController.class).getAllTickets()).withRel("tickets"));

        return ResponseEntity.created(linkTo(methodOn(TicketController.class).getTicketById(createdTicket.getId())).toUri()).body(ticketModel);
    }

    @PutMapping("/tickets/{id}")
    public ResponseEntity<EntityModel<Ticket>> updateTicket(@PathVariable("id") Long id, @RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketService.updateTicket(id,ticket);
        EntityModel<Ticket> ticketModel = EntityModel.of(updatedTicket,
                linkTo(methodOn(TicketController.class).getTicketById(updatedTicket.getId())).withSelfRel(),
                linkTo(methodOn(TicketController.class).getAllTickets()).withRel("tickets"));

        return ResponseEntity.ok(ticketModel);
    }

    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
