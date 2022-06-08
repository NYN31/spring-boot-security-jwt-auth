package com.springsecurity.jpa.controller;

import com.springsecurity.jpa.model.AuthenticationRequest;
import com.springsecurity.jpa.model.AuthenticationResponse;
import com.springsecurity.jpa.service.JwtUtil;
import com.springsecurity.jpa.service.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationManager authenticationManager;

    private final MyUserDetailsService myUserDetailsService;

    private final JwtUtil jwtTokenUtil;

    public HomeResource(AuthenticationManager authenticationManager,
                        MyUserDetailsService myUserDetailsService,
                        JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/")
    public String home() {
        return ("<h1>Welcome to home page!</h1>");
    }

    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome to user page!</h1>");
    }
    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome to admin page!</h1>");
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello world";
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
        throws Exception {
        log.info("Enter into createAuthenticationToken method");
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect Username or password");
        }
        final UserDetails userDetails = myUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
