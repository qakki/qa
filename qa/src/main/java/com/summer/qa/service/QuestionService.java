package com.summer.qa.service;

import com.summer.qa.model.Question;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/3 0003
 * @description：
 */
public interface QuestionService {

  List<Question> getLatestQuestions(int userId, int page, int limit);

  int addQuestion(Question question);

  Question getQuestionById(int id);
}
