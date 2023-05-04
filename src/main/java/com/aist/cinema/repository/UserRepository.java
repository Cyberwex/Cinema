package com.aist.cinema.repository;

import com.aist.cinema.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(value = "user", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByUsername(String username);
    @EntityGraph(value = "user", type = EntityGraph.EntityGraphType.LOAD)
    List<User> findAll();
    @EntityGraph(value = "user", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findById(Long userId);
}
