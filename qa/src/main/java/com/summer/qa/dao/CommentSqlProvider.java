package com.summer.qa.dao;

import com.summer.qa.model.Comment;
import org.apache.ibatis.jdbc.SQL;

public class CommentSqlProvider {

  public String insertSelective(Comment record) {
    SQL sql = new SQL();
    sql.INSERT_INTO("tb_comment");

    if (record.getId() != null) {
      sql.VALUES("id", "#{id,jdbcType=INTEGER}");
    }

    if (record.getUserId() != null) {
      sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
    }

    if (record.getEntityId() != null) {
      sql.VALUES("entity_id", "#{entityId,jdbcType=INTEGER}");
    }

    if (record.getEntityType() != null) {
      sql.VALUES("entity_type", "#{entityType,jdbcType=INTEGER}");
    }

    if (record.getAddTime() != null) {
      sql.VALUES("add_time", "#{addTime,jdbcType=TIMESTAMP}");
    }

    if (record.getIsDel() != null) {
      sql.VALUES("is_del", "#{isDel,jdbcType=INTEGER}");
    }

    if (record.getContent() != null) {
      sql.VALUES("content", "#{content,jdbcType=LONGVARCHAR}");
    }

    return sql.toString();
  }

  public String updateByPrimaryKeySelective(Comment record) {
    SQL sql = new SQL();
    sql.UPDATE("tb_comment");

    if (record.getUserId() != null) {
      sql.SET("user_id = #{userId,jdbcType=INTEGER}");
    }

    if (record.getEntityId() != null) {
      sql.SET("entity_id = #{entityId,jdbcType=INTEGER}");
    }

    if (record.getEntityType() != null) {
      sql.SET("entity_type = #{entityType,jdbcType=INTEGER}");
    }

    if (record.getAddTime() != null) {
      sql.SET("add_time = #{addTime,jdbcType=TIMESTAMP}");
    }

    if (record.getIsDel() != null) {
      sql.SET("is_del = #{isDel,jdbcType=INTEGER}");
    }

    if (record.getContent() != null) {
      sql.SET("content = #{content,jdbcType=LONGVARCHAR}");
    }

    sql.WHERE("id = #{id,jdbcType=INTEGER}");

    return sql.toString();
  }
}
