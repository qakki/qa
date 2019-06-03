package com.summer.qa.dao;

import com.summer.qa.model.Message;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MessageMapper {
    @Delete({
        "delete from tb_message",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into tb_message (id, from_id, ",
        "to_id, created_date, ",
        "has_read, conversation_id, ",
        "content)",
        "values (#{id,jdbcType=INTEGER}, #{fromId,jdbcType=INTEGER}, ",
        "#{toId,jdbcType=INTEGER}, #{createdDate,jdbcType=TIMESTAMP}, ",
        "#{hasRead,jdbcType=INTEGER}, #{conversationId,jdbcType=VARCHAR}, ",
        "#{content,jdbcType=LONGVARCHAR})"
    })
    int insert(Message record);

    @InsertProvider(type=MessageSqlProvider.class, method="insertSelective")
    int insertSelective(Message record);

    @Select({
        "select",
        "id, from_id, to_id, created_date, has_read, conversation_id, content",
        "from tb_message",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="from_id", property="fromId", jdbcType=JdbcType.INTEGER),
        @Result(column="to_id", property="toId", jdbcType=JdbcType.INTEGER),
        @Result(column="created_date", property="createdDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="has_read", property="hasRead", jdbcType=JdbcType.INTEGER),
        @Result(column="conversation_id", property="conversationId", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.LONGVARCHAR)
    })
    Message selectByPrimaryKey(Integer id);

    @UpdateProvider(type=MessageSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Message record);

    @Update({
        "update tb_message",
        "set from_id = #{fromId,jdbcType=INTEGER},",
          "to_id = #{toId,jdbcType=INTEGER},",
          "created_date = #{createdDate,jdbcType=TIMESTAMP},",
          "has_read = #{hasRead,jdbcType=INTEGER},",
          "conversation_id = #{conversationId,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKeyWithBLOBs(Message record);

    @Update({
        "update tb_message",
        "set from_id = #{fromId,jdbcType=INTEGER},",
          "to_id = #{toId,jdbcType=INTEGER},",
          "created_date = #{createdDate,jdbcType=TIMESTAMP},",
          "has_read = #{hasRead,jdbcType=INTEGER},",
          "conversation_id = #{conversationId,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Message record);
}