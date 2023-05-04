package com.aist.cinema.service;

import com.aist.cinema.dto.ticket.TicketResponse;
import com.aist.cinema.entity.Session;
import com.aist.cinema.entity.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id);
    Ticket createTicket(Integer seatNumber, Long sessionId, String userToken) throws Exception;
    Ticket updateTicket(Long id, Ticket ticket);
    void deleteTicket(Long id);
}
