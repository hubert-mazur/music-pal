package com.hm.zti.fis.musicpal;

import com.hm.zti.fis.musicpal.exceptions.person.UserExistsException;
import com.hm.zti.fis.musicpal.exceptions.person.UserNotExistsException;
import com.hm.zti.fis.musicpal.login.LoginRequest;
import com.hm.zti.fis.musicpal.login.LoginService;
import com.hm.zti.fis.musicpal.person.Person;
import com.hm.zti.fis.musicpal.person.PersonRepository;
import com.hm.zti.fis.musicpal.person.PersonRole;
import com.hm.zti.fis.musicpal.person.PersonService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithUserDetails;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonTest {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;
    private Person person;

    @Autowired
    private LoginService loginService;


    @BeforeAll
    void setup() throws UserExistsException {
        this.personRepository.deleteAll();
        this.person = new Person("Test", "User", "test@localhost.com", "test", PersonRole.USER);
        this.personService.signUp(this.person);
    }

    @AfterAll
    void cleanup() {
        this.personRepository.deleteAll();
    }

    @Test
    void createNewUser() {
        Assertions.assertTrue(this.personRepository.existsByEmail(this.person.getEmail()));
    }

    @Test
    void throwUserExistsException() {

        Assertions.assertThrows(UserExistsException.class, () ->
                this.personService.signUp(this.person)
        );
    }

    @Test
    void login() {
        Assertions.assertDoesNotThrow(() -> this.loginService.login(new LoginRequest(this.person.getEmail(), "test")));
    }

    @Test
    @WithUserDetails("test@localhost.com")
    void changeFirstName() throws UserNotExistsException {
        this.personService.changeUserDetails("Bob", "firstName");
        this.person = this.personRepository.getFirstByEmail(this.person.getEmail()).orElseThrow(UserNotExistsException::new);
        Assertions.assertEquals("Bob", this.person.getFirstName());
    }



}
