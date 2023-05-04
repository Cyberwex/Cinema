package com.aist.cinema.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "session")
@NamedEntityGraph(
        name = "sessionWithMovieAndHall",
        attributeNodes = {
                @NamedAttributeNode("movie"),
                @NamedAttributeNode("hall"),
                @NamedAttributeNode("tickets")
        }
)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;
    private Double ticketPrice;
    @JsonProperty("available_seats")
    public int getAvailableSeats() {
        int totalSeats = hall.getCapacity();
        int soldSeats = tickets.size();
        return totalSeats - soldSeats;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    @ToString.Exclude
    private Hall hall;

    @JsonIgnore
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Ticket> tickets = new ArrayList<>();

}
