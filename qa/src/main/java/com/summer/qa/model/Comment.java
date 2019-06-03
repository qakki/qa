package com.summer.qa.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
public class Comment {
    private Integer id;

    private Integer userId;

    private Integer entityId;

    private Integer entityType;

    private Date addTime;

    private Integer isDel;

    private String content;

}