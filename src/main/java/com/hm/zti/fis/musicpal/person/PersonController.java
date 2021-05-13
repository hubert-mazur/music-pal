package com.hm.zti.fis.musicpal.person;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/person")
@Service
@AllArgsConstructor
public class PersonController {
    private final PersonRepository personRepository;

    @GetMapping(path = "/hello")
    public String hello() {
        return "Hello, this is protected site";
    }

    @DeleteMapping()
    public void deleteAccount() {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<Person> p = personRepository.getFirstByEmail(email);
        this.personRepository.delete(p.get());
    }

    // TODO: Implement PATCH mappings
}
