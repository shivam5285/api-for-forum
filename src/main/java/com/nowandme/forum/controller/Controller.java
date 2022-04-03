package com.nowandme.forum.controller;

import com.nowandme.forum.exception.AuthenticationException;
import com.nowandme.forum.model.api.LoginRequest;
import com.nowandme.forum.model.api.LoginResponse;
import com.nowandme.forum.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/user/authenticate")
    public ResponseEntity<LoginResponse> generateAuthToken(@RequestBody LoginRequest loginRequest) throws AuthenticationException {
        final String jwt = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
