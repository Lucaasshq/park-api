package com.LucasH.park_api.exeception;

public class CpfUniqueViolationExeception extends RuntimeException {
    public CpfUniqueViolationExeception(String message) {
        super(message);
    }
}
