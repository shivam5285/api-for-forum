package com.nowandme.forum.model.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Credential {
    @Id
    private String userId;
    private String hashedPassword;
}
