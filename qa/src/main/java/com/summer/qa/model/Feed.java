package com.summer.qa.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

@Data
public class Feed {
  private Integer id;

  private Date createdDate;

  private Integer userId;

  private String data;

  private Integer type;

  private JSONObject dataJSON = null;

  public void setData(String data) {
    this.data = data;
    dataJSON = JSONObject.parseObject(data);
  }

  public String get(String key) {
    return dataJSON == null ? null : dataJSON.getString(key);
  }
}
