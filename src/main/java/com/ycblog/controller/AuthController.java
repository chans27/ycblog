package com.ycblog.controller;

import com.ycblog.domain.User;
import com.ycblog.exception.InvalidSIgnInInformation;
import com.ycblog.repository.UserRepository;
import com.ycblog.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public User login(@RequestBody Login login) {
        //find in DB
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSIgnInInformation::new);
        //token
        return user;
    }
}
