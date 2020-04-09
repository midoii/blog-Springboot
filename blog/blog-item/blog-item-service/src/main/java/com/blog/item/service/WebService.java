package com.blog.item.service;

import com.blog.common.back.*;
import com.blog.item.mapper.*;
import com.blog.item.pojo.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WebService {

    @Autowired
    private BlogConfigMapper blogConfigMapper;

    @Autowired
    private PagesMapper pagesMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private MultiMapper multiMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    public ReturnJson getAboutMe(){
        BlogConfig blogConfig = new BlogConfig();
        BlogConfig one = blogConfigMapper.selectOne(blogConfig);
        AboutMeTemplate aboutMeTemplate = new AboutMeTemplate();
        aboutMeTemplate.setQrcode(new ArrayList<>(Arrays.asList(one.getAlipayQrcode(), one.getWxpayQrcode())));
        Pages pages = new Pages();
        pages.setType("about");
        Pages pone = pagesMapper.selectOne(pages);
        aboutMeTemplate.setHtml(pone.getHtml());
        return new ReturnJson<>(true, 200, "success", aboutMeTemplate);
    }

    public ReturnJson getArticleArchives(Integer page, Integer pageSize){
        Map<String, Map<String, List<Archives>>> map = new HashMap<>();
        PageHelper.startPage(page, pageSize);
        List<Article> articles = articleMapper.queryArticleByPage(0);
        for(int i = 0; i < articles.size(); i++){
            Article article = articles.get(i);

            TagCategoryList category = multiMapper.queryCategory(article.getCategoryId());
            List<TagCategoryList> tags = multiMapper.queryTags(article.getId());
            Archives archives = new Archives(article, category, tags);

            Date date = new Date(article.getPublishTime() * 1000);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            if(map.containsKey(year + "年")){
                Map subMap = map.get(year + "年");
                if(subMap.containsKey(month + "月")){
                    List<Archives> list = (List<Archives>) subMap.get(month + "月");
                    list.add(archives);
                    subMap.put(month + "月", list);
                }else {
                    List<Archives> list = new ArrayList<>();
                    list.add(archives);
                    subMap.put(month + "月", list);
                }
                map.put(year + "年", subMap);
            }else{
                Map<String, List<Archives>> subMap=new HashMap<>();
                List<Archives> list = new ArrayList<>();
                list.add(archives);
                subMap.put(month + "月", list);
                map.put(year + "年", subMap);
            }
        }
        ListTemplate<Map<String, Map<String, List<Archives>>>> template = new ListTemplate<>(page, pageSize, articles.size(), map);
        return new ReturnJson<>(true, 200, "success", template);
    }

    public ReturnJson getArticleInfo(String articleId){
        Article article = new Article();
        article.setId(articleId);
        articleMapper.increaseViewCount(articleId);
        Article one = articleMapper.selectOne(article);
        TagCategoryList category = multiMapper.queryCategory(one.getCategoryId());
        List<TagCategoryList> tags = multiMapper.queryTags(articleId);

        BlogConfig blogConfig = blogConfigMapper.queryBlogConfigData();
        Map<String, String> qrcode = new HashMap<>();
        qrcode.put("wxpayQrcode", blogConfig.getWxpayQrcode());
        qrcode.put("alipayQrcode", blogConfig.getAlipayQrcode());

        Map<String, IdTitle> pn = new HashMap<>();
        IdTitle preArticle = multiMapper.queryPreArticle(articleId);
        IdTitle nextArticle = multiMapper.queryNextArticle(articleId);
        if(preArticle == null)
            pn.put("pre", null);
        else
            pn.put("pre", preArticle);
        if(nextArticle == null)
            pn.put("next", null);
        else
            pn.put("next", nextArticle);

        ArticleInfoTemplate articleInfoTemplate = new ArticleInfoTemplate(one, category, tags, qrcode, pn);
        return new ReturnJson<>(true, 200, "success", articleInfoTemplate);
    }

    public ReturnJson getArticleList(Integer page, Integer pageSize){
        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.queryArticleByPage(page);
        List<ArticleMsg> articleMsgs = processWithArticle(list);
        ListTemplate<List<ArticleMsg>> template = new ListTemplate<>(page, pageSize,
                articleMapper.queryNumByStatus(0), articleMsgs);
        return new ReturnJson<>(true, 200, "success", template);

    }

    public ReturnJson<ListTemplate> getArticleByCategory(int page, int pageSize, String categoryId){
        if(categoryId.length() == 0){
            return new ReturnJson<>(false, -1, "分类ID不能为空", new ListTemplate<>());
        }
        Category category = new Category();
        category.setCategoryId(categoryId);
        int num = categoryMapper.selectCount(category);
        if(num == 0){
            return new ReturnJson<>(false, -1, "不存在该分类", new ListTemplate<>());
        }
        PageHelper.startPage(page, pageSize);
        Article article = new Article();
        article.setCategoryId(categoryId);
        List<Article> list = articleMapper.select(article);
        List<ArticleMsg> articleMsgs = processWithArticle(list);
        ListTemplate<List<ArticleMsg>> template = new ListTemplate<>(page, pageSize, articleMsgs.size(), articleMsgs);
        return new ReturnJson<>(true, 200, "success", template);

    }

    public ReturnJson<ListTemplate> getArticleByTag(int page, int pageSize, String tagId){
        if(tagId.length() == 0){
            return new ReturnJson<>(false, -1, "标签ID不能为空", new ListTemplate<>());
        }
        Tag tag = new Tag();
        tag.setTagId(tagId);
        int num = tagMapper.selectCount(tag);
        if(num == 0){
            return new ReturnJson<>(false, -1, "不存在该标签", new ListTemplate<>());
        }
        PageHelper.startPage(page, pageSize);
        List<Article> list = multiMapper.queryAllArticleByTag(tagId);
        List<ArticleMsg> articleMsgs = processWithArticle(list);
        ListTemplate<List<ArticleMsg>> template = new ListTemplate<>(page, pageSize, articleMsgs.size(), articleMsgs);
        return new ReturnJson<>(true, 200, "success", template);
    }

    public List<ArticleMsg> processWithArticle(List<Article> list){
        List<ArticleMsg> articleMsgs = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            Article article = list.get(i);
            TagCategoryList category = multiMapper.queryCategory(article.getCategoryId());
            List<TagCategoryList> tags = multiMapper.queryTags(article.getId());
            articleMsgs.add(new ArticleMsg(article, category, tags));
        }
        return articleMsgs;
    }

    public ReturnJson getCategoryList(){
        List<Category> categories = categoryMapper.selectAll();
        ListTemplate<List<Category>> template = new ListTemplate<>(0, 15, categories.size(), categories);
        return new ReturnJson<>(true, 200, "success", template);
    }

    public ReturnJson getTagsList(){
        List<Tag> tags = tagMapper.selectAll();
        ListTemplate<List<Tag>> template = new ListTemplate<>(0, 15, tags.size(), tags);
        return new ReturnJson<>(true, 200, "success", template);
    }

    public ReturnJson getBlogInformation(){
        BlogInfo blogInfo = multiMapper.queryBlogInfo();
        int num = articleMapper.queryNumByStatus(0);
        blogInfo.setArticleCount(num);
        blogInfo.setCategoryCount(categoryMapper.queryAllNum());
        blogInfo.setTagCount(tagMapper.queryAllNum());
        return new ReturnJson<>(true, 200, "seccess", blogInfo);
    }

    public ReturnJson getCommentsByArticleId(String articleId){
        List<Comments> comments = commentsMapper.queryParentComment(articleId);
        for(int i = 0; i < comments.size(); i++){
            List<Comments> childrenComment = commentsMapper.queryChildrenComment(articleId, comments.get(i).getId());
            comments.get(i).setChildren(childrenComment);
        }
        CommentTemplate template = new CommentTemplate(comments.size(), comments);
        return new ReturnJson<>(true, 200, "success", template);
    }

    public ReturnJson getSearchResult(String searchValue, Integer page, Integer pageSize){
        PageHelper.startPage(page, pageSize);
        List<Article> list = multiMapper.searchArticleByCondition(searchValue);
        List<ArticleMsg> articleMsgs = processWithArticle(list);
        ListTemplate<List<ArticleMsg>> template = new ListTemplate<>(page, pageSize, articleMsgs.size(), articleMsgs);
        return new ReturnJson<>(true, 200, "success", template);
    }
}
