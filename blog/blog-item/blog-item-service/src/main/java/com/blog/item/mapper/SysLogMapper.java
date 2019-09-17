package com.blog.item.mapper;

import com.blog.item.pojo.SysLog;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface SysLogMapper extends Mapper<SysLog> {

    @Select("select * FROM sys_log order by time desc")
    Page<SysLog> querySysLog();

    @Select("select count(*) from sys_log")
    int querySysLogCount();
}
