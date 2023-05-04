package com.aist.cinema.service;

import com.aist.cinema.entity.Hall;
import com.aist.cinema.entity.Movie;
import com.aist.cinema.entity.Session;
import com.aist.cinema.entity.Ticket;
import com.aist.cinema.repository.MovieRepository;
import com.aist.cinema.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SessionServiceImpl implements SessionService{
    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final HallService hallService;

    private final TicketService ticketService;

    public SessionServiceImpl(SessionRepository sessionRepository, MovieRepository movieRepository, HallService hallService, TicketService ticketService) {
        this.sessionRepository = sessionRepository;
        this.movieRepository = movieRepository;
        this.hallService = hallService;
        this.ticketService = ticketService;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }


    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Session not found with id:" + id));
    }

    public Session saveSession(String date, String title, String hallName,Double ticketPrice) {
        Session newSession = new Session();
        newSession.setTicketPrice(ticketPrice);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        Movie movie = movieRepository.findByTitle(title);
        if (movie == null) {
            throw new IllegalArgumentException("Movie not found: " + title);
        }
        newSession.setMovie(movie);

        Hall hall = hallService.findHallByNameWithSessions(hallName);
        if (hall == null) {
            throw new IllegalArgumentException("Hall not found: " + hallName);
        }

        LocalDateTime sessionStart = dateTime;
        LocalDateTime sessionEnd = sessionStart.plusMinutes(movie.getDurationMin());
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = start.plusMinutes(movie.getDurationMin());
            if (sessionStart.isBefore(end) && start.isBefore(sessionEnd) && session.getHall().getName().equals(hallName)) {
                throw new IllegalStateException("Hall is already booked for the selected time period");
            }
        }
        newSession.setHall(hall);

        newSession.setStartTime(sessionStart);
        return sessionRepository.save(newSession);
    }

    public Session updateSession(Long id, Session session) {
        Session existingSession = getSessionById(id);

        existingSession.setHall(session.getHall());
        existingSession.setMovie(session.getMovie());
        existingSession.setStartTime(session.getStartTime());
        existingSession.setTicketPrice(session.getTicketPrice());

        return sessionRepository.save(existingSession);
    }

    public void deleteSession(Long id) {
        Session session = getSessionById(id);
        List<Ticket> tickets = session.getTickets();
        for(Ticket ticket : tickets){
            ticketService.deleteTicket(ticket.getId());
        }
        sessionRepository.delete(session);
    }
}
