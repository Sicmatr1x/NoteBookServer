package com.sicmatr1x.spider.util;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import java.util.Iterator;

public class ZhihuImgTranslator implements Translator{

  @Override
  public Element translate(Element imgElement) {
    String srcAddress = null;
    // smart get img src url
    Attributes attrs = imgElement.attributes();
    Iterator<Attribute> iterator = attrs.iterator();
    while(iterator.hasNext()){
      Attribute attr = iterator.next();
      if(attr.getValue() != null && attr.getValue().contains("http")){
        srcAddress = attr.getValue();
      }
    }

    String rawWidth = imgElement.attr("data-rawwidth");
    String rawHeight = imgElement.attr("data-rawheight");
    imgElement.attr("src", srcAddress);
    imgElement.attr("style", "width:" + rawWidth + ";height:" + rawHeight);
    imgElement.removeAttr("data-caption");
    imgElement.removeAttr("data-size");
    imgElement.removeAttr("data-rawwidth");
    imgElement.removeAttr("data-rawheight");
    imgElement.removeAttr("data-default-watermark-src");
    imgElement.removeAttr("data-original");
    imgElement.removeAttr("data-actualsrc");
    return imgElement;
  }
}
