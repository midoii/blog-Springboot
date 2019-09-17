package com.blog.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Table(name = "category")
public class Category {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private Long aid;

    @Id
    @Column(name = "id")
    private String categoryId;

    @Column(name = "name")
    private String categoryName;
    private Long createTime;
    private Long updateTime;
    private String status;
    private Long articleCount;
    private String canDel;
}
