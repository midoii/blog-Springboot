package com.blog.item.controller;


import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.blog.item.pojo.SysLog;
import com.blog.item.service.AdminService;
import com.blog.item.service.ArticleService;
import com.blog.item.service.SysLogService;
import com.qiniu.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("a/article")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private AdminService adminService;

    /*
    * 显示所有列表
    * */
    @GetMapping("list")
    public ResponseEntity<String> getArticleByPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
            @RequestParam(value = "by", defaultValue = "status") String by,
            @RequestParam(value = "status", defaultValue = "0") Integer status,
            @RequestParam(value = "categoryId", defaultValue = "0") String categoryId,
            @RequestParam(value = "tagId", defaultValue = "0") String tagId,
            @RequestHeader("accessToken") String accessToken
    ) {
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        if (by.equals("status")) {
            return ResponseEntity.ok(JsonUtils.serialize(articleService.getArticleByStatus(page, pageSize, status)));
        }else if (by.equals("category")) {
            return ResponseEntity.ok(JsonUtils.serialize(articleService.getArticleByCategory(page, pageSize, categoryId)));
        } else {
            return ResponseEntity.ok(JsonUtils.serialize(articleService.getArticleByTag(page, pageSize, tagId)));
        }
    }

    /*
    * 保存文章
    * */
    @PostMapping("save")
    public ResponseEntity<String> saveArticle(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "htmlContent") String htmlContent,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "cover") String cover,
            @RequestParam(value = "subMessage") String subMessage,
            @RequestParam(value = "isEncrypt") Integer isEncrypt,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        if(id == null){
            ReturnJson json = articleService.saveArticleWithoutId(content, htmlContent, title, cover, subMessage, isEncrypt);
            String msg = "管理员" + "保存了文章" + json.getData();
            sysLogService.saveSysLog(msg);
            return ResponseEntity.ok(JsonUtils.serialize(json));
        }else{
            ReturnJson json = articleService.saveArticleWithId(id, content, htmlContent, title, cover, subMessage, isEncrypt);
            String msg = "管理员" + "保存了文章" + json.getData();
            sysLogService.saveSysLog(msg);
            return ResponseEntity.ok(JsonUtils.serialize(json));
        }
    }


    /*
    * 发布文章
    * */
    @PostMapping("publish")
    public ResponseEntity<String> publishArticle(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "htmlContent") String htmlContent,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "cover") String cover,
            @RequestParam(value = "subMessage") String subMessage,
            @RequestParam(value = "isEncrypt") Integer isEncrypt,
            @RequestParam(value = "category[id]") String category,
            @RequestParam(value = "tags[0][id]", required = false) String tag1,
            @RequestParam(value = "tags[1][id]", required = false) String tag2,
            @RequestParam(value = "tags[2][id]", required = false) String tag3,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        if(title == null)
            return ResponseEntity.ok(JsonUtils.serialize(new ReturnJson<>(false, -1, "标题不能为空", "")));
        if(content == null)
            return ResponseEntity.ok(JsonUtils.serialize(new ReturnJson<>(false, -1, "文章内容不能为空", "")));
        if(subMessage == null)
            return ResponseEntity.ok(JsonUtils.serialize(new ReturnJson<>(false, -1, "文章简介不能为空", "")));
        if(category == null)
            return ResponseEntity.ok(JsonUtils.serialize(new ReturnJson<>(false, -1, "分类名不能为空", "")));
        if(tag1 == null)
            return ResponseEntity.ok(JsonUtils.serialize(new ReturnJson<>(false, -1, "至少需要一个标签", "")));
        if(id == null){
            ReturnJson json = articleService.saveArticleWithoutId(content, htmlContent, title, cover, subMessage, isEncrypt);
            id = json.getData().toString();
        }else{
            articleService.saveArticleWithId(id, content, htmlContent, title, cover, subMessage, isEncrypt);
        }
        ReturnJson returnJson = articleService.publishArticle(id, category, tag1, tag2, tag3);
        return ResponseEntity.ok(JsonUtils.serialize(returnJson));
    }

    /*
    * 获取文章信息
    * */
    @GetMapping("info")
    public ResponseEntity<String> getArticleInfo(
            @RequestParam(value = "id") String id,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));


        ReturnJson articleInfo = articleService.getArticleInfo(id);
        return ResponseEntity.ok(JsonUtils.serialize(articleInfo));
    }

    /*
    * 删除文章
    * */
    @PostMapping("delete")
    public ResponseEntity<String> deleteArticle(
            @RequestParam(value = "id") String id,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));


        ReturnJson returnJson = articleService.deleteArticle(id);
        sysLogService.saveSysLog("管理员删除了文章:" + returnJson.getData());
        return ResponseEntity.ok(JsonUtils.serialize(returnJson));
    }
}
