package com.hm.zti.fis.musicpal.event;

import com.hm.zti.fis.musicpal.exceptions.event.EventNotOwnedException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotPatricipated;
import com.hm.zti.fis.musicpal.exceptions.event.EventReadOnly;
import com.hm.zti.fis.musicpal.exceptions.link.LinkNotExists;
import com.hm.zti.fis.musicpal.exceptions.link.LinkNotInEvent;
import com.hm.zti.fis.musicpal.exceptions.person.UserNotExistsException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotFoundException;
import com.hm.zti.fis.musicpal.link.LinkRequest;
import com.hm.zti.fis.musicpal.person.PatchRequest;
import com.hm.zti.fis.musicpal.person.PersonBasicInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Service
@AllArgsConstructor
@RequestMapping(path = "/api/event")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<Event> getParticipatedEvents() throws UserNotExistsException {
        return this.eventService.getEvents();
    }

    @GetMapping(path = "/participants/{eventId}")
    public Set<PersonBasicInfo> getEventParticipants(@PathVariable Long eventId) throws EventNotFoundException {
        return this.eventService.getEventParticipants(eventId);
    }

    @PostMapping
    public void addEvent(@RequestBody EventRequest request) throws UserNotExistsException, EventNotFoundException {
        this.eventService.addEvent(request);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteEvent(@PathVariable Long id) throws UserNotExistsException, EventNotOwnedException {
        this.eventService.deleteEvent(id);
    }

    @PatchMapping(path = "/{id}/title")
    public void changeTitle(@PathVariable Long id, @RequestBody PatchRequest request) throws EventNotFoundException, UserNotExistsException, EventNotOwnedException, EventReadOnly {
        this.eventService.changeProperty(id, request.getValue(), "title");
    }

    @PatchMapping(path = "/{id}/description")
    public void changeDescription(@PathVariable Long id, @RequestBody PatchRequest request) throws EventNotFoundException, UserNotExistsException, EventNotOwnedException, EventReadOnly {
        this.eventService.changeProperty(id, request.getValue(), "description");
    }

    @PutMapping(path = "/{eventId}/invite/{personId}")
    public void inviteToEvent(@PathVariable Long eventId, @PathVariable Long personId) throws UserNotExistsException, EventNotOwnedException, EventNotFoundException, EventReadOnly {
        this.eventService.addPersonToEvent(eventId, personId);
    }

    @DeleteMapping(path = "/{eventId}/remove/{personId}")
    public void removeFromEvent(@PathVariable Long eventId, @PathVariable Long personId) throws UserNotExistsException, EventNotOwnedException, EventNotFoundException, EventReadOnly {
        this.eventService.removePersonFromEvent(eventId, personId);
    }

    @PutMapping(path = "/{eventId}/link")
    public void addNewLink(@PathVariable Long eventId, @RequestBody LinkRequest linkRequest) throws UserNotExistsException, EventNotFoundException, EventNotPatricipated, EventReadOnly {
        this.eventService.addLink(eventId, linkRequest);
    }

    @PatchMapping(path = "/{eventId}/link/{linkId}/upvote")
    public void upvoteLink(@PathVariable Long eventId, @PathVariable Long linkId) throws UserNotExistsException, LinkNotExists, EventNotFoundException, LinkNotInEvent, EventNotPatricipated, EventReadOnly {
        System.out.println("\n\n\n\nHERE\n\n\n");
        this.eventService.changeVote(eventId, linkId, Vote.upvote);
    }

    @PatchMapping(path = "/{eventId}/link/{linkId}/downvote")
    public void downVoteLink(@PathVariable Long eventId, @PathVariable Long linkId) throws UserNotExistsException, LinkNotExists, EventNotFoundException, LinkNotInEvent, EventNotPatricipated, EventReadOnly {
        this.eventService.changeVote(eventId, linkId, Vote.downvote);
    }

    @PutMapping(path = "/{eventId}")
    public void updateEvent(@PathVariable Long eventId, @RequestBody EventRequest request) throws UserNotExistsException, EventNotFoundException, EventReadOnly {
        this.eventService.modifyEvent(eventId, request);
    }

    @GetMapping(path = "/{eventId}")
    public Event getSelectedEvent(@PathVariable Long eventId) throws UserNotExistsException, EventNotFoundException, EventNotPatricipated {
        return this.eventService.getSelectedEvent(eventId);
    }

    @DeleteMapping(path = "/{eventId}/links/{linkId}")
    public void deleteLinkFromEvent(@PathVariable Long eventId, @PathVariable Long linkId) throws UserNotExistsException, EventNotFoundException, EventNotPatricipated, EventReadOnly {
        this.eventService.deleteLink(eventId, linkId);
    }

    @GetMapping(path = "/{eventId}/archive")
    public void archiveEvent(@PathVariable Long eventId) throws EventNotFoundException, UserNotExistsException {
        this.eventService.closeEvent(eventId);
    }
}
