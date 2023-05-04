package com.aist.cinema.repository;

import com.aist.cinema.entity.Hall;
import com.aist.cinema.entity.Seat;
import com.aist.cinema.entity.Session;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @EntityGraph(value = "seat", type = EntityGraph.EntityGraphType.LOAD)
    List<Seat> findAll();
    @EntityGraph(value = "seat", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Seat> findById(Long id);
    List<Seat> findSeatsByHall(Hall hall);
}
