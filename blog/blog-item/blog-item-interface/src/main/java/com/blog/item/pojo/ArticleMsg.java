package com.blog.item.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleMsg {

    private Article article;
    private TagCategoryList category;
    private List<TagCategoryList> tags;
}
