package com.hm.zti.fis.musicpal.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;


@Service
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String email) {
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);

    }
}
