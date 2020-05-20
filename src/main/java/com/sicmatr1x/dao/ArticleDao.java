package com.sicmatr1x.dao;

import com.sicmatr1x.pojo.Article;

/**
 * @author GUOJO
 */
public interface ArticleDao {

    public void saveArticle(Article article);

    public Article findOneArticleByURL(String url);
}
