package com.aist.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "seat")
@NamedEntityGraph(
        name = "seat",
        attributeNodes = {
                @NamedAttributeNode("hall")
        })

public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    @ToString.Exclude
    private Hall hall;

    public Seat(Integer number) {
        this.number = number;
    }
}
