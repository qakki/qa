package com.summer.qa.dao;

import com.summer.qa.model.Message;
import org.apache.ibatis.jdbc.SQL;

public class MessageSqlProvider {

  public String insertSelective(Message record) {
    SQL sql = new SQL();
    sql.INSERT_INTO("tb_message");

    if (record.getId() != null) {
      sql.VALUES("id", "#{id,jdbcType=INTEGER}");
    }

    if (record.getFromId() != null) {
      sql.VALUES("from_id", "#{fromId,jdbcType=INTEGER}");
    }

    if (record.getToId() != null) {
      sql.VALUES("to_id", "#{toId,jdbcType=INTEGER}");
    }

    if (record.getCreatedDate() != null) {
      sql.VALUES("created_date", "#{createdDate,jdbcType=TIMESTAMP}");
    }

    if (record.getHasRead() != null) {
      sql.VALUES("has_read", "#{hasRead,jdbcType=INTEGER}");
    }

    if (record.getConversationId() != null) {
      sql.VALUES("conversation_id", "#{conversationId,jdbcType=VARCHAR}");
    }

    if (record.getContent() != null) {
      sql.VALUES("content", "#{content,jdbcType=LONGVARCHAR}");
    }

    return sql.toString();
  }

  public String updateByPrimaryKeySelective(Message record) {
    SQL sql = new SQL();
    sql.UPDATE("tb_message");

    if (record.getFromId() != null) {
      sql.SET("from_id = #{fromId,jdbcType=INTEGER}");
    }

    if (record.getToId() != null) {
      sql.SET("to_id = #{toId,jdbcType=INTEGER}");
    }

    if (record.getCreatedDate() != null) {
      sql.SET("created_date = #{createdDate,jdbcType=TIMESTAMP}");
    }

    if (record.getHasRead() != null) {
      sql.SET("has_read = #{hasRead,jdbcType=INTEGER}");
    }

    if (record.getConversationId() != null) {
      sql.SET("conversation_id = #{conversationId,jdbcType=VARCHAR}");
    }

    if (record.getContent() != null) {
      sql.SET("content = #{content,jdbcType=LONGVARCHAR}");
    }

    sql.WHERE("id = #{id,jdbcType=INTEGER}");

    return sql.toString();
  }
}
