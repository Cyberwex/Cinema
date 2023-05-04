package com.aist.cinema;

import com.aist.cinema.dto.authentication.RegisterRequest;
import com.aist.cinema.entity.Movie;
import com.aist.cinema.entity.security.Role;
import com.aist.cinema.repository.SessionRepository;
import com.aist.cinema.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(HallService hallService, MovieService movieService, SessionService sessionService,
                             TicketService ticketService, SessionRepository sessionRepository,
                             AuthenticationService authenticationService
    ) {
        return args -> {
            hallService.createHall("Birbank", 30);
            hallService.createHall("Dolby Atmos", 20);
            hallService.createHall("Platinum", 30);
            movieService.saveMovie(new Movie("Killer 3", "Default description", 120));
            movieService.saveMovie(new Movie("Guardians of the Galaxy", "Default description", 90));
            movieService.saveMovie(new Movie("Zone", "Default description", 150));
            movieService.saveMovie(new Movie("Evil Dead Rise", "Default description", 120));
            sessionService.saveSession("01.05.23 15:00", "Killer 3", "Birbank", 13.5);
            sessionService.saveSession("01.05.23 15:00", "Guardians of the Galaxy", "Platinum", 7.5);
            sessionService.saveSession("01.05.23 15:00", "Zone", "Dolby Atmos", 8.0);
            authenticationService.register(new RegisterRequest("Farid", "Hasanov", "Safar",
                    "CyberWex", "P@ssw0rd", Role.ADMIN));
            authenticationService.register(new RegisterRequest("Tural", "Mahmudov", "Akbar",
                    "Tural", "P@ssw0rd", Role.USER));
            /*Нужен новый токен для создания билета*/
            for (int i = 1; i <= 3; i++) {
                ticketService.createTicket(i, (long) i, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDeWJlcldleCIsImlhdCI6MTY4MzIwODIxNSwiZXhwIjoxNjgzMjk0NjE1fQ.jLzIKX7c01I4xr6PC5m4qNJNsYeZxQ8YPGFxScIppvw");
            }
        };
    }
}
