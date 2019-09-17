package com.blog.item.controller;

import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("a/qiniu")
public class TokenController {


    @GetMapping("token")
    public ResponseEntity<String> generateToken() {
        String accessKey = "自己的AK";
        String secretKey = "自己的SK";
        String bucket = "blogimg";
        long expireSeconds = 600;   //过期时间
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"imgUrl\": \"自己的域名/$(key)\"}");
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket,null, expireSeconds,putPolicy);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", upToken);
        ReturnJson<HashMap<String, String>> json = new ReturnJson<>(true, 200, "sucess", map);
        String serialize = JsonUtils.serialize(json);
        return ResponseEntity.ok(serialize);
    }
}
