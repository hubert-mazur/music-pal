package com.hm.zti.fis.musicpal.person;

import com.hm.zti.fis.musicpal.exceptions.person.UserExistsException;
import com.hm.zti.fis.musicpal.exceptions.person.UserNotExistsException;
import com.hm.zti.fis.musicpal.exceptions.login.InvalidCredential;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public Person getUserInfo() throws UserNotExistsException {
        Person person = null;
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        person = this.personRepository.getFirstByEmail(email).orElseThrow(UserNotExistsException::new);
        person.setPassword(null);
        return person;
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

    public void deletePerson(String email) {
        this.personRepository.deleteByEmail(email);
    }

    public void changeUserDetails(String value, String detail) throws UserNotExistsException {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<Person> p = personRepository.getFirstByEmail(email);

        if (p.isPresent()) {
            Person person = p.get();

            switch (detail) {
                case "firstName":
                    person.setFirstName(value);
                    break;
                case "lastName":
                    person.setLastName(value);
                    break;
                case "password":
                    person.setPassword(this.bCryptPasswordEncoder.encode(value));
                    break;
                default:
                    System.out.println("field is not recognized");
                    break;
            }
            this.personRepository.save(person);
        } else {
            throw new UserNotExistsException();
        }
    }
}
