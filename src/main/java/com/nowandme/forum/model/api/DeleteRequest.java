package com.nowandme.forum.model.api;

import com.nowandme.forum.model.ContentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRequest {
    private String userId;
    private ContentType contentType;
    private Integer contentId;
}
