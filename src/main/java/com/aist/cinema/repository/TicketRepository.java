package com.aist.cinema.repository;

import com.aist.cinema.entity.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @EntityGraph(value = "ticket", type = EntityGraph.EntityGraphType.LOAD)
    List<Ticket> findAll();
    @EntityGraph(value = "ticket", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Ticket> findById(Long id);

}
