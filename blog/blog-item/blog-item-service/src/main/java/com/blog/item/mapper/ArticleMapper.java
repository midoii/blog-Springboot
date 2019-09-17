package com.blog.item.mapper;

import com.blog.item.pojo.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ArticleMapper  extends Mapper<Article> {
    @Select("SELECT COUNT(*) FROM article WHERE status = #{status}")
    int queryNumByStatus(int status);

    @Select("SELECT * FROM article WHERE status = #{status}" +
            " ORDER BY publish_time DESC")
    List<Article> queryArticleByPage(@Param("status") int status);

    @Select("Select * from article where id=#{id}")
    Article queryArticleById(String id);

    @Update("update article set pageview=pageview+1 where id=#{id}")
    void increaseViewCount(String id);
}
