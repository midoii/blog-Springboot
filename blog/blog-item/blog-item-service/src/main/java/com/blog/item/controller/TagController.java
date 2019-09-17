package com.blog.item.controller;

import com.blog.common.back.ListTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.blog.item.service.AdminService;
import com.blog.item.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("a/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private AdminService adminService;
    /*
    * 新增标签
    * */
    @PostMapping("add")
    public ResponseEntity<String> addTag(
            @RequestParam(value = "tagName") String tagName,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<String> tag = tagService.addTag(tagName);
        return ResponseEntity.ok(JsonUtils.serialize(tag));
    }

    /*
    * 修改标签
    * */
    @PostMapping("modify")
    public ResponseEntity<String> updateTag(
            @RequestParam(value = "tagId") String tagId,
            @RequestParam(value = "tagName") String tagName,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<String> tag = tagService.updateTag(tagId, tagName);
        return ResponseEntity.ok(JsonUtils.serialize(tag));
    }

    /*
    * 删除指定标签
    * */
    @PostMapping("delete")
    public ResponseEntity<String> deleteTag(
            @RequestParam(value = "tagId") String tagId,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<String> tag = tagService.deleteTag(tagId);
        return ResponseEntity.ok(JsonUtils.serialize(tag));
    }

    /*
    * 显示所有标签
    * */
    @GetMapping("list")
    public ResponseEntity<String> getAllTag(
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<ListTemplate> allTag = tagService.getAllTag();
        return ResponseEntity.ok(JsonUtils.serialize(allTag));
    }


    @GetMapping()
    public ResponseEntity<String> getMsgByTag(
            @RequestParam(value = "tagId") String tagId,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<List> magByTag = tagService.getMagByTag(tagId);
        return ResponseEntity.ok(JsonUtils.serialize(magByTag));
    }
}
