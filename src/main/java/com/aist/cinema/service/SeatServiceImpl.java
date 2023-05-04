package com.aist.cinema.service;

import com.aist.cinema.entity.Hall;
import com.aist.cinema.entity.Seat;
import com.aist.cinema.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SeatServiceImpl implements SeatService{
    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Seat not found with id:" + id));
    }

    public Seat saveSeat(Seat seat) {
        Hall hall = seat.getHall();
        if(hall.getSeats().size()>=hall.getCapacity()){
            throw new RuntimeException("Hall is full");
        }
        return seatRepository.save(seat);
    }

    public Seat updateSeat(Long id, Seat seat) {
        Seat existingSeat = getSeatById(id);

        existingSeat.setHall(seat.getHall());
        existingSeat.setNumber(seat.getNumber());

        return seatRepository.save(existingSeat);
    }

    public void deleteSeat(Long id) {
        Seat seat = getSeatById(id);
        seatRepository.delete(seat);
    }
}
