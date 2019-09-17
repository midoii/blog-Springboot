package com.blog.common.back;


import com.blog.item.pojo.Article;
import com.blog.item.pojo.TagCategoryList;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Archives {

    private Article article;
    private TagCategoryList category;
    private List<TagCategoryList> tags;
}
