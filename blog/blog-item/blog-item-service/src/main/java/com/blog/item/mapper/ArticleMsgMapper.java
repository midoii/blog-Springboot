package com.blog.item.mapper;

import com.blog.item.pojo.Article;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMsgMapper {


    @Select("select * from article where id=#{id}")
    Article queryArticle(String id);

    @Select("select tag_id from article_tag_mapper where article_id=#{id}")
    List<String> queryTagsById(String id);
}
