package com.aist.cinema;

import com.aist.cinema.entity.*;
import com.aist.cinema.exceptionHandlers.exceptions.*;
import com.aist.cinema.repository.SeatRepository;
import com.aist.cinema.repository.SessionRepository;
import com.aist.cinema.repository.TicketRepository;
import com.aist.cinema.repository.UserRepository;
import com.aist.cinema.service.JwtService;
import com.aist.cinema.service.TicketServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    private static final Long SESSION_ID = 1L;
    private static final Integer SEAT_NUMBER = 1;
    private static final String USER_TOKEN = "token";


    @Mock
    private SeatRepository seatRepository;
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = InvalidSeatNumberException.class)
    public void testCreateTicketWithInvalidSeatNumber() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("CyberWex");
        user.setBalance(20.0);

        List<Seat> seatList = new ArrayList<>();
        seatList.add(new Seat(1));
        Hall hall = new Hall();
        hall.setCapacity(3);
        hall.setName("testHall");
        hall.setSeats(seatList);

        Session session = new Session();
        session.setId(SESSION_ID);
        session.setTicketPrice(10.0);
        session.setHall(hall);



        when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));
        when(userRepository.findByUsername(jwtService.extractUsername(USER_TOKEN))).thenReturn(Optional.of(user));
        when(seatRepository.findSeatsByHall(hall)).thenReturn(seatList);

        ticketService.createTicket(-1, SESSION_ID, USER_TOKEN);
    }

    @Test(expected = NoAvailableSeatsException.class)
    public void testCreateTicketWithNoAvailableSeats() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("CyberWex");
        user.setBalance(20.0);

        Session session = new Session();
        session.setId(SESSION_ID);
        session.setTicketPrice(10.0);
        session.setHall(new Hall());
        session.getHall().setSeats(new ArrayList<>());
        session.getHall().getSeats().add(new Seat());
        session.getHall().getSeats().get(0).setNumber(SEAT_NUMBER);


        when(userRepository.findByUsername(jwtService.extractUsername(USER_TOKEN))).thenReturn(Optional.of(user));
        when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));

        ticketService.createTicket(SEAT_NUMBER, SESSION_ID, USER_TOKEN);
    }

    @Test

    public void testCreateTicketWithSeatAlreadyOccupied() {

        User user = new User();
        user.setId(1L);
        user.setUsername("CyberWex");
        user.setBalance(20.0);

        List<Seat> seatList = new ArrayList<>();
        seatList.add(new Seat(1));
        seatList.add(new Seat(2));
        seatList.add(new Seat(3));

        Hall hall = new Hall();
        hall.setCapacity(3);
        hall.setName("testHall");
        hall.setSeats(seatList);

        Session session = new Session();
        session.setId(SESSION_ID);
        session.setTicketPrice(10.0);
        session.setHall(hall);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setSeat(session.getHall().getSeats().get(0));
        ticket.setSession(session);
        ticket.setUser(user);

        session.getTickets().add(ticket);


        when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));
        when(userRepository.findByUsername(jwtService.extractUsername(USER_TOKEN))).thenReturn(Optional.of(user));
        when(seatRepository.findSeatsByHall(hall)).thenReturn(seatList);

        assertThrows(SeatAlreadyOccupiedException.class, () -> {
            ticketService.createTicket(SEAT_NUMBER, SESSION_ID, USER_TOKEN);
        });

        verify(sessionRepository).findById(SESSION_ID);
        verify(userRepository).findByUsername(jwtService.extractUsername(USER_TOKEN));
    }
    @Test(expected = InsufficientBalanceException.class)
    public void testCreateTicketWithInsufficientBalance() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("CyberWex");
        user.setBalance(5.0);

        Session session = new Session();
        session.setId(SESSION_ID);
        session.setTicketPrice(10.0);

        when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));
        when(userRepository.findByUsername(jwtService.extractUsername(USER_TOKEN))).thenReturn(Optional.of(user));

        ticketService.createTicket(SEAT_NUMBER, SESSION_ID, USER_TOKEN);
    }

    @Test(expected = Exception.class)
    public void testCreateTicket() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("CyberWex");
        user.setBalance(20.0);

        Session session = new Session();
        session.setId(SESSION_ID);
        session.setTicketPrice(10.0);
        session.setHall(new Hall());
        session.getHall().setSeats(new ArrayList<>());
        session.getHall().getSeats().add(new Seat());
        session.getHall().getSeats().get(0).setId(1L);
        session.getHall().getSeats().get(0).setNumber(SEAT_NUMBER);

        when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));
        when(userRepository.findByUsername(jwtService.extractUsername(USER_TOKEN))).thenReturn(Optional.of(user));

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setSeat(session.getHall().getSeats().get(0));
        ticket.setSession(session);
        ticket.setUser(user);

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket createdTicket = ticketService.createTicket(SEAT_NUMBER, SESSION_ID, USER_TOKEN);

        assertNotNull(createdTicket);
        assertEquals(ticket.getId(), createdTicket.getId());
        assertEquals(ticket.getSeat(), createdTicket.getSeat());
        assertEquals(ticket.getSession(), createdTicket.getSession());
        assertEquals(ticket.getUser(), createdTicket.getUser());
    }
}
