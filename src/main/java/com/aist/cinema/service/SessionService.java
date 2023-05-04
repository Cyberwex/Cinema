package com.aist.cinema.service;

import com.aist.cinema.entity.Hall;
import com.aist.cinema.entity.Movie;
import com.aist.cinema.entity.Session;

import java.util.List;

public interface SessionService {
    List<Session> getAllSessions();
    Session getSessionById(Long id);
    Session saveSession(String date, String title, String hallName,Double ticketPrice);
    Session updateSession(Long id, Session session);
    void deleteSession(Long id);

}
