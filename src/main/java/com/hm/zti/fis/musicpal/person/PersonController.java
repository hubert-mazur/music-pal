package com.hm.zti.fis.musicpal.person;

import com.hm.zti.fis.musicpal.exceptions.person.UserNotExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/person")
@Service
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping()
    public List<PersonBasicInfo> hello() throws UserNotExistsException {
        return  this.personService.getPeople();
    }

    @GetMapping(path = "/identity")
    public PersonBasicInfo identity() throws UserNotExistsException {
        return this.personService.getIdentity();
    }


    @DeleteMapping()
    public void deleteAccount() {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        this.personService.deletePerson(email);
    }

    @PatchMapping("/firstname")
    public void changeFirstName(@RequestBody PatchRequest request) throws UserNotExistsException {
        this.personService.changeUserDetails(request.getValue(), "firstName");
    }

    @PatchMapping("/lastname")
    public void changeLastName(@RequestBody PatchRequest request) throws UserNotExistsException {
        this.personService.changeUserDetails(request.getValue(), "lastName");
    }

    @PatchMapping("/password")
    public void changePassword(@RequestBody PatchRequest request) throws UserNotExistsException {
        this.personService.changeUserDetails(request.getValue(), "password");
    }

}
