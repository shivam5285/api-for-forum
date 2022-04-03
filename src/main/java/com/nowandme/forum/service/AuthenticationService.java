package com.nowandme.forum.service;

import com.nowandme.forum.exception.AuthenticationException;
import com.nowandme.forum.model.api.LoginRequest;
import com.nowandme.forum.model.dao.Credential;
import com.nowandme.forum.repository.CredentialRepository;
import com.nowandme.forum.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticate(LoginRequest loginRequest) throws AuthenticationException {
        Credential credential = credentialRepository.findByUserId(loginRequest.getUserId());
        if (credential == null)
            throw new AuthenticationException("Invalid User");
        if (credential.getHashedPassword().equals(loginRequest.getHashedPassword()))
            throw new AuthenticationException("Wrong password");

        final String jwt = jwtUtil.generateToken(loginRequest.getUserId());
        return jwt;
    }
}
