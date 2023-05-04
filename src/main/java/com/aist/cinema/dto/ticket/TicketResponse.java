package com.aist.cinema.dto.ticket;

import com.aist.cinema.entity.Session;
import com.aist.cinema.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private Long id;
    private User user;
    private Session session;
    private Integer seatNumber;
}
