package com.example.parking.exception;

public class AlreadyParkedException extends RuntimeException {

    public AlreadyParkedException(String plate) {
        super("Vehicle is already parked: " + plate);
    }
}
