package com.blog.item.mapper;

import com.blog.item.pojo.Category;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface CategoryMapper extends Mapper<Category> {
    @Select("SELECT COUNT(*) FROM category")
    int queryAllNum();

    @Update("update category set article_count=article_count-1 where id=#{id}")
    void decreaseArticleCount(String id);

    @Update("update category set article_count=article_count+1 where id=#{id}")
    void increaseArticleCount(String id);
}
