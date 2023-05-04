package com.aist.cinema.service;

import com.aist.cinema.entity.Hall;
import com.aist.cinema.entity.Seat;
import com.aist.cinema.repository.HallRepository;
import com.aist.cinema.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HallServiceImpl implements HallService{
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;

    public HallServiceImpl(HallRepository hallRepository, SeatRepository seatRepository) {
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
    }


    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    public Hall getHallById(Long id) {
        return hallRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Hall not found with id " + id));
    }

    public Hall saveHall(Hall hall) {
        return hallRepository.save(hall);
    }

    public Hall updateHall(Long id, Hall hall) {
        Hall existingHall = getHallById(id);

        existingHall.setName(hall.getName());
        existingHall.setSeats(hall.getSeats());
        existingHall.setCapacity(hall.getCapacity());

        return hallRepository.save(existingHall);
    }
    public Hall createHall(String name, int capacity) {
        Hall hall = new Hall();
        hall.setName(name);
        hall.setCapacity(capacity);

        for (int i = 1; i <= capacity; i++) {
            Seat seat = new Seat(i);
            seat.setHall(hall);
            hall.getSeats().add(seat);
        }
        hallRepository.save(hall);
        seatRepository.saveAll(hall.getSeats());

        return hall;
    }

    public void deleteHall(Long id) {
        Hall hall = getHallById(id);
        hallRepository.delete(hall);
    }

    public Hall findHallByNameWithSessions(String hallName) {
        return hallRepository.findByName(hallName);
    }
}
