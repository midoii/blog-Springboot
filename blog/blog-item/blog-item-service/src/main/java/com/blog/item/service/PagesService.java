package com.blog.item.service;


import com.blog.common.back.ReturnJson;
import com.blog.item.mapper.PagesMapper;
import com.blog.item.pojo.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagesService {

    @Autowired
    private PagesMapper pagesMapper;


    public ReturnJson<Pages> getAboutMsg(){
        Pages pages = new Pages();
        pages.setType("about");
        Pages one = pagesMapper.selectOne(pages);
        return new ReturnJson<>(true, 200, "sucess", one);
    }

    public ReturnJson<String> updateAboutMsg(String aboutMeContent, String htmlContent){
        Pages pages = new Pages();
        pages.setType("about");
        pages.setMd(aboutMeContent);
        pages.setHtml(htmlContent);
        pagesMapper.updateByPrimaryKeySelective(pages);
        return new ReturnJson<>(true, 200, "sucess", "更新成功");
    }
}
