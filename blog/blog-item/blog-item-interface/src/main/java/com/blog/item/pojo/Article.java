package com.blog.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "article")
@Data
public class Article {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private Long aid;

    @Id
    private String id;
    private String title;
    private String categoryId;
    private Long createTime;
    private Long deleteTime;
    private Long updateTime;
    private Long publishTime;
    private Integer status;
    private String content;
    private String htmlContent;
    private String cover;
    private String subMessage;
    private Integer pageview;
    private Integer isEncrypt;

}
