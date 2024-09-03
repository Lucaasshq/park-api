package com.LucasH.park_api.exeception;

public class UsernameUniqueViolationExeception extends RuntimeException {

    public UsernameUniqueViolationExeception(String message) {
        super(message);
    }
}
