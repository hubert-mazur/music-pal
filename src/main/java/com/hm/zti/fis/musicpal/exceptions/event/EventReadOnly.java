package com.hm.zti.fis.musicpal.exceptions.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Event is read only now.")
public class EventReadOnly extends Exception{

}
