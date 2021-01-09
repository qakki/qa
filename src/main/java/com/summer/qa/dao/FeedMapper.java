package com.summer.qa.dao;

import com.summer.qa.model.Feed;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FeedMapper {
  @Delete({"delete from tb_feed", "where id = #{id,jdbcType=INTEGER}"})
  int deleteByPrimaryKey(Integer id);

  @Insert({
    "insert into tb_feed (id, created_date, ",
    "user_id, data, type)",
    "values (#{id,jdbcType=INTEGER}, #{createdDate,jdbcType=TIMESTAMP}, ",
    "#{userId,jdbcType=INTEGER}, #{data,jdbcType=VARCHAR}, #{type,jdbcType=INTEGER})"
  })
  int insert(Feed record);

  @InsertProvider(type = FeedSqlProvider.class, method = "insertSelective")
  int insertSelective(Feed record);

  @Select({
    "select",
    "id, created_date, user_id, data, type",
    "from tb_feed",
    "where id = #{id,jdbcType=INTEGER}"
  })
  @Results({
    @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
    @Result(column = "created_date", property = "createdDate", jdbcType = JdbcType.TIMESTAMP),
    @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER),
    @Result(column = "data", property = "data", jdbcType = JdbcType.VARCHAR),
    @Result(column = "type", property = "type", jdbcType = JdbcType.INTEGER)
  })
  Feed selectByPrimaryKey(Integer id);

  @UpdateProvider(type = FeedSqlProvider.class, method = "updateByPrimaryKeySelective")
  int updateByPrimaryKeySelective(Feed record);

  @Update({
    "update tb_feed",
    "set created_date = #{createdDate,jdbcType=TIMESTAMP},",
    "user_id = #{userId,jdbcType=INTEGER},",
    "data = #{data,jdbcType=VARCHAR},",
    "type = #{type,jdbcType=INTEGER}",
    "where id = #{id,jdbcType=INTEGER}"
  })
  int updateByPrimaryKey(Feed record);

  @SelectProvider(type = FeedSqlProvider.class, method = "selectUserFeeds")
  List<Feed> selectUserFeeds(
      @Param("maxId") int maxId,
      @Param("userIds") List<Integer> userIds,
      @Param("count") int count);
}
