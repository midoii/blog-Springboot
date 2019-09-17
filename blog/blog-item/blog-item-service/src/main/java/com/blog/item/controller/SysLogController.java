package com.blog.item.controller;


import com.blog.common.back.ListTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.blog.item.pojo.SysLog;
import com.blog.item.service.AdminService;
import com.blog.item.service.SysLogService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("a/sys")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private AdminService adminService;

    /*
    * 获取系统日志信息
    * */
    @GetMapping("log")
    public ResponseEntity<String> querySysLogByPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize,
            @RequestHeader("accessToken") String accessToken
    ){
        if(!adminService.checkAccessToken(accessToken))
            return ResponseEntity.ok((JsonUtils.serialize(new ReturnJson<>(false, -4001, "无效的Token", ""))));

        Page<SysLog> result = sysLogService.getSysLogByPage(page, pageSize);
        ListTemplate listTemplate = new ListTemplate<>(page, pageSize, sysLogService.queryCountOfSysLog(), result);
        ReturnJson returnJson = new ReturnJson<>(true, 200, "sucess", listTemplate);
        String output = JsonUtils.serialize(returnJson);
        return ResponseEntity.ok(output);
    }
}
