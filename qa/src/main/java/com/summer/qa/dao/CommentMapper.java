package com.summer.qa.dao;

import com.summer.qa.model.Comment;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommentMapper {
  @Delete({"delete from tb_comment", "where id = #{id,jdbcType=INTEGER}"})
  int deleteByPrimaryKey(Integer id);

  @Insert({
    "insert into tb_comment (id, user_id, ",
    "entity_id, entity_type, ",
    "add_time, is_del, ",
    "content)",
    "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, ",
    "#{entityId,jdbcType=INTEGER}, #{entityType,jdbcType=INTEGER}, ",
    "#{addTime,jdbcType=TIMESTAMP}, #{isDel,jdbcType=INTEGER}, ",
    "#{content,jdbcType=LONGVARCHAR})"
  })
  int insert(Comment record);

  @InsertProvider(type = CommentSqlProvider.class, method = "insertSelective")
  int insertSelective(Comment record);

  @Select({
    "select",
    "id, user_id, entity_id, entity_type, add_time, is_del, content",
    "from tb_comment",
    "where id = #{id,jdbcType=INTEGER}"
  })
  @Results({
    @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
    @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
    @Result(column = "entity_id", property = "entityId", jdbcType = JdbcType.INTEGER),
    @Result(column = "entity_type", property = "entityType", jdbcType = JdbcType.INTEGER),
    @Result(column = "add_time", property = "addTime", jdbcType = JdbcType.TIMESTAMP),
    @Result(column = "is_del", property = "isDel", jdbcType = JdbcType.INTEGER),
    @Result(column = "content", property = "content", jdbcType = JdbcType.LONGVARCHAR)
  })
  Comment selectByPrimaryKey(Integer id);

  @UpdateProvider(type = CommentSqlProvider.class, method = "updateByPrimaryKeySelective")
  int updateByPrimaryKeySelective(Comment record);

  @Update({
    "update tb_comment",
    "set user_id = #{userId,jdbcType=INTEGER},",
    "entity_id = #{entityId,jdbcType=INTEGER},",
    "entity_type = #{entityType,jdbcType=INTEGER},",
    "add_time = #{addTime,jdbcType=TIMESTAMP},",
    "is_del = #{isDel,jdbcType=INTEGER},",
    "content = #{content,jdbcType=LONGVARCHAR}",
    "where id = #{id,jdbcType=INTEGER}"
  })
  int updateByPrimaryKeyWithBLOBs(Comment record);

  @Update({
    "update tb_comment",
    "set user_id = #{userId,jdbcType=INTEGER},",
    "entity_id = #{entityId,jdbcType=INTEGER},",
    "entity_type = #{entityType,jdbcType=INTEGER},",
    "add_time = #{addTime,jdbcType=TIMESTAMP},",
    "is_del = #{isDel,jdbcType=INTEGER}",
    "where id = #{id,jdbcType=INTEGER}"
  })
  int updateByPrimaryKey(Comment record);
}
