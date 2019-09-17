package com.blog.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Table(name = "admin")
@Data
public class Admin {


    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private Long aid;

    @Id
    private String userId;
    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String salt;

    @JsonIgnore
    private String accessToken;

    @JsonIgnore
    private Long tokenExpiresIn;

    @JsonIgnore
    private Long createTime;

    @JsonIgnore
    private Integer status;
    private Long lastLoginTime;

    @Transient
    private TokenTemplate token;
}
