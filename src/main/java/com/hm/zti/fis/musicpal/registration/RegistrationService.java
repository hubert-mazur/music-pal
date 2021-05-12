package com.hm.zti.fis.musicpal.registration;

import com.hm.zti.fis.musicpal.exceptions.person.UserExistsException;
import com.hm.zti.fis.musicpal.person.Person;
import com.hm.zti.fis.musicpal.person.PersonRole;
import com.hm.zti.fis.musicpal.person.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final PersonService personService;

    public String register(RegistrationRequest request) throws UserExistsException {
        boolean isEmailValid = emailValidator.test(request.getEmail());
        // TODO: Implement exception for not valid email
        if (!isEmailValid) {
            throw new IllegalStateException("Email is not valid");
        }
        return personService.signUp(new Person(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), PersonRole.USER));

    }
}
