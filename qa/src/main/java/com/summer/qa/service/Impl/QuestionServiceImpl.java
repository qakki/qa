package com.summer.qa.service.Impl;

import com.github.pagehelper.PageHelper;
import com.summer.qa.dao.QuestionMapper;
import com.summer.qa.model.Question;
import com.summer.qa.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/3 0003
 * @description：
 */
@Service
public class QuestionServiceImpl implements QuestionService {

  @Autowired private QuestionMapper questionMapper;

  /**
   * @author: lightingSummer
   * @date: 2019/6/3 0003
   * @description: 获取最新的question
   * @return java.util.List<com.summer.qa.model.Question>
   */
  @Override
  public List<Question> getLatestQuestions(int userId, int page, int limit) {
    PageHelper.startPage(page, limit);
    return questionMapper.selectByIdAndTimeDesc(userId);
  }
}
