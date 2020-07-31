package com.sicmatr1x.controller;

import com.sicmatr1x.common.Constant;
import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.pojo.ArticleSource;
import com.sicmatr1x.service.SpiderService;
import com.sicmatr1x.spider.ZhihuHtmlUtil;
import com.sicmatr1x.vo.CommonVo;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sicmatr1x
 */
@RestController
@RequestMapping("notebook")
public class IndexController {

  @Autowired
  private Constant constant;

  @Autowired
  private SpiderService spiderService;

  private ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 检查版本号
   * @return application version
   */
  @RequestMapping("/version")
  public CommonVo version() {
    Map<String, String> dataMap = new HashMap<>();
    dataMap.put("version", constant.getVersion());
    CommonVo response = new CommonVo(true, dataMap);
    return response;
  }

  /**
   * 添加知乎回答
   * @return is add job success
   */
  @RequestMapping("/zhihu/question/{questionId}/answer/{answerId}")
  public CommonVo spiderZhihuAnswer(@PathVariable String questionId, @PathVariable String answerId) {
    CommonVo response = new CommonVo(false);
    Article article = new Article();
    article.setUrl(ZhihuHtmlUtil.DOMAIN + "question/" + questionId + "/answer/" + answerId);
    article.setSource(ArticleSource.ZHIHU_ANSWER);
    Article resultArticle = null;
    try {
      resultArticle = spiderService.spiderZhihuAnswer(article);
      response.setSuccess(true);
      // 避免返回body过大
      resultArticle.setBody(resultArticle.getBody().substring(0, 400) + "......");
      response.setData(resultArticle);
    } catch (IOException e) {
      e.printStackTrace();
      response.setErrorMessage(e.getMessage());
    }
    return response;
  }

    /**
     * 添加知乎回答
     * @return is add job success
     */
    @RequestMapping(value="/add/zhihu/answer", method = RequestMethod.POST)
    public CommonVo spiderZhihuAnswer2(@RequestParam String url) {
        CommonVo response = new CommonVo(false);
        Article article = new Article();
        String[] work = url.split("\\?");
        article.setUrl(work[0]);
        article.setSource(ArticleSource.ZHIHU_ANSWER);
        Article resultArticle = null;
        try {
            resultArticle = spiderService.spiderZhihuAnswer(article);
            response.setSuccess(true);
            // 避免返回body过大
            resultArticle.setBody(resultArticle.getBody().substring(0, 400) + "......");
            response.setData(resultArticle);
        } catch (IOException e) {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 添加知乎回答
     * @return is add job success
     */
    @RequestMapping(value="/add/zhihu/p", method = RequestMethod.POST)
    public CommonVo spiderZhihuZhuanLan(@RequestParam String url) {
        CommonVo response = new CommonVo(false);
        Article article = new Article();
        String[] work = url.split("\\?");
        article.setUrl(work[0]);
        article.setSource(ArticleSource.ZHIHU_ZHUANLAN);
        Article resultArticle = null; 
        try {
            resultArticle = spiderService.spiderZhihuZhuanLan(article);
            response.setSuccess(true);
            // 避免返回body过大
            resultArticle.setBody(resultArticle.getBody().substring(0, 400) + "......");
            response.setData(resultArticle);
        } catch (IOException e) {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

  /**
   * 根据URL查找article
   * @param url
   * @return
   */
  @RequestMapping(value="/article",method = RequestMethod.GET)
  public String findOneArticleById(@RequestParam String url) throws IOException {
    Article article = null;
    String[] work = url.split("\\?");
    article = spiderService.findOneArticleByURL(work[0]);
    if (article == null) {
        CommonVo response = new CommonVo(true);
        response.setErrorMessage("Not found any result in DB");
        return objectMapper.writeValueAsString(response);
    } else {
        return article.getBody();
    }
  }

}
