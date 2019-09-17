package com.blog.item.mapper;

import com.blog.item.pojo.Tag;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface TagMapper extends Mapper<Tag> {
    @Select("SELECT COUNT(*) FROM tag")
    int queryAllNum();

    @Update("update tag set article_count=article_count-1 where id=#{id}")
    void decreaseArticleCount(String id);

    @Update("update tag set article_count=article_count+1 where id=#{id}")
    void increaseArticleCount(String id);
}
