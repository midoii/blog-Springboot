package com.blog.item.mapper;

import com.blog.item.pojo.*;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MultiMapper {

    @Select("SELECT a.id, a.title, a.cover, a.pageview,a.`status`,a.is_encrypt,a.create_time,a.delete_time," +
            "a.update_time,a.publish_time,c.`name` as category_name from article a inner join category c on a.category_id=c.id" +
            " where a.category_id=#{categoryId}")
    Page<ArticleTemp> queryArticleByCategory(String categoryId);


    @Select("SELECT a.id, a.title, a.cover, a.pageview,a.status,a.is_encrypt,a.create_time,a.delete_time," +
            "a.update_time,a.publish_time,c.name AS category_name from article a,article_tag_mapper atm,category c " +
            "where atm.article_id = a.id AND a.category_id = c.id AND atm.tag_id =#{tagId}")
    Page<ArticleTemp> queryArticleByTag(String tagId);


    @Select("select id,name from category where id=#{category}")
    TagCategoryList queryCategory(String category);


    @Select("select id,name from tag where id=ANY(select tag_id as id from article_tag_mapper where article_id=#{id})")
    List<TagCategoryList> queryTags(String id);

    @Select("SELECT id,title from article where publish_time>#{articleId} order by publish_time ASC limit 0,1;")
    IdTitle queryNextArticle(String articleId);

    @Select("SELECT id,title from article where publish_time<#{articleId} order by publish_time ASC limit 0,1;")
    IdTitle queryPreArticle(String articleId);

    @Select("SELECT blog_name,avatar,sign,github from blog_config where id=1")
    BlogInfo queryBlogInfo();

    @Select("select * from article where id=ANY(select article_id as id from article_tag_mapper where tag_id=#{tagId})")
    List<Article> queryAllArticleByTag(String tagId);

    @Select("select * from article where title like #{searchValue} or sub_message like #{searchValue}")
    List<Article> searchArticleByCondition(String searchValue);
}
