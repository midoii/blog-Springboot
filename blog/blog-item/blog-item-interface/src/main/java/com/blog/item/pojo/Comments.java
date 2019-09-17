package com.blog.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "comments")
public class Comments {


    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Id
    private String articleId;
    private Integer parentId;
    private Integer replyId;
    private String name;
    private String email;
    private String content;
    private String sourceContent;
    private Long createTime;
    private Long deleteTime;
    private Integer status;
    private Integer isAuthor;

    @Transient
    private List<Comments> children;
}
