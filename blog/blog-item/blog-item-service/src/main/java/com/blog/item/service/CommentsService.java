package com.blog.item.service;

import com.blog.common.back.CommentTemplate;
import com.blog.common.back.ListTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.item.mapper.ArticleMapper;
import com.blog.item.mapper.CommentsMapper;
import com.blog.item.pojo.Article;
import com.blog.item.pojo.Comments;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private ArticleMapper articleMapper;

    public int getAllCommentsCount(){
        return commentsMapper.queryAllNum();
    }

    public ReturnJson getArticleComment(String articleId){
        Article article = new Article();
        article.setId(articleId);
        Article one = articleMapper.selectOne(article);
        if(one == null){
            return new ReturnJson<>(false, -1, "找不到该文章", "");
        }

        List<Comments> comments = commentsMapper.queryParentComment(articleId);
        for(int i = 0; i < comments.size(); i++){
            List<Comments> childrenComment = commentsMapper.queryChildrenComment(articleId, comments.get(i).getId());
            comments.get(i).setChildren(childrenComment);
        }
        CommentTemplate template = new CommentTemplate(comments.size(), comments);
        return new ReturnJson<>(true, 200, "success", template);
    }

    public ReturnJson addArticleComment(String content, String articleId, Integer replyId, String sourceContent,
                                        String name, String email, Integer isAuthor){
        if(content.length() == 0){
            return new ReturnJson<>(false, -1, "评论内容不能为空", "");
        }
        Article article = new Article();
        article.setId(articleId);
        Article one = articleMapper.selectOne(article);
        if(one == null)
            return new ReturnJson<>(false, -1, "没有找到指定文章", "");
        if(replyId != 0){
            Comments comments = new Comments();
            comments.setId(replyId);
            Comments selectOne = commentsMapper.selectOne(comments);
            if(selectOne == null)
                return new ReturnJson<>(false, -1, "没有找到回复的对象", "");
            if(!selectOne.getArticleId().equals(articleId))
                return new ReturnJson<>(false, -1, "评论文章不匹配", "");
            if(selectOne.getParentId() == 0)
                insertComment(content, articleId, replyId, selectOne.getId(), sourceContent, name, email, isAuthor);
            else
                insertComment(content, articleId, replyId, selectOne.getParentId(), sourceContent, name, email, isAuthor);
        }
        else
            insertComment(content, articleId, 0, 0, sourceContent, name, email, isAuthor);
        return new ReturnJson<>(true, 200, "评论成功", "");
    }

    public void insertComment(String content, String articleId, Integer replyId, Integer parentId,
                              String sourceContent,String name, String email, Integer isAuthor){
        Comments comments = new Comments();
        comments.setContent(content);
        comments.setArticleId(articleId);
        comments.setParentId(parentId);
        comments.setSourceContent(sourceContent);
        comments.setName(name);
        comments.setReplyId(replyId);
        comments.setCreateTime((new Date().getTime())/1000);
        comments.setIsAuthor(isAuthor);
        comments.setEmail(email);
        commentsMapper.insertSelective(comments);
    }


    public ReturnJson deleteComment(Integer commentsId){
        Comments comments = new Comments();
        comments.setId(commentsId);
        Comments one = commentsMapper.selectOne(comments);
        if(one == null)
            return new ReturnJson<>(false, -1, "没有找到该评论", "");
        commentsMapper.delete(one);
        return new ReturnJson<>(true, 200 ,"删除成功", "");
    }

    public ReturnJson getAllComment(Integer page, Integer pageSize){
        PageHelper.startPage(page, pageSize);
        List<Comments> comments = commentsMapper.selectAll();
        ListTemplate<List<Comments>> template = new ListTemplate<>(page, pageSize, comments.size(), comments);
        return new ReturnJson<>(true, 200, "success", template);
    }
}
