package com.sicmatr1x.dao;

import com.sicmatr1x.dao.ArticleDao;
import com.sicmatr1x.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author GUOJO
 */
@Repository
public class ArticleDaoImpl implements ArticleDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveArticle(Article article)  {
        mongoTemplate.save(article);
    }

    @Override
    public Article findOneArticleByURL(String url) {
        Query query = new Query(Criteria.where("url").is(url));
        Article articleEntity = mongoTemplate.findOne(query, Article.class);
        return articleEntity;
    }
}
