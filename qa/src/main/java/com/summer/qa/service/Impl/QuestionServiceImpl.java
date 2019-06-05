package com.summer.qa.service.Impl;

import com.github.pagehelper.PageHelper;
import com.summer.qa.dao.QuestionMapper;
import com.summer.qa.model.Question;
import com.summer.qa.service.QuestionService;
import com.summer.qa.service.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/3 0003
 * @description：
 */
@Service
public class QuestionServiceImpl implements QuestionService {

  @Autowired private QuestionMapper questionMapper;
  @Autowired private SensitiveService sensitiveService;

  /**
   * @author: lightingSummer
   * @date: 2019/6/3 0003
   * @description: 获取最新的question
   */
  @Override
  public List<Question> getLatestQuestions(int userId, int page, int limit) {
    PageHelper.startPage(page, limit);
    return questionMapper.selectByIdAndTimeDesc(userId);
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/5 0005
   * @description: 添加问题 返回主键
   */
  @Override
  public int addQuestion(Question question) {
    question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
    question.setContent(HtmlUtils.htmlEscape(question.getContent()));
    // 敏感词过滤
    question.setTitle(sensitiveService.filter(question.getTitle()));
    question.setContent(sensitiveService.filter(question.getContent()));
    return questionMapper.insertSelective(question) > 0 ? question.getId() : 0;
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/5 0005
   * @description: 根据主键获取Question
   */
  @Override
  public Question getQuestionById(int id) {
    return questionMapper.selectByPrimaryKey(id);
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/5 0005
   * @description : 更新Question评论数量
   */
  @Override
  public int updateCommentCount(int id, int commentCount) {
    Question question = new Question();
    question.setId(id);
    question.setCommentCount(commentCount);
    return questionMapper.updateByPrimaryKeySelective(question);
  }
}
