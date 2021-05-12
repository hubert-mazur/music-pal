package com.hm.zti.fis.musicpal.person;

import com.hm.zti.fis.musicpal.exceptions.person.UserExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonService implements UserDetailsService {
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return personRepository.getFirstByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email" + email + " does not exists"));
    }

    public String signUp(Person person) throws UserExistsException {


        boolean userExists = this.personRepository.getFirstByEmail(person.getEmail()).isPresent();

        if (userExists) {
            System.out.println("Email is invalid throwing an exception");
            throw new UserExistsException("User with email " + person.getEmail() + "exists");
        }

        String encodedPassword = this.bCryptPasswordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);

        this.personRepository.save(person);

        return person.getEmail();
    }
}
