package com.blog.item.service;

import com.blog.common.back.ReturnJson;
import com.blog.common.utils.JsonUtils;
import com.blog.item.mapper.BlogConfigMapper;
import com.blog.item.pojo.BlogConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogConfigService {

    @Autowired
    private BlogConfigMapper blogConfigMapper;

    public ReturnJson<BlogConfig> getBlogConfigData(){
        BlogConfig data = blogConfigMapper.queryBlogConfigData();
        data.setHadOldPassword(false);
        data.setId(null);
        return new ReturnJson<>(true, 200, "sucess", data);
    }

    public ReturnJson<String> updateBlogConfig(String blogName, String avatar, String sign,
                                         String wxpayQrcode, String alipayQrcode, String github){
        blogConfigMapper.updateBlogConfigData(blogName, avatar, sign, wxpayQrcode, alipayQrcode, github);
        return new ReturnJson<>(true, 200, "sucess", "更新成功");
    }
}
