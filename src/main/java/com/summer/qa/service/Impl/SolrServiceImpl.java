package com.summer.qa.service.Impl;

import com.summer.qa.model.Question;
import com.summer.qa.service.SolrService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：lightingSummer
 * @date ：2019/6/21 0021
 * @description：
 */
@Service
public class SolrServiceImpl implements SolrService {
  private static final String SOLR_URL = "http://139.196.215.221:8080/solr8/new_core";
  private static final String QUESTION_TITLE_FIELD = "question_title";
  private static final String QUESTION_CONTENT_FIELD = "question_content";
  private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();

  @Override
  public List<Question> searchQuestion(
      String keyword, int offset, int count, String hlPre, String hlPos) throws Exception {
    List<Question> questionList = new ArrayList<>();
    SolrQuery query = new SolrQuery(keyword);
    query.setRows(count);
    query.setStart(offset);
    query.setHighlight(true);
    query.setHighlightSimplePre(hlPre);
    query.setHighlightSimplePost(hlPos);
    query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
    QueryResponse response = client.query(query);
    for (Map.Entry<String, Map<String, List<String>>> entry :
        response.getHighlighting().entrySet()) {
      Question question = new Question();
      question.setId(Integer.valueOf(entry.getKey()));
      if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
        List<String> title = entry.getValue().get(QUESTION_TITLE_FIELD);
        question.setTitle(title.get(0));
      }
      if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
        List<String> content = entry.getValue().get(QUESTION_CONTENT_FIELD);
        question.setContent(content.get(0));
      }
      questionList.add(question);
    }
    return questionList;
  }

  @Override
  public boolean indexQuestion(int qid, String title, String content) throws Exception {
    SolrInputDocument doc = new SolrInputDocument();
    doc.setField("id", qid);
    doc.setField(QUESTION_TITLE_FIELD, title);
    doc.setField(QUESTION_CONTENT_FIELD, content);
    UpdateResponse response = client.add(doc, 1000);
    return response != null && response.getStatus() == 0;
  }
}
