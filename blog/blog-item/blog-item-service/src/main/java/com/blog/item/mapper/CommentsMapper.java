package com.blog.item.mapper;

import com.blog.item.pojo.Comments;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CommentsMapper extends Mapper<Comments> {
    @Select("SELECT COUNT(*) FROM comments")
    int queryAllNum();


    @Select("select * from comments where status=0 and article_id=#{articleId} and parent_id=0 order by create_time DESC")
    List<Comments> queryParentComment(String articleId);


    @Select("select * from comments where status=0 and article_id=#{articleId} and parent_id=#{parentId} order by create_time ASC")
    List<Comments> queryChildrenComment(@Param("articleId") String articleId, @Param("parentId") Integer parentId);
}
