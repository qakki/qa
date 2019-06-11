package com.summer.qa.controller;

import com.summer.qa.async.EventModel;
import com.summer.qa.async.EventProducer;
import com.summer.qa.async.EventType;
import com.summer.qa.model.HostHolder;
import com.summer.qa.service.CommentService;
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
  @Autowired private EventProducer eventProducer;
  @Autowired private CommentService commentService;

  /**
   * @author: lightingSummer
   * @date: 2019/6/10 0010
   * @description: 点赞入口
   */
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

    // 异步处理点赞，站内信通知
    EventModel eventModel = new EventModel();
    eventModel.setType(EventType.LIKE_EVENT);
    eventModel.setActorId(hostHolder.getUser().getId());
    eventModel.setEntityType(SettingUtil.ENTITY_COMMENT);
    eventModel.setEntityId(commentId);
    eventModel.setEventOwnerId(commentService.getCommentById(commentId).getUserId());
    eventProducer.addEvent(eventModel);

    return QAUtil.getJSONString(0, String.valueOf(likeCount));
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/10 0010
   * @description: 点踩入口
   */
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
