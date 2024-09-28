package com.LucasH.park_api.exeception;

public class CodigoUniqueViolationExeception extends RuntimeException {
    public CodigoUniqueViolationExeception(String message) {
        super(message);
    }
}
