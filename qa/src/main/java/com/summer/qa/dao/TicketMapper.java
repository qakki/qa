package com.summer.qa.dao;

import com.summer.qa.model.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TicketMapper {
  @Delete({"delete from tb_ticket", "where id = #{id,jdbcType=INTEGER}"})
  int deleteByPrimaryKey(Integer id);

  @Insert({
    "insert into tb_ticket (id, user_id, ",
    "ticket, expired, ",
    "status)",
    "values (#{id,jdbcType=INTEGER}, #{userId,jdbcType=INTEGER}, ",
    "#{ticket,jdbcType=VARCHAR}, #{expired,jdbcType=TIMESTAMP}, ",
    "#{status,jdbcType=INTEGER})"
  })
  int insert(Ticket record);

  @InsertProvider(type = TicketSqlProvider.class, method = "insertSelective")
  int insertSelective(Ticket record);

  @Select({
    "select",
    "id, user_id, ticket, expired, status",
    "from tb_ticket",
    "where id = #{id,jdbcType=INTEGER}"
  })
  @Results({
    @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
    @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
    @Result(column = "ticket", property = "ticket", jdbcType = JdbcType.VARCHAR),
    @Result(column = "expired", property = "expired", jdbcType = JdbcType.TIMESTAMP),
    @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER)
  })
  Ticket selectByPrimaryKey(Integer id);

  @UpdateProvider(type = TicketSqlProvider.class, method = "updateByPrimaryKeySelective")
  int updateByPrimaryKeySelective(Ticket record);

  @Update({
    "update tb_ticket",
    "set user_id = #{userId,jdbcType=INTEGER},",
    "ticket = #{ticket,jdbcType=VARCHAR},",
    "expired = #{expired,jdbcType=TIMESTAMP},",
    "status = #{status,jdbcType=INTEGER}",
    "where id = #{id,jdbcType=INTEGER}"
  })
  int updateByPrimaryKey(Ticket record);
}
