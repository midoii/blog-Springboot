package com.blog.item.service;

import com.blog.common.back.ListTemplate;
import com.blog.common.back.ReturnJson;
import com.blog.common.utils.GetRandomString;
import com.blog.item.mapper.*;
import com.blog.item.pojo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleMsgMapper articleMsgMapper;

    @Autowired
    private MultiMapper multiMapper;

    @Autowired
    private TagArticleMapper tagArticleMapper;

    public int getArticleCountByStatus(int status){
        return articleMapper.queryNumByStatus(status);
    }

    public  ReturnJson<ListTemplate<List<Article>>> getArticleByStatus(int page, int pageSize, int status){
        if(status == 0 || status == 1 || status == 2) {
            PageHelper.startPage(page, pageSize);
            Example example = new Example(Article.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", status);
            List<Article> articles = articleMapper.selectByExample(example);
            ListTemplate<List<Article>> listTemplate = new ListTemplate<>(page, pageSize, articles.size(), articles);
            return new ReturnJson<>(true, 200, "success", listTemplate);
        }
        return new ReturnJson<>(false, -1, "status值不正确", new ListTemplate<>());
    }

    public ReturnJson<ListTemplate> getArticleByCategory(int page, int pageSize, String categoryId){
        if(categoryId.length() == 0){
            return new ReturnJson<>(false, -1, "分类ID不能为空", new ListTemplate<>());
        }
        Category category = new Category();
        category.setCategoryId(categoryId);
        List<Category> list = categoryMapper.select(category);
        if(list.size() == 0){
            return new ReturnJson<>(false, -1, "不存在该分类", new ListTemplate<>());
        }
        PageHelper.startPage(page, pageSize);
        Page<ArticleTemp> tempPage = multiMapper.queryArticleByCategory(categoryId);
        ListTemplate<Page<ArticleTemp>> template = new ListTemplate<>(page, pageSize, tempPage.size(), tempPage);
        return new ReturnJson<>(true, 200, "sucess", template);
    }

    public ReturnJson<ListTemplate> getArticleByTag(int page, int pageSize, String tagId){
        if(tagId.length() == 0){
            return new ReturnJson<>(false, -1, "标签ID不能为空", new ListTemplate<>());
        }
        Tag tag = new Tag();
        tag.setTagId(tagId);
        List<Tag> list = tagMapper.select(tag);
        if(list.size() == 0){
            return new ReturnJson<>(false, -1, "不存在该标签", new ListTemplate<>());
        }
        PageHelper.startPage(page, pageSize);
        Page<ArticleTemp> tempPage = multiMapper.queryArticleByTag(tagId);
        ListTemplate<Page<ArticleTemp>> template = new ListTemplate<>(page, pageSize, tempPage.size(), tempPage);
        return new ReturnJson<>(true, 200, "sucess", template);
    }

    public ReturnJson saveArticleWithoutId(String content,String htmlContent, String title, String cover,
                                             String subMessage, Integer isEncrypt){
        String id = GetRandomString.getRandomString(22);
        Article article = new Article();
        article.setId(id);
        article.setContent(content);
        article.setHtmlContent(htmlContent);
        article.setTitle(title);
        article.setCover(cover);
        article.setSubMessage(subMessage);
        article.setIsEncrypt(isEncrypt);
        article.setCreateTime((new Date().getTime())/1000);
        article.setUpdateTime((new Date().getTime())/1000);
        article.setStatus(2);
        articleMapper.insertSelective(article);
        return new ReturnJson<>(true, 200, "sucess", id);
    }

    public ReturnJson saveArticleWithId(String id, String content,String htmlContent, String title, String cover,
                             String subMessage, Integer isEncrypt){
        Article article = new Article();
        article.setId(id);
        List<Article> list = articleMapper.select(article);
        if(list.size() == 0)
            return new ReturnJson<>(false, -1, "未找到ID对应的文章", "");
        article.setContent(content);
        article.setHtmlContent(htmlContent);
        article.setTitle(title);
        article.setCover(cover);
        article.setSubMessage(subMessage);
        article.setIsEncrypt(isEncrypt);
        articleMapper.updateByPrimaryKeySelective(article);
        return new ReturnJson<>(true, 200, "sucess", id);
    }



    public ReturnJson getArticleInfo(String id){
        Article article = new Article();
        article.setId(id);
        List<Article> list = articleMapper.select(article);
        if(list.size() == 0)
            return new ReturnJson<>(false, -1, "未找到ID对应的文章", "");
        ArticleMsg articleMsg = new ArticleMsg();
        articleMsg.setArticle(articleMsgMapper.queryArticle(id));
        articleMsg.setCategory(multiMapper.queryCategory(articleMsg.getArticle().getCategoryId()));
        articleMsg.setTags(multiMapper.queryTags(id));
        return new ReturnJson<>(true, 200, "sucess", articleMsg);
    }

    @Transactional
    public ReturnJson publishArticle(String id, String category, String tag1, String tag2, String tag3){
        Article article = new Article();
        article.setId(id);
        Article one = articleMapper.selectOne(article);
        if(StringUtils.isNotBlank(one.getCategoryId())){
            if(!one.getCategoryId().equals(category)){
                categoryMapper.decreaseArticleCount(one.getCategoryId());
                categoryMapper.increaseArticleCount(category);
                article.setCategoryId(category);
                article.setPublishTime((new Date().getTime())/1000);
                article.setStatus(0);
                articleMapper.updateByPrimaryKeySelective(article);
            }
        }else {
            categoryMapper.increaseArticleCount(category);
            article.setCategoryId(category);
            article.setStatus(0);
            article.setPublishTime((new Date().getTime())/1000);
            articleMapper.updateByPrimaryKeySelective(article);
        }
        ArticleTagMapper articleTagMapper = new ArticleTagMapper();
        articleTagMapper.setArticleId(id);
        List<ArticleTagMapper> select = tagArticleMapper.select(articleTagMapper);
        for(int i = 0; i < select.size(); i++){
            String tagId = select.get(i).getTagId();
            tagMapper.decreaseArticleCount(tagId);
        }
        tagArticleMapper.delete(articleTagMapper);
        addArticleTagMapper(id, tag1);
        addArticleTagMapper(id, tag2);
        addArticleTagMapper(id, tag3);
        return new ReturnJson<>(true, 200, "sucess", id);
    }

    private void addArticleTagMapper(String id, String tag){
        if(StringUtils.isNotBlank(tag)){
            Tag tt = new Tag();
            tt.setTagId(tag);
            List<Tag> select = tagMapper.select(tt);
            if(select.size() == 0)
                return;
            //tagMapper.increaseArticleCount(tag);
            ArticleTagMapper articleTagMapper = new ArticleTagMapper();
            articleTagMapper.setArticleId(id);
            articleTagMapper.setTagId(tag);
            List<ArticleTagMapper> list = tagArticleMapper.select(articleTagMapper);
            if(list.size() == 0){
                articleTagMapper.setCreateTime((new Date().getTime())/1000);
                tagArticleMapper.insert(articleTagMapper);
                tagMapper.increaseArticleCount(tag);
            }
        }
    }

    @Transactional
    public ReturnJson deleteArticle(String id){
        if(id.length() == 0)
            return new ReturnJson<>(false, -1, "文章ID不能为空", "");
        Article article = new Article();
        article.setId(id);
        Article one = articleMapper.selectOne(article);
        if(one == null){
            return new ReturnJson<>(false, -1, "找不到该文章", "");
        }
        int status = one.getStatus();
        if(status == 1){
            articleMapper.delete(one);
        }else{
            String cat = one.getCategoryId();
            if(status == 2)
                one.setStatus(1);
            else
                one.setStatus(2);
            one.setCategoryId("");
            one.setDeleteTime((new Date().getTime())/1000);
            one.setUpdateTime((new Date().getTime())/1000);
            articleMapper.updateByPrimaryKeySelective(one);
            if(status == 0){
                categoryMapper.decreaseArticleCount(cat);
            }
        }
        ArticleTagMapper articleTagMapper = new ArticleTagMapper();
        articleTagMapper.setArticleId(id);
        List<ArticleTagMapper> list = tagArticleMapper.select(articleTagMapper);
        for(int i = 0; i < list.size(); i++){
            String tagId = list.get(i).getTagId();
            tagMapper.decreaseArticleCount(tagId);
        }
        tagArticleMapper.delete(articleTagMapper);
        return new ReturnJson<>(true, 200, "sucess", "删除成功");
    }
}
