package com.summer.qa.service;

import com.summer.qa.model.Question;

import java.util.List;

/**
 * @author ：lightingSummer
 * @date ：2019/6/21 0021
 * @description：
 */
public interface SolrService {

  List<Question> searchQuestion(String keyword, int offset, int count, String hlPre, String hlPos)
      throws Exception;

  boolean indexQuestion(int qid, String title, String content) throws Exception;
}
