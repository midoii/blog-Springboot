package com.blog.item.pojo;

import lombok.Data;

@Data
public class BlogInfo {


    private String blogName;
    private String avatar;
    private String sign;
    private String github;
    private Integer articleCount;
    private Integer categoryCount;
    private Integer tagCount;
}
