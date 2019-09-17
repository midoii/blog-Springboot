package com.blog.item.controller;

import com.blog.common.utils.JsonUtils;
import com.blog.common.back.ReturnJson;
import com.blog.item.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("a/statistics")
public class StatisticsController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private AdminService adminService;

    /*
    * 获取首页面板的统计信息
    * */
    @GetMapping("home")
    public ResponseEntity<String> getHomeStatistics(
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        int publishCount = articleService.getArticleCountByStatus(0);
        int deletedCount = articleService.getArticleCountByStatus(1);
        int draftsCount = articleService.getArticleCountByStatus(2);
        int categoryCount = categoryService.getCategoryCount();
        int tagCount = tagService.getTagCount();
        int commentsCount = commentsService.getAllCommentsCount();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("publishCount", publishCount);
        map.put("draftsCount", draftsCount);
        map.put("deletedCount", deletedCount);
        map.put("categoryCount", categoryCount);
        map.put("tagCount", tagCount);
        map.put("commentsCount", commentsCount);

        String output = JsonUtils.serialize(new ReturnJson<>(true,200, "sucess",map));
        return ResponseEntity.ok(output);
    }
}
