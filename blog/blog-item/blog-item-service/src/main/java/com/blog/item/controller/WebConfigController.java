package com.blog.item.controller;

import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.blog.item.pojo.BlogConfig;
import com.blog.item.pojo.Pages;
import com.blog.item.service.AdminService;
import com.blog.item.service.BlogConfigService;
import com.blog.item.service.PagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("a/webConfig")
public class WebConfigController {

    @Autowired
    private BlogConfigService blogConfigService;

    @Autowired
    private PagesService pagesService;

    @Autowired
    private AdminService adminService;

    /*
    * 获取博客目前的配置信息
    * */
    @GetMapping()
    public ResponseEntity<String> getBlogConfigData(
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<BlogConfig> data = blogConfigService.getBlogConfigData();
        String serialize = JsonUtils.serialize(data);
        return ResponseEntity.ok(serialize);
    }


    /*
     * 修改博客配置（暂时还没有写加密那部分）
     * */
    @PostMapping("modify")
    public ResponseEntity<String> updateBlogConfigData(
            @RequestParam(value = "blogName") String blogName,
            @RequestParam(value = "avatar") String avatar,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "wxpayQrcode") String wxpayQrcode,
            @RequestParam(value = "alipayQrcode") String alipayQrcode,
            @RequestParam(value = "github") String github,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<String> blogConfig =
                blogConfigService.updateBlogConfig(blogName, avatar, sign, wxpayQrcode, alipayQrcode, github);
        return ResponseEntity.ok(JsonUtils.serialize(blogConfig));
    }

    /*
    * 获取关于我的信息
    * */
    @GetMapping("getAbout")
    public ResponseEntity<String> getAboutMsg(
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<Pages> msg = pagesService.getAboutMsg();
        return ResponseEntity.ok(JsonUtils.serialize(msg));
    }

    /*
    * 修改关于我的信息
    * */
    @PostMapping("modifyAbout")
    public ResponseEntity<String> updateAboutMsg(
            @RequestParam(value = "aboutMeContent") String aboutMeContent,
            @RequestParam(value = "htmlContent") String htmlContent,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<String> msg = pagesService.updateAboutMsg(aboutMeContent, htmlContent);
        return ResponseEntity.ok(JsonUtils.serialize(msg));
    }
}
