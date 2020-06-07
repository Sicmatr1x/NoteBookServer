package com.sicmatr1x.service;

import com.sicmatr1x.dao.ArticleDao;
import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.spider.ZhihuHtmlUtil;
import com.sicmatr1x.spider.ZhihuZhuanlanHtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

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
        articleDao.saveArticle(article);
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

    public Article findOneArticleByURL(String url) {
        return articleDao.findOneArticleByURL(url);
    }
}
