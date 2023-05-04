package com.aist.cinema.dto.session;

import com.aist.cinema.entity.Movie;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {
    Long id;
    private LocalDateTime startTime;
    private String movieTitle;
    private Integer durationMin;
    private Double ticketPrice;
    private Long hallId;
    private String hallName;
}
