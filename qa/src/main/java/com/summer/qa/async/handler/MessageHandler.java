package com.summer.qa.async.handler;

import com.summer.qa.async.EventHandler;
import com.summer.qa.async.EventModel;
import com.summer.qa.async.EventType;
import com.summer.qa.model.Comment;
import com.summer.qa.model.Message;
import com.summer.qa.model.Question;
import com.summer.qa.model.User;
import com.summer.qa.service.CommentService;
import com.summer.qa.service.MessageService;
import com.summer.qa.service.QuestionService;
import com.summer.qa.service.UserService;
import com.summer.qa.util.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/11 0011
 * @description：
 */
@Component
public class MessageHandler implements EventHandler {

  @Autowired private MessageService messageService;
  @Autowired private CommentService commentService;
  @Autowired private UserService userService;
  @Autowired private QuestionService questionService;

  @Override
  public void doHandle(EventModel model) {
    Message message = new Message();
    message.setFromId(1);
    message.setToId(model.getEventOwnerId());
    message.setCreatedDate(new Date());
    if (model.getType() == EventType.LIKE_EVENT) {
      if (model.getEntityType() == SettingUtil.ENTITY_COMMENT) {
        Comment comment = commentService.getCommentById(model.getEntityId());
        User user = userService.getUserById(model.getActorId());
        message.setContent(
            "您的评论 "
                + comment.getContent().substring(0, 5)
                + "... 被用户 "
                + user.getName()
                + " 赞了一下！");
      } else if (model.getEntityType() == SettingUtil.ENTITY_QUESTION) {
        Question question = questionService.getQuestionById(model.getEntityId());
        User user = userService.getUserById(model.getActorId());
        message.setContent(
            "您的问题 " + question.getTitle().substring(0, 5) + "... 被用户 " + user.getName() + " 赞了一下！");
      }
    } else if (model.getType() == EventType.COMMENT_EVENT) {
      Question question = questionService.getQuestionById(model.getEntityId());
      User user = userService.getUserById(model.getActorId());
      message.setContent(
          "您的问题 " + question.getTitle().substring(0, 5) + "... 被用户 " + user.getName() + " 评论了！");
    }
    messageService.addMessage(message);
  }

  @Override
  public List<EventType> getSupportEventTypes() {
    return Arrays.asList(EventType.COMMENT_EVENT, EventType.LIKE_EVENT);
  }
}
