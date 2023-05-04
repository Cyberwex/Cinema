package com.aist.cinema.service;

import com.aist.cinema.entity.Seat;

import java.util.List;

public interface SeatService {
    List<Seat> getAllSeats();
    Seat getSeatById(Long id);
    Seat saveSeat(Seat seat);
    Seat updateSeat(Long id, Seat seat);
    void deleteSeat(Long id);
}
