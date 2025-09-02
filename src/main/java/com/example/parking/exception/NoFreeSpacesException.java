package com.example.parking.exception;

public class NoFreeSpacesException extends RuntimeException {

    public NoFreeSpacesException() {
        super("There are no free spaces in the parking lot.");
    }
}
