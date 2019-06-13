package com.summer.qa.dao;

import com.summer.qa.model.Message;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {
  @Delete({"delete from tb_message", "where id = #{id,jdbcType=INTEGER}"})
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

  @InsertProvider(type = MessageSqlProvider.class, method = "insertSelective")
  int insertSelective(Message record);

  @Select({
    "select",
    "id, from_id, to_id, created_date, has_read, conversation_id, content",
    "from tb_message",
    "where id = #{id,jdbcType=INTEGER}"
  })
  @Results({
    @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
    @Result(column = "from_id", property = "fromId", jdbcType = JdbcType.INTEGER),
    @Result(column = "to_id", property = "toId", jdbcType = JdbcType.INTEGER),
    @Result(column = "created_date", property = "createdDate", jdbcType = JdbcType.TIMESTAMP),
    @Result(column = "has_read", property = "hasRead", jdbcType = JdbcType.INTEGER),
    @Result(column = "conversation_id", property = "conversationId", jdbcType = JdbcType.VARCHAR),
    @Result(column = "content", property = "content", jdbcType = JdbcType.LONGVARCHAR)
  })
  Message selectByPrimaryKey(Integer id);

  @UpdateProvider(type = MessageSqlProvider.class, method = "updateByPrimaryKeySelective")
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

  // 坑人的5.7版本
  @Select({
    "select cnt as id,  from_id, to_id, created_date, has_read, conversation_id, content ",
    "from ",
    "(select id, from_id, to_id, created_date, has_read, conversation_id, content "
        + "from tb_message where from_id=#{userId} or to_id=#{userId} "
        + "order by id desc "
        + ") tt",
    "INNER JOIN( select max(id) d,count(0) as cnt  from tb_message GROUP BY conversation_id) a on tt.id=a.d",
    "group by conversation_id order by created_date desc"
  })
  List<Message> selectConversationListByUserId(int userId);

  @Select({
    "select",
    "count(0)",
    "from tb_message",
    "where conversation_id = #{conversationId} and to_id=#{toId} and has_read=0"
  })
  int selectConversationUnreadCount(
      @Param("toId") int toId, @Param("conversationId") String conversationId);

  @Select({
    "select",
    "id, from_id, to_id, created_date, has_read, conversation_id, content",
    "from tb_message",
    "where conversation_id = #{conversationId}",
    "order by id desc"
  })
  List<Message> selectMessageByConversationId(String conversationId);
}
