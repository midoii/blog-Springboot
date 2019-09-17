package com.blog.item.service;

import com.blog.common.back.ListTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.common.utils.GetRandomString;
import com.blog.item.mapper.TagCategoryMapper;
import com.blog.item.mapper.TagMapper;
import com.blog.item.pojo.Tag;
import com.blog.item.pojo.TagCategoryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagCategoryMapper tagCategoryMapper;

    public int getTagCount(){
        return tagMapper.queryAllNum();
    }

    public ReturnJson<String> addTag(String tagName){
        if(tagName.length() == 0){
            return new ReturnJson<>(false, -4002, "标签名称不能为空", "");
        }
        Tag tag = new Tag();
        tag.setTagName(tagName);
        int count = tagMapper.selectCount(tag);
        if(count > 0){
            return new ReturnJson<>(false, -4002, "该标签已经存在，请勿重复添加", "");
        }
        tag.setTagId(GetRandomString.getRandomString(22));
        tag.setCreateTime((new Date().getTime())/1000);
        tagMapper.insertSelective(tag);
        return new ReturnJson<>(true, 200, "sucess", tag.getTagId());
    }

    public ReturnJson<String> updateTag(String tagId, String tagName){
        if(tagId.length() == 0){
            return new ReturnJson<>(false, -4002, "标签ID不能为空", "");
        }
        if(tagName.length() == 0){
            return new ReturnJson<>(false, -4002, "标签名称不能为空", "");
        }
        Tag tag = new Tag();
        tag.setTagId(tagId);
        int count = tagMapper.selectCount(tag);
        if(count == 0){
            return new ReturnJson<>(false, -4002, "该标签不存在", "");
        }
        tag.setTagName(tagName);
        tag.setUpdateTime((new Date().getTime())/1000);
        tagMapper.updateByPrimaryKeySelective(tag);
        return new ReturnJson<>(true, 200, "更新成功", "");
    }

    public ReturnJson<String> deleteTag(String tagId){
        if(tagId.length() == 0){
            return new ReturnJson<>(false, -4002, "标签ID不能为空", "");
        }
        Tag tag = new Tag();
        tag.setTagId(tagId);
        int count = tagMapper.selectCount(tag);
        if(count == 0){
            return new ReturnJson<>(false, -4002, "该标签不存在", "");
        }
        tagMapper.delete(tag);
        return new ReturnJson<>(true, 200, "删除成功", "");
    }

    public ReturnJson<ListTemplate> getAllTag(){
        List<Tag> tags = tagMapper.selectAll();
        int size = tags.size();
        ListTemplate template = new ListTemplate<>(0, size, size, tags);
        return new ReturnJson<>(true, 200, "sucess", template);
    }

    public ReturnJson<List> getMagByTag(String id){
        List<TagCategoryList> lists = tagCategoryMapper.queryMsgByTag(id);
        return new ReturnJson<>(true, 200, "sucess", lists);
    }
}
