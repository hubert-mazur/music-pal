package com.hm.zti.fis.musicpal.event;

import com.hm.zti.fis.musicpal.exceptions.event.EventNotOwnedException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotPatricipated;
import com.hm.zti.fis.musicpal.exceptions.link.LinkNotExists;
import com.hm.zti.fis.musicpal.exceptions.link.LinkNotInEvent;
import com.hm.zti.fis.musicpal.exceptions.person.UserNotExistsException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotFoundException;
import com.hm.zti.fis.musicpal.link.Link;
import com.hm.zti.fis.musicpal.link.LinkRepository;
import com.hm.zti.fis.musicpal.link.LinkRequest;
import com.hm.zti.fis.musicpal.person.Person;
import com.hm.zti.fis.musicpal.person.PersonRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;
    private final LinkRepository linkRepository;


    private Person getContextUser() throws UserNotExistsException {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return this.personRepository.getFirstByEmail(email).orElseThrow(UserNotExistsException::new);

    }

    private void checkEventOwnership(Long eventId) throws UserNotExistsException, EventNotOwnedException {
        Person person = this.getContextUser();
        List<Event> events = person.getOwnedEvents();
        events.stream().filter((event ->
                eventId.equals(event.getId())
        )).findAny().orElseThrow(EventNotOwnedException::new);
    }

    public void addEvent(EventRequest eventRequest) throws UserNotExistsException {
        Event event = new Event(eventRequest.getTitle(), eventRequest.getDescription());
        Person person = this.getContextUser();
        List<Event> ownedEvents = person.getOwnedEvents();
        Set<Person> eventParticipants = new HashSet<>();
        Set<Event> participatedEvents = person.getParticipatedEvents();


        if (ownedEvents == null)
            ownedEvents = new ArrayList<>();
        if (participatedEvents == null)
            participatedEvents = new HashSet<>();


        eventParticipants.add(person);
        event.setParticipants(eventParticipants);

        participatedEvents.add(event);
        person.setParticipatedEvents(participatedEvents);

        ownedEvents.add(event);
        person.setOwnedEvents(ownedEvents);


        this.personRepository.save(person);
        this.eventRepository.save(event);
    }

    public void addPersonToEvent(Long eventId, Long personId) throws UserNotExistsException, EventNotFoundException, EventNotOwnedException {
        this.checkEventOwnership(eventId);
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person p = this.personRepository.findById(personId).orElseThrow(UserNotExistsException::new);

        Set<Person> participants = event.getParticipants();
        Set<Event> participated = p.getParticipatedEvents();

        if (participated == null)
            participated = new HashSet<>();

        if (participants == null) {
            participants = new HashSet<>();
        }

        participated.add(event);
        p.setParticipatedEvents(participated);
        participants.add(p);
        event.setParticipants(participants);
        this.eventRepository.save(event);

    }

    public void removePersonFromEvent(Long eventId, Long personId) throws EventNotFoundException, UserNotExistsException, EventNotOwnedException {
        this.checkEventOwnership(eventId);
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person person = this.personRepository.findById(personId).orElseThrow(UserNotExistsException::new);

        person.getParticipatedEvents().removeIf((x) -> x.getId().equals(eventId));
        event.getParticipants().removeIf((x) -> x.getId().equals(personId));

        this.eventRepository.save(event);
        this.personRepository.save(person);
    }

    public void deleteEvent(Long eventId) throws UserNotExistsException, EventNotOwnedException {
        this.checkEventOwnership(eventId);
        this.eventRepository.deleteById(eventId);
    }

    public void changeProperty(Long id, String value, String field) throws EventNotFoundException, UserNotExistsException, EventNotOwnedException {
        this.checkEventOwnership(id);
        Event event = this.eventRepository.findById(id).orElseThrow(EventNotFoundException::new);

        switch (field) {
            case "title":
                event.setTitle(value);
                break;
            case "description":
                event.setDescription(value);
                break;
            default:
                System.out.println("Mapping not found");
                break;
        }
        this.eventRepository.save(event);
    }

    public void addLink(Long eventId, LinkRequest link) throws EventNotFoundException, UserNotExistsException, EventNotPatricipated {
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person person = this.getContextUser();

        Set<Person> eventParticipants = event.getParticipants();
        if (eventParticipants == null)
            throw new EventNotPatricipated();

        boolean isParticipant = eventParticipants.stream().anyMatch((x) -> x.getId().equals(person.getId()));
        if (!isParticipant)
            throw new EventNotPatricipated();

        Set<Link> links = event.getLinks();

        if (links == null)
            links = new HashSet<>();

        links.add(new Link(link.getLink()));

        event.setLinks(links);
        this.eventRepository.save(event);
    }

    public void changeVote(Long eventId, Long linkId, Vote v) throws EventNotFoundException, UserNotExistsException, EventNotPatricipated, LinkNotExists, LinkNotInEvent {
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person person = this.getContextUser();
        Set<Person> eventParticipants = event.getParticipants();

        if(eventParticipants == null)
            throw new EventNotPatricipated();

        boolean isParticipant = eventParticipants.stream().anyMatch((x) -> x.getId().equals(person.getId()));

        if (!isParticipant)
            throw new EventNotPatricipated();

        if (event.getLinks() != null && event.getLinks().stream().noneMatch((x) -> x.getId().equals(linkId)))
            throw new LinkNotInEvent();

        Link link = this.linkRepository.findById(linkId).orElseThrow(LinkNotExists::new);

        Set<Link> upvotes = person.getUpVotes();
        Set<Link> downvotes = person.getDownVotes();

        if (v.equals(Vote.upvote)) {
            if (downvotes != null)
                downvotes.removeIf((x) -> x.getId().equals(linkId));
            if (upvotes == null)
                upvotes = new HashSet<>();

            upvotes.add(link);
        } else {
            if (upvotes != null)
                upvotes.removeIf((x) -> x.getId().equals(linkId));
            if (downvotes == null)
                downvotes = new HashSet<>();

            downvotes.add(link);
        }

        person.setUpVotes(upvotes);
        person.setDownVotes(downvotes);
        this.personRepository.save(person);
    }
}
