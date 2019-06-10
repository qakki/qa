package com.summer.qa.controller;

import com.summer.qa.model.HostHolder;
import com.summer.qa.service.LikeService;
import com.summer.qa.util.QAUtil;
import com.summer.qa.util.SettingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ：lightingSummer
 * @date ：2019/6/10 0010
 * @description：
 */
@Controller
public class LikeController {
  private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

  @Autowired private HostHolder hostHolder;
  @Autowired private LikeService likeService;

  @RequestMapping(
      path = {"/like"},
      method = {RequestMethod.GET, RequestMethod.POST})
  @ResponseBody
  public String like(@RequestParam("commentId") int commentId) {
    if (hostHolder.getUser() == null) {
      return QAUtil.getJSONString(999);
    }
    long likeCount =
        likeService.like(hostHolder.getUser().getId(), SettingUtil.ENTITY_COMMENT, commentId);
    return QAUtil.getJSONString(0, String.valueOf(likeCount));
  }

  @RequestMapping(
      path = {"/dislike"},
      method = {RequestMethod.GET, RequestMethod.POST})
  @ResponseBody
  public String dislike(@RequestParam("commentId") int commentId) {
    if (hostHolder.getUser() == null) {
      return QAUtil.getJSONString(999);
    }
    long disLikeCount =
        likeService.disLike(hostHolder.getUser().getId(), SettingUtil.ENTITY_COMMENT, commentId);
    return QAUtil.getJSONString(0, String.valueOf(disLikeCount));
  }
}
