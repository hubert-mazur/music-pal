package com.hm.zti.fis.musicpal;

import com.hm.zti.fis.musicpal.event.Event;
import com.hm.zti.fis.musicpal.event.EventRepository;
import com.hm.zti.fis.musicpal.event.EventRequest;
import com.hm.zti.fis.musicpal.event.EventService;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotFoundException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotOwnedException;
import com.hm.zti.fis.musicpal.exceptions.event.EventNotPatricipated;
import com.hm.zti.fis.musicpal.exceptions.event.EventReadOnly;
import com.hm.zti.fis.musicpal.exceptions.person.UserExistsException;
import com.hm.zti.fis.musicpal.exceptions.person.UserNotExistsException;
import com.hm.zti.fis.musicpal.link.Link;
import com.hm.zti.fis.musicpal.link.LinkRequest;
import com.hm.zti.fis.musicpal.person.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class EventTest {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;

    @Autowired
    PersonService personService;

    @Autowired
    PersonRepository personRepository;

    @BeforeAll
    void setUser() throws UserExistsException {
            this.personService.signUp(new Person("test", "test", "test@localhost.com", "test", PersonRole.USER));

    }

    @AfterEach
    void cleanUp() {
        this.eventRepository.deleteAll();

    }

    @AfterAll
    void deletePersonRepository() {
        this.personRepository.deleteAll();
    }

    @Test
    @WithUserDetails("test@localhost.com")
    void createEvent() throws UserNotExistsException {
        Set<Long> participants = new HashSet<>();
        Person p = this.personRepository.getFirstByEmail(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).orElseThrow(UserNotExistsException::new);
        participants.add(p.getId());

        this.eventService.addEvent(new EventRequest("test event", "test description", participants));

        Assertions.assertTrue(this.eventRepository.findAll().size() != 0);

    }

    @Test
    @WithUserDetails("test@localhost.com")
    void deleteEvent() throws UserNotExistsException, EventNotOwnedException {
        Event e;
        this.eventService.addEvent(new EventRequest("title", "description", null));
        e = this.eventService.getEvents().get(0);

        Long deletedId = e.getId();

        this.eventService.deleteEvent(deletedId);

        List<Event> events = this.eventService.getEvents();

        Assertions.assertTrue(events == null || events.stream().noneMatch(ev -> ev.getId().equals(deletedId)));
    }

    @Test
    @WithUserDetails("test@localhost.com")
    void addPersonToEvent() throws UserNotExistsException, EventNotFoundException {
        Event e;
        this.eventService.addEvent(new EventRequest("title", "description", null));
        e = this.eventService.getEvents().get(0);

        try {
            this.personService.signUp(new Person("test2", "test2", "test2@localhost.com", "test", PersonRole.USER));
        } catch (Exception ignored) {

        }

        Person p = this.personRepository.getFirstByEmail("test2@localhost.com").orElseThrow(UserNotExistsException::new);

        this.eventRepository.setParticipant(e.getId(), p.getId());
        e = this.eventService.getEvents().get(0);


        Set<PersonBasicInfo> participants = this.eventService.getEventParticipants(e.getId());

        Assertions.assertTrue(participants != null && participants.stream().anyMatch(pc -> pc.getId().equals(p.getId())));

    }

    @Test
    @WithUserDetails("test@localhost.com")
    void removePersonFromEvent() throws UserNotExistsException, EventNotFoundException, EventReadOnly, EventNotOwnedException {
        Event e;
        this.eventService.addEvent(new EventRequest("title", "description", null));
        e = this.eventService.getEvents().get(0);

        try {
            this.personService.signUp(new Person("test2", "test2", "test2@localhost.com", "test", PersonRole.USER));
        } catch (Exception ignored) {

        }

        Person p = this.personRepository.getFirstByEmail("test2@localhost.com").orElseThrow(UserNotExistsException::new);

        this.eventRepository.setParticipant(e.getId(), p.getId());

        this.eventService.removePersonFromEvent(e.getId(), p.getId());

        Set<PersonBasicInfo> participants = this.eventService.getEventParticipants(e.getId());

        Assertions.assertTrue(participants != null && participants.stream().noneMatch(pc -> pc.getId().equals(p.getId())));

    }


    @Test
    @WithUserDetails("test@localhost.com")
    void addLinkToEvent() throws UserNotExistsException, EventNotFoundException, EventReadOnly, EventNotPatricipated {
        Event e;
        this.eventService.addEvent(new EventRequest("title", "description", null));
        e = this.eventService.getEvents().get(0);


        this.eventService.addLink(e.getId(), new LinkRequest("https://www.youtube.com/watch?v=2Q_ZzBGPdqE"));
        e = this.eventService.getEvents().get(0);
        Set<Link> links = e.getLinks();

        Assertions.assertTrue(links != null && links.stream().anyMatch(l -> l.getLink().equals("https://www.youtube.com/watch?v=2Q_ZzBGPdqE")));
    }

    @Test
    @WithUserDetails("test@localhost.com")
    void modifyEventReadOnlyThrowsException() throws UserNotExistsException, EventNotFoundException {
        Event e;
        this.eventService.addEvent(new EventRequest("title", "description", null));
        e = this.eventService.getEvents().get(0);

        this.eventService.closeEvent(e.getId());

        Assertions.assertThrows(EventReadOnly.class, ()-> this.eventService.modifyEvent(e.getId(), new EventRequest("new title", "new description", null)));

    }
}
