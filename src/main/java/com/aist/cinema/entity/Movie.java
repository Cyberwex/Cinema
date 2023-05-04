package com.aist.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Integer durationMin;

    public Movie(String title, String description, Integer durationMin) {
        this.title = title;
        this.description = description;
        this.durationMin = durationMin;
    }
}
