package com.blog.common.back;

import com.blog.item.pojo.Article;
import com.blog.item.pojo.TagCategoryList;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ArticleInfoTemplate {

    private Article article;
    private TagCategoryList category;
    private List<TagCategoryList> tags;
    private Map qrcode;
    private Map pn;
}
