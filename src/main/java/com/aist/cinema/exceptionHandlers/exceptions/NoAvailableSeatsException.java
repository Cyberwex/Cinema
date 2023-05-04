package com.aist.cinema.exceptionHandlers.exceptions;

public class NoAvailableSeatsException extends Exception {
    public NoAvailableSeatsException() {
        super("No available seats for this session");
    }
}

