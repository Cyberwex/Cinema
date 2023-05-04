package com.aist.cinema.exceptionHandlers.exceptions;

public class SeatAlreadyOccupiedException extends Exception {
    public SeatAlreadyOccupiedException() {
        super("Seat already occupied");
    }
}

