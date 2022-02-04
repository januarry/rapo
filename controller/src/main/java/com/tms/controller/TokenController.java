package com.tms.controller;

import com.tms.configuration.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private Environment env;

    @Autowired
    JwtUtils jwtUtils;

    // @GetMapping("/api/authorizationToken")
    // public String generateToken() {

    //     return jwtUtils.generateJwtToken("TMS");

    // }

}
