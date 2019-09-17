package com.blog.item.pojo;


import lombok.Data;

/*
* 用于返回的article模板
* */

@Data
public class ArticleTemp {

    private String id;
    private String title;
    private String cover;
    private Integer pageview;
    private Integer status;
    private Integer isEncrypt;

    private String categoryName;
    private Long createTime;
    private Long deleteTime;
    private Long updateTime;
    private Long publishTime;
}
