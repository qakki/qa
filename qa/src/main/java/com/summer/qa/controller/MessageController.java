package com.summer.qa.controller;

import com.summer.qa.model.HostHolder;
import com.summer.qa.model.Message;
import com.summer.qa.model.User;
import com.summer.qa.model.ViewObject;
import com.summer.qa.service.MessageService;
import com.summer.qa.service.SensitiveService;
import com.summer.qa.service.UserService;
import com.summer.qa.util.QAUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/6 0006
 * @description：
 */
@Controller
public class MessageController {
  private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

  @Autowired private HostHolder hostHolder;
  @Autowired private MessageService messageService;
  @Autowired private UserService userService;
  @Autowired private SensitiveService sensitiveService;

  /**
   * @author: lightingSummer
   * @date: 2019/6/6 0006
   * @description: 发送站内信
   */
  @RequestMapping(
      path = {"/msg/addMessage"},
      method = {RequestMethod.POST})
  @ResponseBody
  public String addMessage(
      @RequestParam("toName") String toName, @RequestParam("content") String content) {
    try {
      if (hostHolder.getUser() == null) {
        return QAUtil.getJSONString(999, "用户未登录");
      }
      User toUser = userService.getUserByName(toName);
      if (toUser == null) {
        return QAUtil.getJSONString(1, "用户不存在");
      }
      Message message = new Message();
      content = sensitiveService.filter(content);
      message.setFromId(hostHolder.getUser().getId());
      message.setToId(toUser.getId());
      message.setContent(content);
      message.setCreatedDate(new Date());
      messageService.addMessage(message);
      return QAUtil.getJSONString(0);
    } catch (Exception e) {
      logger.error("add message failed :" + e.getMessage());
      return QAUtil.getJSONString(1, "发送消息失败");
    }
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/6 0006
   * @description: 获取消息列表
   */
  @RequestMapping(
      path = {"/msg/list"},
      method = {RequestMethod.GET})
  public String conversationDetail(Model model) {
    try {
      int localUserId = hostHolder.getUser().getId();
      List<ViewObject> conversations = new ArrayList<ViewObject>();
      List<Message> conversationList = messageService.getConversationList(localUserId, 1, 10);
      for (Message msg : conversationList) {
        ViewObject vo = new ViewObject();
        vo.set("conversation", msg);
        int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
        User user = userService.getUserById(targetId);
        vo.set("user", user);
        vo.set(
            "unread",
            messageService.getConversationUnreadCount(localUserId, msg.getConversationId()));
        conversations.add(vo);
      }
      model.addAttribute("conversations", conversations);
    } catch (Exception e) {
      logger.error("获取站内信列表失败" + e.getMessage());
    }
    return "letter";
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/6 0006
   * @description: 对话详情页
   */
  @RequestMapping(
      path = {"/msg/detail"},
      method = {RequestMethod.GET})
  public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
    try {
      List<Message> conversationList = messageService.getConversationDetail(conversationId, 1, 10);
      List<ViewObject> messages = new ArrayList<>();
      for (Message message : conversationList) {
        ViewObject vo = new ViewObject();
        vo.set("message", message);
        User user = userService.getUserById(message.getFromId());
        vo.set("headUrl", user.getHeadUrl());
        vo.set("userId", user.getId());
        messages.add(vo);
      }
      model.addAttribute("messages", messages);
    } catch (Exception e) {
      logger.error("get conversation detail failed " + e.getMessage());
    }
    return "letterDetail";
  }
}
