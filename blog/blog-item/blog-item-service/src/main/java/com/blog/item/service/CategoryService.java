package com.blog.item.service;

import com.blog.common.back.ListTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.common.utils.GetRandomString;
import com.blog.item.mapper.ArticleMapper;
import com.blog.item.mapper.CategoryMapper;
import com.blog.item.mapper.TagCategoryMapper;
import com.blog.item.pojo.Article;
import com.blog.item.pojo.Category;
import com.blog.item.pojo.TagCategoryList;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagCategoryMapper tagCategoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    public int getCategoryCount(){
        return categoryMapper.queryAllNum();
    }

    public ReturnJson<String> addCategory(String categoryName){

        if(categoryName.length() == 0){
            return new ReturnJson<>(false, -4002, "分类名称不能为空", "");
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        int count = categoryMapper.selectCount(category);
        if(count > 0){
            return new ReturnJson<>(false, -4002, "该分类已经存在，请勿重复添加", "");
        }
        category.setCategoryId(GetRandomString.getRandomString(22));
        category.setCreateTime((new Date().getTime())/1000);
        categoryMapper.insertSelective(category);
        return new ReturnJson<>(true, 200, "success", category.getCategoryId());
    }

    public ReturnJson<String> updateCategory(String categoryId, String categoryName){

        if(categoryId.length() == 0){
            return new ReturnJson<>(false, -4002, "分类ID不能为空", "");
        }
        if(categoryName.length() == 0){
            return new ReturnJson<>(false, -4002, "分类名称不能为空", "");
        }
        Category category = new Category();
        category.setCategoryId(categoryId);
        int count = categoryMapper.selectCount(category);
        if(count == 0){
            return new ReturnJson<>(false, -4002, "该分类不存在", "");
        }
        category.setCategoryName(categoryName);
        category.setUpdateTime((new Date().getTime())/1000);
        categoryMapper.updateByPrimaryKeySelective(category);
        return new ReturnJson<>(true, 200, "更新成功", "");
    }

    public ReturnJson<String> deleteCategory(String categoryId){
        if(categoryId.length() == 0){
            return new ReturnJson<>(false, -4002, "分类ID不能为空", "");
        }
        Category category = new Category();
        category.setCategoryId(categoryId);
        int count = categoryMapper.selectCount(category);
        if(count == 0){
            return new ReturnJson<>(false, -4002, "该分类不存在", "");
        }
        categoryMapper.delete(category);
        return new ReturnJson<>(true, 200, "删除成功", "");
    }

    public ReturnJson<ListTemplate> listAllCategory(){
        List<Category> categories = categoryMapper.selectAll();
        int size = categories.size();
        ListTemplate template = new ListTemplate<>(0, size, size, categories);
        return new ReturnJson<>(true, 200, "success", template);
    }

    public ReturnJson<List> getMagByCategory(String id){
        List<TagCategoryList> lists = tagCategoryMapper.queryMsgByCategory(id);
        return new ReturnJson<>(true, 200, "success", lists);
    }
}
