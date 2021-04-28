package com.personio.employeehierarchy.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundEmployeeException extends RuntimeException {

    public NotFoundEmployeeException(final String message) {
        super(message);
    }
}
