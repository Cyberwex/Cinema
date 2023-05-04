package com.aist.cinema.controller;

import com.aist.cinema.entity.Hall;
import com.aist.cinema.entity.Session;
import com.aist.cinema.service.SessionService;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/sessions")
    public CollectionModel<EntityModel<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        List<EntityModel<Session>> sessionModels = sessions.stream()
                .map(session -> EntityModel.of(session,
                        linkTo(methodOn(SessionController.class).getSessionById(session.getId())).withSelfRel(),
                        linkTo(methodOn(SessionController.class).updateSession(session.getId(), session)).withRel("update"),
                        linkTo(methodOn(SessionController.class).deleteSession(session.getId())).withRel("delete")))
                .collect(Collectors.toList());
        return CollectionModel.of(sessionModels,
                linkTo(methodOn(SessionController.class).getAllSessions()).withSelfRel());
    }

    @GetMapping("/sessions/{id}")
    public EntityModel<Session> getSessionById(@PathVariable("id") Long id) {
        Session session = sessionService.getSessionById(id);
        return EntityModel.of(session,
                linkTo(methodOn(SessionController.class).getSessionById(id)).withSelfRel(),
                linkTo(methodOn(SessionController.class).getAllSessions()).withRel("sessions"));
    }

    @PostMapping("/sessions")
    @PreAuthorize("hasAnyAuthority('admin:create', 'management:create')")
    @SneakyThrows
    public ResponseEntity<EntityModel<Session>> createSession(@RequestParam String startDate,@RequestParam String movieName, @RequestParam String hallName,@RequestParam double ticketPrice) {
        Session createdSession = sessionService.saveSession(startDate,movieName,hallName,ticketPrice);
        EntityModel<Session> sessionModel = EntityModel.of(createdSession,
                linkTo(methodOn(SessionController.class).getSessionById(createdSession.getId())).withSelfRel(),
                linkTo(methodOn(SessionController.class).updateSession(createdSession.getId(), createdSession)).withRel("update"),
                linkTo(methodOn(SessionController.class).deleteSession(createdSession.getId())).withRel("delete"));
        return ResponseEntity.created(linkTo(methodOn(SessionController.class).getSessionById(createdSession.getId())).toUri()).body(sessionModel);
    }

    @PutMapping("/sessions/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public ResponseEntity<EntityModel<Session>> updateSession(@PathVariable("id") Long id, @RequestBody Session session) {
        Session updatedSession = sessionService.updateSession(id, session);
        EntityModel<Session> sessionModel = EntityModel.of(updatedSession,
                linkTo(methodOn(SessionController.class).getSessionById(updatedSession.getId())).withSelfRel(),
                linkTo(methodOn(SessionController.class).updateSession(updatedSession.getId(), updatedSession)).withRel("update"),
                linkTo(methodOn(SessionController.class).deleteSession(updatedSession.getId())).withRel("delete"));
        return ResponseEntity.ok(sessionModel);
    }

    @DeleteMapping("/sessions/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteSession(@PathVariable("id") Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
