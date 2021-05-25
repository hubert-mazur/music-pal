package com.hm.zti.fis.musicpal.event;

import com.hm.zti.fis.musicpal.exceptions.event.EventNotOwnedException;
import com.hm.zti.fis.musicpal.exceptions.person.UserNotExistsException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotFoundException;
import com.hm.zti.fis.musicpal.person.PatchRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Service
@AllArgsConstructor
@RequestMapping(path = "/api/event")
public class EventController {
    private final EventService eventService;

    @PostMapping
    public void addEvent(@RequestBody EventRequest request) throws UserNotExistsException {
        this.eventService.addEvent(request);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteEvent(@PathVariable Long id) throws UserNotExistsException, EventNotOwnedException {
        this.eventService.deleteEvent(id);
    }

    @PatchMapping(path="/{id}/title")
    public void changeTitle( @PathVariable Long id, @RequestBody PatchRequest request) throws EventNotFoundException, UserNotExistsException, EventNotOwnedException {
        this.eventService.changeProperty(id, request.getValue(), "title");
    }

    @PatchMapping(path="/{id}/description")
    public void changeDescription(@PathVariable Long id, @RequestBody PatchRequest request) throws EventNotFoundException, UserNotExistsException, EventNotOwnedException {
        this.eventService.changeProperty(id, request.getValue(), "description");
    }

    @PutMapping(path = "/{eventId}/invite/{personId}")
    public void inviteToEvent(@PathVariable Long eventId, @PathVariable Long personId) throws UserNotExistsException, EventNotOwnedException, EventNotFoundException {
        this.eventService.addPersonToEvent(eventId, personId);
    }

    @DeleteMapping(path = "/{eventId}/remove/{personId}")
    public void removeFromEvent(@PathVariable Long eventId, @PathVariable Long personId) throws UserNotExistsException, EventNotOwnedException, EventNotFoundException {
        this.eventService.removePersonFromEvent(eventId, personId);
    }

}
