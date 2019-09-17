package com.blog.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "blog_config")
public class BlogConfig {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String blogName;
    private String avatar;
    private String sign;
    private String wxpayQrcode;
    private String alipayQrcode;
    private String github;
    @Transient
    private Boolean hadOldPassword;
}
