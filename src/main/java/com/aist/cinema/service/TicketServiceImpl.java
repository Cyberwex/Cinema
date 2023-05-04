package com.aist.cinema.service;

import com.aist.cinema.dto.ticket.TicketResponse;
import com.aist.cinema.entity.*;
import com.aist.cinema.exceptionHandlers.exceptions.*;
import com.aist.cinema.repository.SeatRepository;
import com.aist.cinema.repository.SessionRepository;
import com.aist.cinema.repository.TicketRepository;
import com.aist.cinema.repository.UserRepository;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final SessionRepository sessionRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    public TicketServiceImpl(TicketRepository ticketRepository, SeatRepository seatRepository, SessionRepository sessionRepository, JwtService jwtService, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.sessionRepository = sessionRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Ticket not found with id " + id));
    }

    private Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket createTicket(Integer seatNumber, Long sessionId, String userToken) throws Exception {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new Exception("Session with ID " + sessionId + " not found"));
        if (session == null) {
            throw new SessionCannotBeNullException();
        }
        User user = userRepository.findByUsername(jwtService.extractUsername(userToken)).get();
        user.setBalance(user.getBalance()-session.getTicketPrice());

        if(user.getBalance()<session.getTicketPrice()){
            Double diff = session.getTicketPrice()-user.getBalance();
            throw new InsufficientBalanceException(diff);
        }

        if (seatNumber == null || seatNumber <= 0) {
            throw new InvalidSeatNumberException();
        }

        if (!hasAvailableSeats(session)) {
            throw new NoAvailableSeatsException();
        }

        Optional<Seat> optionalSeat = findSeatByNumber(seatNumber, session.getHall());

        if (!optionalSeat.isPresent()) {
            throw new Exception("Seat not found");
        }


        Optional<Ticket> ticket = session.getTickets().stream()
                .filter(t -> t.getSeat().getNumber() == seatNumber)
                .findFirst();

        if (ticket.isPresent()) {
            throw new SeatAlreadyOccupiedException();
        }
        Seat seat = optionalSeat.get();

        Ticket createdTicket = new Ticket();
        createdTicket.setSeat(seat);

        createdTicket.setSession(session);
        createdTicket.setUser(user);
        userRepository.save(user);

        return ticketRepository.save(createdTicket);
    }

    private boolean hasAvailableSeats(Session session) {
       return session.getAvailableSeats() > 0;
    }

    private Optional<Seat> findSeatByNumber(Integer seatNumber, Hall hall) {
        List<Seat> seats = seatRepository.findSeatsByHall(hall);
        return seats.stream().filter(seat -> seat.getNumber().equals(seatNumber)).findFirst();
    }


    public Ticket updateTicket(Long id, Ticket ticket) {
        Ticket existingTicket = getTicketById(id);

        existingTicket.setSeat(ticket.getSeat());
        existingTicket.setUser(ticket.getUser());
        existingTicket.setSession(ticket.getSession());

        return saveTicket(existingTicket);
    }

    public void deleteTicket(Long id) {
        Ticket ticket = getTicketById(id);
        User user = ticket.getUser();
        Session session = ticket.getSession();
        user.setBalance(user.getBalance()+session.getTicketPrice());
        userRepository.save(user);
        ticketRepository.delete(ticket);
    }
}
