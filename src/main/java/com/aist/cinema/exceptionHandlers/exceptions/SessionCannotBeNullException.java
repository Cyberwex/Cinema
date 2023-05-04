package com.aist.cinema.exceptionHandlers.exceptions;

public class SessionCannotBeNullException extends Exception {
    public SessionCannotBeNullException() {
        super("Session cannot be null.");
    }
}
