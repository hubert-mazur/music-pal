package com.hm.zti.fis.musicpal.event;

import com.hm.zti.fis.musicpal.exceptions.event.EventNotOwnedException;
import com.hm.zti.fis.musicpal.exceptions.person.UserNotExistsException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotFoundException;
import com.hm.zti.fis.musicpal.person.Person;
import com.hm.zti.fis.musicpal.person.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;

    private void checkEventOwnership(Long eventId) throws UserNotExistsException, EventNotOwnedException {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Person person = this.personRepository.getFirstByEmail(email).orElseThrow(UserNotExistsException::new);

        List<Event> events = person.getOwnedEvents();
        events.stream().filter((event ->
                eventId.equals(event.getId())
        )).findAny().orElseThrow(EventNotOwnedException::new);
    }

    public void addEvent(EventRequest eventRequest) throws UserNotExistsException {
        Event event = new Event(eventRequest.getTitle(), eventRequest.getDescription());
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Person person = this.personRepository.getFirstByEmail(email).orElseThrow(UserNotExistsException::new);

        List<Event> e = person.getOwnedEvents();
        if (e.isEmpty())
            e = new ArrayList<>();

        e.add(event);
        person.setOwnedEvents(e);
        this.personRepository.save(person);
    }

    public void deleteEvent(Long eventId) throws UserNotExistsException, EventNotOwnedException {
        this.checkEventOwnership(eventId);
        this.eventRepository.deleteById(eventId);
    }

    public void changeProperty(Long id, String value, String field) throws EventNotFoundException, UserNotExistsException, EventNotOwnedException {
        this.checkEventOwnership(id);
        Event event = this.eventRepository.findById(id).orElseThrow(EventNotFoundException::new);

        switch(field){
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
}
