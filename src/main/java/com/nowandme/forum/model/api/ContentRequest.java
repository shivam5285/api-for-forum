package com.nowandme.forum.model.api;

import com.nowandme.forum.model.ContentType;
import com.nowandme.forum.model.MediaType;
import lombok.Data;

@Data
public class ContentRequest {
    private ContentType contentType;
    private String userId;
    private Boolean isAnonymous = Boolean.FALSE;
    private String title;
    private String parentPostId;
    // Considering supported type of Post/Comment as text, image and video - mediaType will have text content or URL of image/video storage
    private MediaType mediaType;
    private String media; //Text or the URL
}
