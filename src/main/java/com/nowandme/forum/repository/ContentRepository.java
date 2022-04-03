package com.nowandme.forum.repository;

import com.nowandme.forum.model.ContentType;
import com.nowandme.forum.model.dao.Content;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends CrudRepository<Content, Integer> {
    Content findByContentId(Integer contentId);
    List<Content> findByCreatorAndContentType(String creator, ContentType contentType);
    long deleteByContentId(Integer contendId);
}
