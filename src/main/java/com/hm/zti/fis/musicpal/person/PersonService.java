package com.hm.zti.fis.musicpal.person;

import com.hm.zti.fis.musicpal.exceptions.person.UserExistsException;
import com.hm.zti.fis.musicpal.exceptions.person.login.InvalidCredential;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            throw new UserExistsException("User with email " + person.getEmail() + "exists");
        }

        String encodedPassword = this.bCryptPasswordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);

        this.personRepository.save(person);

        return person.getEmail();
    }

    public Boolean checkLoginCredentials(String email, String password) throws InvalidCredential {
        Optional<Person> p = personRepository.getFirstByEmail(email);

        if (!p.isPresent() || !bCryptPasswordEncoder.matches(password, p.get().getPassword()))
            throw new InvalidCredential("Invalid email or password");

        return true;
    }

}
