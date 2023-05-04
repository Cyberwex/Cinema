package com.aist.cinema.repository;

import com.aist.cinema.entity.Hall;
import com.aist.cinema.entity.Seat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    @EntityGraph(value = "hallWithSessionsAndSeats", type = EntityGraph.EntityGraphType.LOAD)
    Hall findByName(String name);
    @EntityGraph(value = "hallWithSessionsAndSeats", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Hall> findById(Long id);

}
