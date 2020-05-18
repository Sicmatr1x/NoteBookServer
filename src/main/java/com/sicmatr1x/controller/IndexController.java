package com.sicmatr1x.controller;

import com.sicmatr1x.common.Constant;
import com.sicmatr1x.vo.CommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sicmatr1x
 */
@RestController
public class IndexController {

  @Autowired
  private Constant constant;

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



}
