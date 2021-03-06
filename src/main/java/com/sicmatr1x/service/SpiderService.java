package com.sicmatr1x.service;

import com.sicmatr1x.dao.ArticleDao;
import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.spider.HuxiuHtmlUtil;
import com.sicmatr1x.spider.ZhihuHtmlUtil;
import com.sicmatr1x.spider.ZhihuZhuanlanHtmlUtil;
import org.bson.BsonSerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author sicmatr1x
 */
@Service
public class SpiderService {

    @Autowired
    private ZhihuHtmlUtil zhihuHtmlUtil;

    @Autowired
    private ZhihuZhuanlanHtmlUtil zhihuZhuanlanHtmlUtil;

    @Autowired
    private HuxiuHtmlUtil huxiuHtmlUtil;

    @Autowired
    private ArticleDao articleDao;

    public Article spiderZhihuAnswer(Article article) throws IOException {
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleDao.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            return articleResult;
        }
        // 开始爬取
        zhihuHtmlUtil.setAddress(article.getUrl());
        zhihuHtmlUtil.parse();
        article.setTitle(zhihuHtmlUtil.getTitle());
        article.setBody(zhihuHtmlUtil.getContent());
        article.setCreatedTime(new Date());
        try {
            articleDao.saveArticle(article);
        } catch (BsonSerializationException exception) {
            // TODO: remove img doms
            return null;
        }

        return article;
    }

    public Article spiderZhihuZhuanLan(Article article) throws IOException {
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleDao.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            return articleResult;
        }
        // 开始爬取
        zhihuZhuanlanHtmlUtil.setAddress(article.getUrl());
        zhihuZhuanlanHtmlUtil.parse();
        article.setTitle(zhihuZhuanlanHtmlUtil.getTitle());
        article.setBody(zhihuZhuanlanHtmlUtil.getContent());
        article.setCreatedTime(new Date());
        articleDao.saveArticle(article);
        return article;
    }

    public Article spiderHuxiu(Article article) throws IOException {
        // 使用URL查询数据库，若已存在则不爬取
        Article articleResult = articleDao.findOneArticleByURL(article.getUrl());
        if (articleResult != null) {
            return articleResult;
        }
        // 开始爬取
        huxiuHtmlUtil.setAddress(article.getUrl());
        huxiuHtmlUtil.parse();
        article.setTitle(huxiuHtmlUtil.getTitle());
        article.setBody(huxiuHtmlUtil.getContent());
        article.setCreatedTime(new Date());
        articleDao.saveArticle(article);
        return article;
    }

    public Article findOneArticleByURL(String url) {
        return articleDao.findOneArticleByURL(url);
    }

    public List<Article> findRecentlyArticles(Integer number) {
        return articleDao.findRecentlyArticles(number);
    }
}
