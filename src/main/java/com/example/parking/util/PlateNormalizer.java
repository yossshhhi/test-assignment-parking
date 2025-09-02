package com.example.parking.util;

public final class PlateNormalizer {

    private PlateNormalizer() {
    }

    public static String normalize(String raw) {
        return raw.trim().toUpperCase();
    }
}
