package com.summer.qa.service;

import com.summer.qa.model.Message;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/6 0006
 * @description：
 */
public interface MessageService {
  int addMessage(Message message);

  List<Message> getConversationList(int userId, int page, int size);

  int getConversationUnreadCount(int toId, String conversationId);

  List<Message> getConversationDetail(String conversationId, int page, int size);
}
