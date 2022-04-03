package com.nowandme.forum.repository;

import com.nowandme.forum.model.dao.Content;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends CrudRepository<Content, Integer> {
    Content findByContentId(String contentId);
}
