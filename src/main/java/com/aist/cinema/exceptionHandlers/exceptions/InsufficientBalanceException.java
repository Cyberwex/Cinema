package com.aist.cinema.exceptionHandlers.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(double diff) {
        super("You don't have enough " + diff + " AZN to buy a ticket, please top up your balance.");
    }
}

