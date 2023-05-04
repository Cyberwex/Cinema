package com.aist.cinema.service;

import com.aist.cinema.entity.Hall;

import java.util.List;

public interface HallService {
    List<Hall> getAllHalls();
    Hall getHallById(Long id);
    Hall saveHall(Hall hall);
    Hall updateHall(Long id, Hall hall);
    void deleteHall(Long id);
    Hall createHall(String name, int capacity);
    Hall findHallByNameWithSessions(String hallName);
}
