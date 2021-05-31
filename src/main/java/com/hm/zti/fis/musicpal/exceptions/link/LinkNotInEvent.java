package com.hm.zti.fis.musicpal.exceptions.link;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "Link not assigned to event")
public class LinkNotInEvent extends Exception{
}