package com.summer.qa.dao;

import com.summer.qa.model.Feed;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class FeedSqlProvider {

  public String insertSelective(Feed record) {
    SQL sql = new SQL();
    sql.INSERT_INTO("tb_feed");

    if (record.getId() != null) {
      sql.VALUES("id", "#{id,jdbcType=INTEGER}");
    }

    if (record.getCreatedDate() != null) {
      sql.VALUES("created_date", "#{createdDate,jdbcType=TIMESTAMP}");
    }

    if (record.getUserId() != null) {
      sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
    }

    if (record.getData() != null) {
      sql.VALUES("data", "#{data,jdbcType=VARCHAR}");
    }

    if (record.getType() != null) {
      sql.VALUES("type", "#{type,jdbcType=INTEGER}");
    }

    return sql.toString();
  }

  public String updateByPrimaryKeySelective(Feed record) {
    SQL sql = new SQL();
    sql.UPDATE("tb_feed");

    if (record.getCreatedDate() != null) {
      sql.SET("created_date = #{createdDate,jdbcType=TIMESTAMP}");
    }

    if (record.getUserId() != null) {
      sql.SET("user_id = #{userId,jdbcType=INTEGER}");
    }

    if (record.getData() != null) {
      sql.SET("data = #{data,jdbcType=VARCHAR}");
    }

    if (record.getType() != null) {
      sql.SET("type = #{type,jdbcType=INTEGER}");
    }

    sql.WHERE("id = #{id,jdbcType=INTEGER}");

    return sql.toString();
  }

  public String selectUserFeeds(
      @Param("maxId") int maxId,
      @Param("userIds") List<Integer> userIds,
      @Param("count") int count) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        "select id, created_date, user_id, data, type " + " from tb_feed " + " where id<#{maxId} ");
    if (userIds.size() != 0) {
      sb.append(" and user_id in ( ");
      for (int userId : userIds) {
        sb.append(userId + ",");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append(")");
    }
    sb.append(" order by id desc limit #{count}");
    return sb.toString();
  }
}
