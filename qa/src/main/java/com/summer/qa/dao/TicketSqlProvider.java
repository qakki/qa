package com.summer.qa.dao;

import com.summer.qa.model.Ticket;
import org.apache.ibatis.jdbc.SQL;

public class TicketSqlProvider {

  public String insertSelective(Ticket record) {
    SQL sql = new SQL();
    sql.INSERT_INTO("tb_ticket");

    if (record.getId() != null) {
      sql.VALUES("id", "#{id,jdbcType=INTEGER}");
    }

    if (record.getUserId() != null) {
      sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
    }

    if (record.getTicket() != null) {
      sql.VALUES("ticket", "#{ticket,jdbcType=VARCHAR}");
    }

    if (record.getExpired() != null) {
      sql.VALUES("expired", "#{expired,jdbcType=TIMESTAMP}");
    }

    if (record.getStatus() != null) {
      sql.VALUES("status", "#{status,jdbcType=INTEGER}");
    }

    return sql.toString();
  }

  public String updateByPrimaryKeySelective(Ticket record) {
    SQL sql = new SQL();
    sql.UPDATE("tb_ticket");

    if (record.getUserId() != null) {
      sql.SET("user_id = #{userId,jdbcType=INTEGER}");
    }

    if (record.getTicket() != null) {
      sql.SET("ticket = #{ticket,jdbcType=VARCHAR}");
    }

    if (record.getExpired() != null) {
      sql.SET("expired = #{expired,jdbcType=TIMESTAMP}");
    }

    if (record.getStatus() != null) {
      sql.SET("status = #{status,jdbcType=INTEGER}");
    }

    sql.WHERE("id = #{id,jdbcType=INTEGER}");

    return sql.toString();
  }
}
