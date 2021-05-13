package com.hm.zti.fis.musicpal.exceptions.person;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid email address")
public class InvalidEmailException extends Exception{
    public InvalidEmailException(String message) {
        super(message);
    }
}
