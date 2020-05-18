package com.sicmatr1x.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.sicmatr1x.spider.util.HeadTitleTranslator;
import com.sicmatr1x.spider.util.ImgDownloader;
import com.sicmatr1x.spider.util.ZhihuImgTranslator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ZhihuHtmlUtil extends HtmlUtil {

  public static final String DOMAIN = "https://www.zhihu.com/";


  @Override
  void getHtml() throws IOException {
    Map<String, String> cookies = new HashMap<>();
    cookies.put("MQTT::auth", "{\"username\":\"zhihu_web/c3eba22b84626619308c435efce4eba0\",\"password\":\"1551688934/9c50faaa4884b954a9f5020c10bef1535edc930b\"}");
    cookies.put("lastuser", "\"2499055595@qq.com\"");
    cookies.put("lastuser:email", "2499055595@qq.com");
    cookies.put("privacy-records", "{\"undefined\":1548381571678}");
    cookies.put("zap:storage:local", "{\"canUseDataViewPayload\":true,\"lastUA\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36\"}");
    this.doc = Jsoup.connect(this.address).cookies(cookies).get();
  }

  /**
   * 获取img dom
   * @param element
   * @return
   */
  public Element translateImgDom(Element element) {
    Elements noscriptNode = element.select("noscript");
    if(noscriptNode.size() == 0){
      return element;
    }
    noscriptNode.first().remove();
    Elements imgElements = element.select("img");
    ZhihuImgTranslator zhihuImgTranslator = new ZhihuImgTranslator();
    for(Element imgElement : imgElements){
      imgElement = zhihuImgTranslator.translate(imgElement);
      this.downloadImg(imgElement);
    }
    return element;
  }

  private Element downloadImg(Element element){
    ImgDownloader imgDownloader = new ImgDownloader();
    return imgDownloader.translate(element, this.title + "_files");
  }

  /**
   * 从回答页面的HTML中解析第一个主回答内容
   *
   * @return 回答内容
   */
  public Element getOneAnswer() {
    Elements questionMainColumn = this.doc.select(".Question-mainColumn");
    if (questionMainColumn.size() < 1) {
      System.out.println("error: can not found class=\"Question-mainColumn\" in " + this.address);
    } else {
      Elements cards = questionMainColumn.get(0).select(".AnswerCard");
      Elements answerItems = cards.get(0).select(".AnswerItem");
      Elements richContent = answerItems.get(0).select(".RichContent");
      Elements richText = richContent.get(0).select(".RichText");
      return richText.get(0);
    }
    return null;
  }

  public String getQuestionTitle() {
    String title;
    try{
      Elements question = this.doc.select(".QuestionHeader-title");
      if(question.size() < 0){
        Elements titleNode = this.doc.select("title");
        System.out.println(titleNode.text());
        return titleNode.text();
      }
      title = question.get(0).text();
    }catch(IndexOutOfBoundsException error){
      System.out.println("Analyze Page Error:" + this.address);
      return null;
    }
    return title;
  }

  public void getAnswerList() {
    // TODO
  }

  public void getArticle() {
    // TODO
  }

  @Override
  boolean initAddress() {
    if (this.address == null || this.address.length() < DOMAIN.length()) {
      System.out.print("Address is too short:" + this.address);
      return false;
    }
    return true;
  }

  @Override
  void analysisPage() {
    HeadTitleTranslator headTitleTranslator = new HeadTitleTranslator();
    headTitleTranslator.setFromAddress(this.getAddress());

    int endIndex = this.address.indexOf("/", DOMAIN.length());
    String mode = this.address.substring(DOMAIN.length(), endIndex);
    this.title = this.getQuestionTitle();
    if(this.title == null){
      return;
    }
    if(this.title.equals("知乎 - 有问题，上知乎")){
      return;
    }
    headTitleTranslator.setTitleText(this.title);

    if ("question".equals(mode)) {
      if (this.address.contains("answer")) {
        Element answerElement = this.getOneAnswer(); // 单个回答链接
        answerElement = this.translateImgDom(answerElement);
        this.content = headTitleTranslator.translate(answerElement).html();

      } else {
        this.getAnswerList(); // 问题链接
      }
    } else if ("p".equals(mode)) { // 文章链接
      this.getArticle();
    }
  }

}
