package com.aist.cinema.dto.user;

import com.aist.cinema.entity.Ticket;
import com.aist.cinema.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private User user;
    private Set<Ticket> tickets;
}
