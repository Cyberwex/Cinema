package com.aist.cinema.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.*;


@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "hall")
@NamedEntityGraph(name = "hallWithSessionsAndSeats", attributeNodes = { @NamedAttributeNode("sessions"),
        @NamedAttributeNode("seats")})
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private int capacity;


    @JsonIgnore
    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Seat> seats = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Session> sessions = new HashSet<>();

}
