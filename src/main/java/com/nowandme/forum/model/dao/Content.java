package com.nowandme.forum.model.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nowandme.forum.model.ContentType;
import com.nowandme.forum.model.MediaType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity
@Data
public class Content {
    @Id
    private Integer contentId;
    private ContentType contentType;
    private String creator;
    private Boolean isAnonymous;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime creationTimestamp;
    private String title = null; //null in case of comment
    private String parentPostId = null; //null in case of post

    // Considering supported type of Post/Comment as text, image and video - mediaType will have text content or URL of image/video storage
    private MediaType mediaType;
    private String media; //Text or the URL
}
