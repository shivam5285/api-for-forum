package com.nowandme.forum.model.api;

import com.nowandme.forum.model.dao.Content;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FetchResponse {
    private Integer size;
    private List<Content> contentList;
}
