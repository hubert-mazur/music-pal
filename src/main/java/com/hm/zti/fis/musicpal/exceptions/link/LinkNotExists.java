package com.hm.zti.fis.musicpal.exceptions.link;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "Link not exists")
public class LinkNotExists extends Exception{
}