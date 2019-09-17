package com.blog.item.controller;


import com.blog.common.back.ListTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.blog.item.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("a")
public class AdminController {

    @Autowired
    private AdminService adminService;


    /*
    * 创建后台登陆账号
    * */
//    @PostMapping("create")
//    public ResponseEntity<String> createAdmin(
//            @RequestParam(value = "username") String username,
//            @RequestParam(value = "password") String password
//    ){
//        ReturnJson json = adminService.createAdmin(username, password);
//        String output = JsonUtils.serialize(json);
//        return ResponseEntity.ok(output);
//    }

    @PostMapping("login")
    public ResponseEntity<String> certifyAdminMsg(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ){
        ReturnJson json = adminService.verifyAdminMsg(username, password);
        String output = JsonUtils.serialize(json);
        return ResponseEntity.ok(output);

    }


}
