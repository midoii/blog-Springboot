package com.blog.common.back;


import com.blog.item.pojo.Comments;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentTemplate {

    private int count;
    private List<Comments> list;
}
