package com.aist.cinema.dto.authentication;

import com.aist.cinema.entity.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String surname;
    private String username;
    private String password;
    private Role role;
}
