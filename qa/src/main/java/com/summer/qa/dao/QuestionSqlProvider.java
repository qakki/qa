package com.summer.qa.dao;

import com.summer.qa.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class QuestionSqlProvider {

    public String selectByIdAndTimeDesc(@Param("userId") Integer userId) {
        StringBuilder sb=new StringBuilder();
        sb.append("select " +
                " id, title, user_id, created_date, comment_count, is_del, content " +
                " from  tb_question " +
                " where ");
        if(userId!=0){
            sb.append(" user_id = #{userId} and ");
        }
        sb.append(" is_del = 0 order by id desc ");
        return sb.toString();
    }

    public String insertSelective(Question record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("tb_question");

        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getTitle() != null) {
            sql.VALUES("title", "#{title,jdbcType=VARCHAR}");
        }

        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }

        if (record.getCreatedDate() != null) {
            sql.VALUES("created_date", "#{createdDate,jdbcType=TIMESTAMP}");
        }

        if (record.getCommentCount() != null) {
            sql.VALUES("comment_count", "#{commentCount,jdbcType=INTEGER}");
        }

        if (record.getIsDel() != null) {
            sql.VALUES("is_del", "#{isDel,jdbcType=INTEGER}");
        }

        if (record.getContent() != null) {
            sql.VALUES("content", "#{content,jdbcType=LONGVARCHAR}");
        }

        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Question record) {
        SQL sql = new SQL();
        sql.UPDATE("tb_question");

        if (record.getTitle() != null) {
            sql.SET("title = #{title,jdbcType=VARCHAR}");
        }

        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=INTEGER}");
        }

        if (record.getCreatedDate() != null) {
            sql.SET("created_date = #{createdDate,jdbcType=TIMESTAMP}");
        }

        if (record.getCommentCount() != null) {
            sql.SET("comment_count = #{commentCount,jdbcType=INTEGER}");
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