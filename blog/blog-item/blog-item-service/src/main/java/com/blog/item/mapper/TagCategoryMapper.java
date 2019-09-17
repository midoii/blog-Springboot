package com.blog.item.mapper;

import com.blog.item.pojo.TagCategoryList;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TagCategoryMapper {

    @Select("SELECT id,name,article_count from category where id=#{id}")
    List<TagCategoryList> queryMsgByCategory(String id);

    @Select("SELECT id,name,article_count from tag where id=#{id}")
    List<TagCategoryList> queryMsgByTag(String id);
}
