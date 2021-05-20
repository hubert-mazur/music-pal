package com.hm.zti.fis.musicpal.login;

import com.hm.zti.fis.musicpal.exceptions.login.InvalidCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/login")
@Service
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping()
    public String login(@RequestBody LoginRequest loginRequest) throws InvalidCredential {

        return loginService.login(loginRequest);
    }

}
