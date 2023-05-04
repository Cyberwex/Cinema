package com.aist.cinema.service;

import com.aist.cinema.dto.authentication.AuthenticationRequest;
import com.aist.cinema.dto.authentication.AuthenticationResponse;
import com.aist.cinema.dto.authentication.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse register(RegisterRequest request) throws Exception;
}
