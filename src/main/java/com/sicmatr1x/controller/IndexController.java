package com.sicmatr1x.controller;

import com.sicmatr1x.common.Constant;
import com.sicmatr1x.pojo.Article;
import com.sicmatr1x.pojo.ArticleSource;
import com.sicmatr1x.service.SpiderService;
import com.sicmatr1x.spider.ZhihuHtmlUtil;
import com.sicmatr1x.vo.CommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

  /**
   *
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
   *
   * @return is add job success
   */
  @RequestMapping("/zhihu/question/{questionId}/answer/{answerId}")
  public CommonVo spiderZhihuAnswer(@PathVariable String questionId, @PathVariable String answerId) {
    CommonVo response = null;
    Article article = new Article();
    article.setUrl(ZhihuHtmlUtil.DOMAIN + "question/" + questionId + "/answer/" + answerId);
    article.setSource(ArticleSource.ZHIHU_ANSWER);
    Article resultArticle = null;
    try {
      resultArticle = spiderService.spiderZhihuAnswer(article);
      response = new CommonVo(true, resultArticle);
    } catch (IOException e) {
      e.printStackTrace();
      response = new CommonVo(false);
      response.setErrorMessage(e.getMessage());
    } finally {
      return response;
    }
  }

}
