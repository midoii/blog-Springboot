package com.blog.item.controller;


import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.blog.item.service.ArticleService;
import com.blog.item.service.CommentsService;
import com.blog.item.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
* 客户端的接口
* */
@RestController
@RequestMapping("w")
public class WebServiceController {

    @Autowired
    private WebService webService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private ArticleService articleService;

    /*
    * 关于我的界面
    * */
    @GetMapping("getAbout")
    public ResponseEntity getAboutMe(){
        return ResponseEntity.ok(JsonUtils.serialize(webService.getAboutMe()));
    }

    /*
    * 获取文章归档信息
    * */
    @GetMapping("article/archives")
    public ResponseEntity getArticleArchives(
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize
    ){

        return ResponseEntity.ok(JsonUtils.serialize(webService.getArticleArchives(page, pageSize)));
    }

    /*
    * 获取文章的信息
    * */
    @GetMapping("article")
    public ResponseEntity getArticleInfo(
            @RequestParam("id") String id
    ){

        return ResponseEntity.ok(JsonUtils.serialize(webService.getArticleInfo(id)));
    }

    /*
    * 获取文章列表
    * */
    @GetMapping("article/list")
    public ResponseEntity getArticleList(
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "by", defaultValue = "status") String by,
            @RequestParam(value = "status", defaultValue = "0") Integer status,
            @RequestParam(value = "categoryId", defaultValue = "0") String categoryId,
            @RequestParam(value = "tagId", defaultValue = "0") String tagId
    ){
        if(by.equals("status"))
            return ResponseEntity.ok(JsonUtils.serialize(webService.getArticleList(page, pageSize)));
        else if (by.equals("category")) {
            return ResponseEntity.ok(JsonUtils.serialize(webService.getArticleByCategory(page, pageSize, categoryId)));
        } else {
            return ResponseEntity.ok(JsonUtils.serialize(webService.getArticleByTag(page, pageSize, tagId)));
        }
    }

    /*
     * 获取所有分类
     * */
    @GetMapping("category/list")
    public ResponseEntity getCategoryList(){
        return ResponseEntity.ok(JsonUtils.serialize(webService.getCategoryList()));
    }

    /*
    * 获取所有标签
    * */
    @GetMapping("tag/list")
    public ResponseEntity getTagList(){
        return ResponseEntity.ok(JsonUtils.serialize(webService.getTagsList()));
    }

    /*
    * 获取博客的基本信息
    * */
    @GetMapping("blogInfo")
    public ResponseEntity getBlogInfo(){
        return ResponseEntity.ok(JsonUtils.serialize(webService.getBlogInformation()));
    }

    /*
    * 某篇文章的评论列表
    * */
    @GetMapping("comments/list")
    public ResponseEntity getCommentList(
            @RequestParam("articleId") String articleId
    ){
        return ResponseEntity.ok(JsonUtils.serialize(webService.getCommentsByArticleId(articleId)));
    }

    /*
    * 在前台添加评论
    * 这里添加评论都不是作者，isAuthor都标记为0
    * 需要以作者身份评论可以去后台
    * */
    @PostMapping("comments/add")
    public ResponseEntity addComment(
            @RequestParam(value = "articleId") String articleId,
            @RequestParam(value = "name", defaultValue = "匿名") String name,
            @RequestParam(value = "replyId", defaultValue = "0") Integer replyId,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "sourceContent") String sourceContent,
            @RequestParam(value = "email", defaultValue = "") String email
    ){

        ReturnJson returnJson = commentsService.addArticleComment(content, articleId, replyId, sourceContent, name, email, 0);
        return ResponseEntity.ok(JsonUtils.serialize(returnJson));
    }

    /*
    * 实现搜索功能
    * */
    @GetMapping("article/search")
    public ResponseEntity searchArticle(
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "searchValue") String searchValue
    ){

        ReturnJson searchResult = webService.getSearchResult(searchValue, page, pageSize);
        return ResponseEntity.ok(JsonUtils.serialize(searchResult));
    }
}
