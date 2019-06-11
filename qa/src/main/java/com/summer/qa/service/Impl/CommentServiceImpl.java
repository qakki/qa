package com.summer.qa.service.Impl;

import com.summer.qa.dao.CommentMapper;
import com.summer.qa.model.Comment;
import com.summer.qa.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/5 0005
 * @description：
 */
@Service
public class CommentServiceImpl implements CommentService {

  @Autowired private CommentMapper commentMapper;

  @Override
  public int addComment(Comment comment) {
    return commentMapper.insertSelective(comment) > 0 ? comment.getId() : 0;
  }

  @Override
  public int getCommentCount(int entityType, int entityId) {
    return commentMapper.selectCountByEntity(entityType, entityId);
  }

  @Override
  public List<Comment> getCommentsByEntity(int entityType, int entityId) {
    return commentMapper.selectCommentsByEntity(entityType, entityId);
  }

  @Override
  public Comment getCommentById(int id) {
    return commentMapper.selectByPrimaryKey(id);
  }
}
