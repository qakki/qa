package com.summer.qa.service.Impl;

import com.github.pagehelper.PageHelper;
import com.summer.qa.dao.MessageMapper;
import com.summer.qa.model.Message;
import com.summer.qa.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/6 0006
 * @description：
 */
@Service
public class MessageServiceImpl implements MessageService {

  @Autowired private MessageMapper messageMapper;

  @Override
  public int addMessage(Message message) {
    return messageMapper.insertSelective(message) > 0 ? message.getId() : 0;
  }

  @Override
  public List<Message> getConversationList(int userId, int page, int size) {
    PageHelper.startPage(page, size);
    return messageMapper.selectConversationListByUserId(userId);
  }

  @Override
  public int getConversationUnreadCount(int toId, String conversationId) {
    return messageMapper.selectConversationUnreadCount(toId, conversationId);
  }

  @Override
  public List<Message> getConversationDetail(String conversationId, int page, int size) {
    PageHelper.startPage(page, size);
    return messageMapper.selectMessageByConversationId(conversationId);
  }
}
