package com.hm.zti.fis.musicpal.exceptions.person;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User with given mail exists")
public class UserExistsException extends Exception{
    public UserExistsException(String message) {
        super(message);
    }
}

