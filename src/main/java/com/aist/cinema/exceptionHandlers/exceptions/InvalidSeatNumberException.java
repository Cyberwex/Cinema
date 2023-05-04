package com.aist.cinema.exceptionHandlers.exceptions;

public class InvalidSeatNumberException extends Exception {
    public InvalidSeatNumberException() {
        super("Invalid seat number");
    }
}
