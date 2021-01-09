package com.summer.qa.model;

import lombok.Data;

@Data
public class User {
  private Integer id;

  private String name;

  private String password;

  private String salt;

  private String headUrl;

  private Integer auth;
}
