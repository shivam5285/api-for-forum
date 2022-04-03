package com.nowandme.forum.controller;

import com.nowandme.forum.exception.AuthenticationException;
import com.nowandme.forum.model.dao.Content;
import com.nowandme.forum.model.api.LoginRequest;
import com.nowandme.forum.model.api.LoginResponse;
import com.nowandme.forum.model.api.ContentRequest;
import com.nowandme.forum.service.AuthenticationService;
import com.nowandme.forum.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class Controller {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ContentService contentService;

    @PostMapping(value = "/user/authenticate", produces = "application/json")
    public ResponseEntity<LoginResponse> generateAuthToken(
            @RequestHeader(name = "X-Request_ID", required = true) String reqId,
            @RequestBody LoginRequest loginRequest) throws AuthenticationException {
        final String jwt = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @Description("To create a new post or comment")
    @PostMapping(value = "/user/new/content", produces = "application/json")
    public ResponseEntity<Content> createPost(
            @RequestHeader(name = "X-Request_ID", required = true) String reqId,
            @RequestHeader(name = "JWT", required = true) String jwt,
            @RequestBody ContentRequest contentRequest) throws SQLException {
        final Content content = contentService.createNewPost(contentRequest, jwt);
        return ResponseEntity.ok(content);
    }
}
