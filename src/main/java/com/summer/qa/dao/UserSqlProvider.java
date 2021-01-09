package com.summer.qa.dao;

import com.summer.qa.model.User;
import org.apache.ibatis.jdbc.SQL;

public class UserSqlProvider {

  public String insertSelective(User record) {
    SQL sql = new SQL();
    sql.INSERT_INTO("tb_user");

    if (record.getId() != null) {
      sql.VALUES("id", "#{id,jdbcType=INTEGER}");
    }

    if (record.getName() != null) {
      sql.VALUES("name", "#{name,jdbcType=VARCHAR}");
    }

    if (record.getPassword() != null) {
      sql.VALUES("password", "#{password,jdbcType=VARCHAR}");
    }

    if (record.getSalt() != null) {
      sql.VALUES("salt", "#{salt,jdbcType=VARCHAR}");
    }

    if (record.getHeadUrl() != null) {
      sql.VALUES("head_url", "#{headUrl,jdbcType=VARCHAR}");
    }

    if (record.getAuth() != null) {
      sql.VALUES("auth", "#{auth,jdbcType=INTEGER}");
    }

    return sql.toString();
  }

  public String updateByPrimaryKeySelective(User record) {
    SQL sql = new SQL();
    sql.UPDATE("tb_user");

    if (record.getName() != null) {
      sql.SET("name = #{name,jdbcType=VARCHAR}");
    }

    if (record.getPassword() != null) {
      sql.SET("password = #{password,jdbcType=VARCHAR}");
    }

    if (record.getSalt() != null) {
      sql.SET("salt = #{salt,jdbcType=VARCHAR}");
    }

    if (record.getHeadUrl() != null) {
      sql.SET("head_url = #{headUrl,jdbcType=VARCHAR}");
    }

    if (record.getAuth() != null) {
      sql.SET("auth = #{auth,jdbcType=INTEGER}");
    }

    sql.WHERE("id = #{id,jdbcType=INTEGER}");

    return sql.toString();
  }
}
