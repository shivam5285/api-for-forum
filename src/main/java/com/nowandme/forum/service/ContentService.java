package com.nowandme.forum.service;

import com.nowandme.forum.model.ContentType;
import com.nowandme.forum.model.dao.Content;
import com.nowandme.forum.model.api.ContentRequest;
import com.nowandme.forum.repository.ContentRepository;
import com.nowandme.forum.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ContentService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ContentRepository contentRepository;

    public Content createNewPost(ContentRequest contentRequest, String jwt) throws SQLException {
        try {
            Boolean isJwtValid = jwtUtil.validateToken(jwt);
            if(!isJwtValid)
                throw new JwtException("Invalid Token");
        } catch (Exception e) {
            throw new JwtException("Invalid Token");
        }
        log.debug("JWT verified for user " + contentRequest.getUserId());
        Content content = contentRepository.save(extracted(contentRequest));
        if(content == null || content.getContentId()==null)
            throw new SQLException("Could not persist in DB");
        log.debug("Content saved in DB with content ID " + content.getContentId());
        return content;
    }

    private Content extracted(ContentRequest contentRequest) {
        Content content = new Content();
        content.setContentType(contentRequest.getContentType());
        content.setCreator(contentRequest.getUserId());
        content.setIsAnonymous(contentRequest.getIsAnonymous());
        content.setCreationTimestamp(LocalDateTime.now());
        if(contentRequest.getContentType().equals(ContentType.POST))
            content.setTitle(contentRequest.getTitle());
        else if(contentRequest.getContentType().equals(ContentType.COMMENT))
            content.setParentPostId(contentRequest.getParentPostId());
        content.setMediaType(contentRequest.getMediaType());
        content.setMedia(contentRequest.getMedia());
        return content;
    }
}
