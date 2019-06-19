package com.summer.qa.dao;

import com.summer.qa.model.Question;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {

  @Delete({"delete from tb_question", "where id = #{id,jdbcType=INTEGER}"})
  int deleteByPrimaryKey(Integer id);

  @Insert({
    "insert into tb_question (id, title, ",
    "user_id, created_date, ",
    "comment_count, is_del, ",
    "content)",
    "values (#{id,jdbcType=INTEGER}, #{title,jdbcType=VARCHAR}, ",
    "#{userId,jdbcType=INTEGER}, #{createdDate,jdbcType=TIMESTAMP}, ",
    "#{commentCount,jdbcType=INTEGER}, #{isDel,jdbcType=INTEGER}, ",
    "#{content,jdbcType=LONGVARCHAR})"
  })
  int insert(Question record);

  @InsertProvider(type = QuestionSqlProvider.class, method = "insertSelective")
  int insertSelective(Question record);

  @Select({
    "select",
    "id, title, user_id, created_date, comment_count, is_del, content",
    "from tb_question",
    "where id = #{id,jdbcType=INTEGER}"
  })
  @Results({
    @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
    @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
    @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
    @Result(column = "created_date", property = "createdDate", jdbcType = JdbcType.TIMESTAMP),
    @Result(column = "comment_count", property = "commentCount", jdbcType = JdbcType.INTEGER),
    @Result(column = "is_del", property = "isDel", jdbcType = JdbcType.INTEGER),
    @Result(column = "content", property = "content", jdbcType = JdbcType.LONGVARCHAR)
  })
  Question selectByPrimaryKey(Integer id);

  @UpdateProvider(type = QuestionSqlProvider.class, method = "updateByPrimaryKeySelective")
  int updateByPrimaryKeySelective(Question record);

  @Update({
    "update tb_question",
    "set title = #{title,jdbcType=VARCHAR},",
    "user_id = #{userId,jdbcType=INTEGER},",
    "created_date = #{createdDate,jdbcType=TIMESTAMP},",
    "comment_count = #{commentCount,jdbcType=INTEGER},",
    "is_del = #{isDel,jdbcType=INTEGER},",
    "content = #{content,jdbcType=LONGVARCHAR}",
    "where id = #{id,jdbcType=INTEGER}"
  })
  int updateByPrimaryKeyWithBLOBs(Question record);

  @Update({
    "update tb_question",
    "set title = #{title,jdbcType=VARCHAR},",
    "user_id = #{userId,jdbcType=INTEGER},",
    "created_date = #{createdDate,jdbcType=TIMESTAMP},",
    "comment_count = #{commentCount,jdbcType=INTEGER},",
    "is_del = #{isDel,jdbcType=INTEGER}",
    "where id = #{id,jdbcType=INTEGER}"
  })
  int updateByPrimaryKey(Question record);

  @Results({
    @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
    @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
    @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
    @Result(column = "created_date", property = "createdDate", jdbcType = JdbcType.TIMESTAMP),
    @Result(column = "comment_count", property = "commentCount", jdbcType = JdbcType.INTEGER),
    @Result(column = "is_del", property = "isDel", jdbcType = JdbcType.INTEGER),
    @Result(column = "content", property = "content", jdbcType = JdbcType.LONGVARCHAR)
  })
  @SelectProvider(type = QuestionSqlProvider.class, method = "selectByIdAndTimeDesc")
  List<Question> selectByIdAndTimeDesc(@Param("userId") Integer userId);

  @SelectProvider(type = QuestionSqlProvider.class, method = "selectQuestionCount")
  int selectQuestionCount(@Param("userId") Integer userId);
}
