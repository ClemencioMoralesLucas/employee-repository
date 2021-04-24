package com.personio.employeehierarchy.domain;

public class InvalidOrganizationException extends RuntimeException {

    public InvalidOrganizationException(final String message) {
        super(message);
    }
}
