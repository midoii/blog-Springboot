package com.blog.common.back;

import lombok.Data;

import java.util.List;

@Data
public class AboutMeTemplate {

    private String html;
    private List<String> qrcode;
}
