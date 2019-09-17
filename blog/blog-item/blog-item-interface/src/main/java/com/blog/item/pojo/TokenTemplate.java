package com.blog.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenTemplate {

    private String accessToken;
    private Long tokenExpiresIn;
    private int exp;
}
