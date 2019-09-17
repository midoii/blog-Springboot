package com.blog.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "friends")
public class Friends {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long aid;
    private String friendId;
    private String name;
    private String url;
    private Long createTime;
    private Long deleteTime;
    private Integer status;
    private Long typeId;
}
