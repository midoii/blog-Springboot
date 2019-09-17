package com.blog.item.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "pages")
@NoArgsConstructor
public class Pages {

    @JsonIgnore
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Id
    private String type;
    private String md;
    private String html;
}
