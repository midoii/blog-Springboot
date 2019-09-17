package com.blog.item.controller;


import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.blog.item.service.AdminService;
import com.blog.item.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("a/comments")
public class CommentController {

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private AdminService adminService;

    @GetMapping("list")
    public ResponseEntity<String> getCommentsList(
            @RequestParam(value = "articleId") String articleId,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        return ResponseEntity.ok(JsonUtils.serialize(commentsService.getArticleComment(articleId)));
    }

    /*
    * 添加评论
    * 在后台添加评论默认就是作者本人
    * 可以修改作者名称为自己的名字
    * */
    @PostMapping("add")
    public ResponseEntity<String> addComment(
            @RequestParam(value = "articleId") String articleId,
            @RequestParam(value = "replyId", defaultValue = "0") Integer replyId,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "sourceContent") String sourceContent,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson returnJson = commentsService.addArticleComment(content, articleId, replyId, sourceContent, "acgiesae", email, 1);
        return ResponseEntity.ok(JsonUtils.serialize(returnJson));
    }

    @PostMapping("delete")
    public ResponseEntity<String> deleteComment(
            @RequestParam(value = "commentsId") Integer commentsId,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        return ResponseEntity.ok(JsonUtils.serialize(commentsService.deleteComment(commentsId)));
    }

    @GetMapping("alllist")
    public ResponseEntity<String> getAllComment(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        return ResponseEntity.ok(JsonUtils.serialize(commentsService.getAllComment(page, pageSize)));
    }
}
