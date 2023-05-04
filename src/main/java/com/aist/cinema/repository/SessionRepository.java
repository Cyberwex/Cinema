package com.aist.cinema.repository;

import com.aist.cinema.entity.Session;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @EntityGraph(value = "sessionWithMovieAndHall", type = EntityGraph.EntityGraphType.LOAD)
    List<Session> findAll();
    @EntityGraph(value = "sessionWithMovieAndHall", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Session> findById(Long id);

}
