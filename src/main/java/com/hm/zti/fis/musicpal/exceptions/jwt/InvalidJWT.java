package com.hm.zti.fis.musicpal.exceptions.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Invalid JWT")
public class InvalidJWT extends Throwable {
}
