package com.nowandme.forum.controller;

import com.nowandme.forum.exception.AuthenticationException;
import com.nowandme.forum.model.api.*;
import com.nowandme.forum.model.dao.Content;
import com.nowandme.forum.service.AuthenticationService;
import com.nowandme.forum.service.ContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Slf4j
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
        log.info("Processing login request with X-Request-ID " + reqId);
        final String jwt = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @Description("To create a new post or comment")
    @PostMapping(value = "/user/new/content", produces = "application/json")
    public ResponseEntity<Content> createPost(
            @RequestHeader(name = "X-Request_ID", required = true) String reqId,
            @RequestHeader(name = "JWT", required = true) String jwt,
            @RequestBody ContentRequest contentRequest) throws SQLException {
        log.info("Processing create request with X-Request-ID " + reqId);
        final Content content = contentService.createNewPost(contentRequest, jwt);
        return ResponseEntity.ok(content);
    }

    @PostMapping(value = "/user/fetch/content", produces = "application/json")
    public ResponseEntity<FetchResponse> fetchUserContent(
            @RequestHeader(name = "X-Request_ID", required = true) String reqId,
            @RequestHeader(name = "JWT", required = true) String jwt,
            @RequestBody FetchRequest fetchRequest) {
        log.info("Processing fetch request with X-Request-ID " + reqId);
        List<Content> contentList = contentService.fetchUserPost(fetchRequest, jwt);
        return ResponseEntity.ok(new FetchResponse(contentList.size(), contentList));
    }

    @PostMapping(value = "/user/delete/content", produces = "application/json")
    public ResponseEntity<DeleteResponse> deleteUserContent(
            @RequestHeader(name = "X-Request_ID", required = true) String reqId,
            @RequestHeader(name = "JWT", required = true) String jwt,
            @RequestBody DeleteRequest deleteRequest) {
        log.info("Processing delete request with X-Request-ID " + reqId);
        Boolean isDeleted = contentService.deleteUserPost(deleteRequest, jwt);
        return ResponseEntity.ok(new DeleteResponse(isDeleted));
    }

}
