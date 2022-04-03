package com.nowandme.forum.service;

import com.nowandme.forum.exception.RecordNotFoundException;
import com.nowandme.forum.model.ContentType;
import com.nowandme.forum.model.Mode;
import com.nowandme.forum.model.api.DeleteRequest;
import com.nowandme.forum.model.api.FetchRequest;
import com.nowandme.forum.model.dao.Content;
import com.nowandme.forum.model.api.ContentRequest;
import com.nowandme.forum.model.dao.MaliciousRequestException;
import com.nowandme.forum.repository.ContentRepository;
import com.nowandme.forum.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ContentService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ContentRepository contentRepository;

    /**
     *
     * @param contentRequest
     * @param jwt
     * @return
     *  Return new content with Id
     * @throws SQLException
     */
    public Content createNewPost(ContentRequest contentRequest, String jwt) throws SQLException {
        validateJwt(contentRequest.getUserId(), jwt);

        Content content = contentRepository.save(extracted(contentRequest));
        if(content == null || content.getContentId()==null)
            throw new SQLException("Could not persist in DB");

        log.debug("Content saved in DB with content ID " + content.getContentId());
        return content;
    }

    /**
     *
     * @param fetchRequest
     * @param jwt
     * @return
     *  Return all/self content of given type considering anonymity
     */
    public List<Content> fetchContent(FetchRequest fetchRequest, String jwt) {
        validateJwt(fetchRequest.getUserId(), jwt);
        List<Content> contentList;

        // Returning only user created content
        if(fetchRequest.getMode().equals(Mode.SELF)) {
            contentList = contentRepository.findByCreatorAndContentType(
                    fetchRequest.getUserId(),
                    fetchRequest.getContentType());

            if (contentList == null || contentList.size() == 0)
                throw new RecordNotFoundException("No content exist for user " + fetchRequest.getUserId() +
                        " for content type " + fetchRequest.getContentType());

            log.debug("Found contents of user " + fetchRequest.getUserId() + " from DB for content type " +
                    fetchRequest.getContentType());
            return contentList;
        }

        // Returning all content considering anonymity
        else {
            contentList = contentRepository.findByContentType(
                    fetchRequest.getContentType());

            if (contentList == null || contentList.size() == 0)
                throw new RecordNotFoundException("No content exists for type " + fetchRequest.getContentType());

            log.debug("Found contents from DB for content type " +
                    fetchRequest.getContentType());

            // Hiding creator information for anonymous posts
            for(Content content: contentList) {
                if(content.getIsAnonymous() && !content.getCreator().equals(fetchRequest.getUserId())) {
                    content.setCreator(null);
                }
            }

            log.debug("Creator information hiding for anonymous posts successful");
        }
        return contentList;
    }

    /**
     *
     * @param deleteRequest
     * @param jwt
     * @return
     *  Return true if content is deleted
     */
    public Boolean deleteUserPost(DeleteRequest deleteRequest, String jwt) {
        validateJwt(deleteRequest.getUserId(), jwt);

        Content content = contentRepository.findByContentId(deleteRequest.getContentId());
        if(!content.getCreator().equals(deleteRequest.getUserId()))
            throw new MaliciousRequestException("Trying to delete other user's content");

        long deletedRecord = contentRepository.deleteByContentId(deleteRequest.getContentId());

        if(deletedRecord!=1)
            throw new RecordNotFoundException("No content exist for user " + deleteRequest.getUserId() +
                    " for content id " + deleteRequest.getContentId());

        log.debug("Content deleted for content id " + deleteRequest.getContentId());
        return Boolean.TRUE;
    }

    /**
     *
     * @param userId
     * @param jwt
     */
    private void validateJwt(String userId, String jwt) {
        try {
            Boolean isJwtValid = jwtUtil.validateToken(jwt);
            if(!isJwtValid)
                throw new JwtException("Invalid Token");
        } catch (Exception e) {
            throw new JwtException("Invalid Token");
        }
        log.debug("JWT verified for user " + userId);
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
