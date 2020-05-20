package com.sicmatr1x.service;

import com.sicmatr1x.dao.ArticleDao;
import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.spider.ZhihuHtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author sicmatr1x
 */
@Service
public class SpiderService {

    @Autowired
    private ZhihuHtmlUtil zhihuHtmlUtil;

    @Autowired
    private ArticleDao articleDao;

    public Article spiderZhihuAnswer(Article article) throws IOException {
        zhihuHtmlUtil.setAddress(article.getUrl());
        zhihuHtmlUtil.parse();
        article.setTitle(zhihuHtmlUtil.getTitle());
        article.setBody(zhihuHtmlUtil.getContent());
        articleDao.saveArticle(article);
        return article;
    }

    public Article findOneArticleByURL(String url) {
        return articleDao.findOneArticleByURL(url);
    }
}
