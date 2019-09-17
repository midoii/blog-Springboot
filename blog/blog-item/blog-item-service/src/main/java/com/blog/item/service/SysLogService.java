package com.blog.item.service;

import com.blog.common.enums.ExceptionEnum;
import com.blog.common.exception.BlogException;
import com.blog.common.utils.IpUtils;
import com.blog.item.mapper.SysLogMapper;
import com.blog.item.pojo.SysLog;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    public Page<SysLog> getSysLogByPage(int page, int pageSize){
        PageHelper.startPage(page, pageSize);

        Page<SysLog> list = sysLogMapper.querySysLog();
        return list;

    }
    public int queryCountOfSysLog(){
        return sysLogMapper.querySysLogCount();
    }

    public void saveSysLog(String content){
        SysLog sysLog = new SysLog();
        sysLog.setContent(content);
        sysLog.setTime((new Date().getTime())/1000);
        sysLog.setIp("127.0.0.1");
        sysLogMapper.insertSelective(sysLog);
    }
}
