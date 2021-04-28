package com.personio.employeehierarchy.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidOrganizationException extends RuntimeException {

    public InvalidOrganizationException(final String message) {
        super(message);
    }
}
