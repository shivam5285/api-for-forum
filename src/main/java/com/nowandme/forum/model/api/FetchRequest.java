package com.nowandme.forum.model.api;

import com.nowandme.forum.model.ContentType;
import com.nowandme.forum.model.Mode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchRequest {
    private String userId;
    private ContentType contentType;
    private Mode mode;
}
