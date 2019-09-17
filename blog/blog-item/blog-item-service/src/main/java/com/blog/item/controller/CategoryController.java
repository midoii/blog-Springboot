package com.blog.item.controller;


import com.blog.common.back.ListTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.blog.item.service.AdminService;
import com.blog.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("a/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AdminService adminService;

    /*
    * 添加分类
    * */
    @PostMapping("add")
    public ResponseEntity<String> addCategory(
            @RequestParam(value = "categoryName") String categoryName,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<String> category = categoryService.addCategory(categoryName);
        return ResponseEntity.ok(JsonUtils.serialize(category));
    }

    /*
    * 修改分类名称
    * */
    @PostMapping("modify")
    public ResponseEntity<String> updateCategory(
            @RequestParam(value = "categoryId") String categoryId,
            @RequestParam(value = "categoryName") String categoryName,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<String> category = categoryService.updateCategory(categoryId, categoryName);
        return ResponseEntity.ok(JsonUtils.serialize(category));
    }

    /*
    * 删除分类
    * */
    @PostMapping("delete")
    public ResponseEntity<String> deleteCategory(
            @RequestParam(value = "categoryId") String categoryId,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<String> category = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(JsonUtils.serialize(category));
    }

    /*
    * 显示所有分类
    * */
    @GetMapping("list")
    public ResponseEntity<String> getAllCategory(
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<ListTemplate> listTemplateReturnJson = categoryService.listAllCategory();
        return ResponseEntity.ok(JsonUtils.serialize(listTemplateReturnJson));
    }

    /*
    * 获取分类信息
    * */
    @GetMapping()
    public ResponseEntity<String> getMsgByCategory(
            @RequestParam(value = "categoryId") String categoryId,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        ReturnJson<List> json = categoryService.getMagByCategory(categoryId);
        return ResponseEntity.ok(JsonUtils.serialize(json));
    }
}
