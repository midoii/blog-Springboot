package com.blog.item.mapper;

import com.blog.item.pojo.BlogConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;


public interface BlogConfigMapper extends Mapper<BlogConfig> {

    @Select("select blog_name blogName, avatar," +
            " sign,wxpay_qrcode wxpayQrcode,alipay_qrcode alipayQrcode, github from blog_config where id = 1")
    BlogConfig queryBlogConfigData();

    @Update("update blog_config set blog_name=#{blogName}, avatar=#{avatar}, sign=#{sign}, " +
            "wxpay_qrcode=#{wxpayQrcode}, alipay_qrcode=#{alipayQrcode}, github=#{github} where id=1")
    void updateBlogConfigData(@Param("blogName") String blogName,@Param("avatar") String avatar,@Param("sign") String sign,
                              @Param("wxpayQrcode") String wxpayQrcode,@Param("alipayQrcode") String alipayQrcode,
                              @Param("github") String github);

}
