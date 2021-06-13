package com.hm.zti.fis.musicpal.event;

import com.hm.zti.fis.musicpal.exceptions.event.EventNotOwnedException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotPatricipated;
import com.hm.zti.fis.musicpal.exceptions.event.EventReadOnly;
import com.hm.zti.fis.musicpal.exceptions.link.LinkNotExists;
import com.hm.zti.fis.musicpal.exceptions.link.LinkNotInEvent;
import com.hm.zti.fis.musicpal.exceptions.person.UserNotExistsException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotFoundException;
import com.hm.zti.fis.musicpal.link.Link;
import com.hm.zti.fis.musicpal.link.LinkRepository;
import com.hm.zti.fis.musicpal.link.LinkRequest;
import com.hm.zti.fis.musicpal.person.Person;
import com.hm.zti.fis.musicpal.person.PersonBasicInfo;
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
        Set<Event> events = person.getOwnedEvents();
        events.stream().filter((event ->
                eventId.equals(event.getId())
        )).findAny().orElseThrow(EventNotOwnedException::new);
    }

    public void modifyEvent(Long eventId, EventRequest eventRequest) throws EventNotFoundException, UserNotExistsException, EventReadOnly {
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        List<Person> participants = event.getParticipants();

        if (event.getClosed())
            throw new EventReadOnly();

//        for (Person person : participants) {
//            List<Event> participated = person.getParticipatedEvents();
//
//            if (!eventRequest.getParticipants().stream().anyMatch((x) -> x.equals(person.getId()))) {
//                participated.remove(event);
//            }
//            participants.remove(person);
//
//            person.setParticipatedEvents(participated);
//            event.setParticipants(participants);
//        }
//
//        this.eventRepository.save(event);


        for (Person person : participants) {
            List<Event> participated = person.getParticipatedEvents();
            participated.remove(event);
            person.setParticipatedEvents(participated);
        }

        participants.clear();
        event.setParticipants(participants);

        event.setTitle(eventRequest.getTitle());
        event.setDescription(event.getDescription());

        this.eventRepository.save(event);


        for (Long id : eventRequest.getParticipants()) {
            this.eventRepository.setParticipant(event.getId(), id);
        }

    }

    public void addEvent(EventRequest eventRequest) throws UserNotExistsException {
        Event event = new Event(eventRequest.getTitle(), eventRequest.getDescription());
        this.eventRepository.save(event);

        Person creator = this.getContextUser();

        for (Long id : eventRequest.getParticipants())
            this.eventRepository.setParticipant(event.getId(), id);

        this.eventRepository.setOwnership(event.getId(), creator.getId());
    }

    public void addPersonToEvent(Long eventId, Long personId) throws UserNotExistsException, EventNotFoundException, EventNotOwnedException, EventReadOnly {
        this.checkEventOwnership(eventId);
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person p = this.personRepository.findById(personId).orElseThrow(UserNotExistsException::new);

        if (event.getClosed())
            throw new EventReadOnly();

        List<Person> participants = event.getParticipants();
        List<Event> participated = p.getParticipatedEvents();

        if (participated == null)
            participated = new ArrayList<>();

        if (participants == null) {
            participants = new ArrayList<>();
        }

        participated.add(event);
        p.setParticipatedEvents(participated);
        participants.add(p);
        event.setParticipants(participants);
        this.eventRepository.save(event);

    }

    public void removePersonFromEvent(Long eventId, Long personId) throws EventNotFoundException, UserNotExistsException, EventNotOwnedException, EventReadOnly {
        this.checkEventOwnership(eventId);
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person person = this.personRepository.findById(personId).orElseThrow(UserNotExistsException::new);

        if (event.getClosed())
            throw new EventReadOnly();

        person.getParticipatedEvents().removeIf((x) -> x.getId().equals(eventId));
        event.getParticipants().removeIf((x) -> x.getId().equals(personId));

        this.eventRepository.save(event);
        this.personRepository.save(person);
    }

    public void deleteEvent(Long eventId) throws UserNotExistsException, EventNotOwnedException {
        this.checkEventOwnership(eventId);
        this.eventRepository.deleteById(eventId);
    }

    public void changeProperty(Long id, String value, String field) throws EventNotFoundException, UserNotExistsException, EventNotOwnedException, EventReadOnly {
        this.checkEventOwnership(id);
        Event event = this.eventRepository.findById(id).orElseThrow(EventNotFoundException::new);

        if (event.getClosed())
            throw new EventReadOnly();

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

    public void addLink(Long eventId, LinkRequest link) throws EventNotFoundException, UserNotExistsException, EventNotPatricipated, EventReadOnly {
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person person = this.getContextUser();

        if (event.getClosed())
            throw new EventReadOnly();

        List<Person> eventParticipants = event.getParticipants();
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

    public void changeVote(Long eventId, Long linkId, Vote v) throws EventNotFoundException, UserNotExistsException, EventNotPatricipated, LinkNotExists, LinkNotInEvent, EventReadOnly {
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person person = this.getContextUser();
        List<Person> eventParticipants = event.getParticipants();

        if (event.getClosed())
            throw new EventReadOnly();

        if (eventParticipants == null || eventParticipants.stream().noneMatch((x) -> x.getId().equals(person.getId())))
            throw new EventNotPatricipated();

        if (event.getLinks() == null || event.getLinks().stream().noneMatch((x) -> x.getId().equals(linkId)))
            throw new LinkNotInEvent();

        Link link = this.linkRepository.findById(linkId).orElseThrow(LinkNotExists::new);

        Set<Link> upvotes = person.getUpVotes();
        Set<Link> downvotes = person.getDownVotes();

        if (v.equals(Vote.upvote)) {
            if (downvotes != null)
                downvotes.removeIf((x) -> x.getId().equals(linkId));
            if (upvotes == null)
                upvotes = new HashSet<>();

            if (upvotes.stream().noneMatch((x) -> x.getId().equals(linkId)))
                upvotes.add(link);
        } else {
            if (upvotes != null)
                upvotes.removeIf((x) -> x.getId().equals(linkId));
            if (downvotes == null)
                downvotes = new HashSet<>();

            if (downvotes.stream().noneMatch((x) -> x.getId().equals(linkId)))
                downvotes.add(link);
        }

        person.setDownVotes(downvotes);
        person.setUpVotes(upvotes);
//        this.personRepository.save(person);
        this.personRepository.save(person);
    }

    public List<Event> getEvents() throws UserNotExistsException {
        Person person = this.getContextUser();
        List<Event> events = person.getParticipatedEvents();
        if (events != null)
            events.forEach(event -> event.setParticipants(null));
        return events;
    }

    public Set<PersonBasicInfo> getEventParticipants(Long eventId) throws EventNotFoundException {
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Set<PersonBasicInfo> participantsBasicInfo = new HashSet<>();
        List<Person> participants = event.getParticipants();

        if (participants == null)
            return null;

        participants.forEach((participant) -> participantsBasicInfo.add(this.personRepository.findPersonById(participant.getId())));

        return participantsBasicInfo;
    }

    public Event getSelectedEvent(Long eventId) throws UserNotExistsException, EventNotFoundException, EventNotPatricipated {
        Person user = this.getContextUser();
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        List<Person> participants = event.getParticipants();

        if (participants == null)
            throw new EventNotPatricipated();

        if (participants.stream().noneMatch((x) -> x.getId().equals(user.getId())))
            throw new EventNotPatricipated();

        event.setParticipants(null);
        return event;
    }

    public void deleteLink(Long eventId, Long linkId) throws EventNotFoundException, UserNotExistsException, EventNotPatricipated, EventReadOnly {
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person person = this.getContextUser();

        if (event.getClosed())
            throw new EventReadOnly();

        if (event.getParticipants().stream().noneMatch((x) -> x.getId().equals(person.getId())))
            throw new EventNotPatricipated();

        this.linkRepository.deleteById(linkId);

    }

    public void closeEvent(Long eventId) throws EventNotFoundException, UserNotExistsException {
        Event event = this.eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        Person user = this.getContextUser();
        if (event.getParticipants().stream().noneMatch((x) -> x.getId().equals(user.getId())))
            throw new EventNotFoundException();

        this.eventRepository.setClosed(eventId);
    }

}
