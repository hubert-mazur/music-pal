package com.hm.zti.fis.musicpal.exceptions.login;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "Invalid email or password")
public class InvalidCredential extends Exception{

    public InvalidCredential(String invalid_email_or_password) {
    }
}
