package com.hm.zti.fis.musicpal.exceptions.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "You are not participant of this event to make changes")
public class EventNotPatricipated extends Exception{

}
