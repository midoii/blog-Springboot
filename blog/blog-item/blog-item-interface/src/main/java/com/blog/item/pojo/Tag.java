package com.blog.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Table(name = "tag")
public class Tag {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private Long aid;

    @Id
    @Column(name = "id")
    private String tagId;

    @Column(name = "name")
    private String tagName;
    private Long createTime;
    private Long updateTime;
    private String status;
    private Long articleCount;

}
