package com.hm.zti.fis.musicpal.exceptions.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Event not found. Check id")
public class EventNotFoundException extends Exception{

}
