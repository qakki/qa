package com.summer.qa.service;

import com.summer.qa.model.Comment;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/5 0005
 * @description：
 */
public interface CommentService {
  int addComment(Comment comment);

  int getCommentCount(int entityType, int entityId);

  List<Comment> getCommentsByEntity(int entityType, int entityId);

  Comment getCommentById(int id);

  int getUserCommentCount(int userId);
}
