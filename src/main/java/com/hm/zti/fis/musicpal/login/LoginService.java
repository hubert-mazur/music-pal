package com.hm.zti.fis.musicpal.login;

import com.hm.zti.fis.musicpal.exceptions.login.InvalidCredential;
import com.hm.zti.fis.musicpal.person.PersonService;
import com.hm.zti.fis.musicpal.security.jwt.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    private final PersonService personService;
    private final JwtTokenUtil jwtTokenUtil;

    public String login(LoginRequest loginRequest) throws InvalidCredential {

        personService.checkLoginCredentials(loginRequest.getEmail(), loginRequest.getPassword());
        return jwtTokenUtil.generateToken(loginRequest.getEmail());
    }
}
