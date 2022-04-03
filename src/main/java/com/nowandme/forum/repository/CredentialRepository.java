package com.nowandme.forum.repository;

import com.nowandme.forum.model.dao.Credential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends CrudRepository<Credential, String> {
    Credential findByUserId(String userId);
}