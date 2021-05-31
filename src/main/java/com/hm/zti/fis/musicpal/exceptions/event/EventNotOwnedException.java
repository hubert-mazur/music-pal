package com.hm.zti.fis.musicpal.exceptions.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You are not allowed to delete this event")
public class EventNotOwnedException extends Exception{

}
