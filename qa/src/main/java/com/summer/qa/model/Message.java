package com.summer.qa.model;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Integer id;

    private Integer fromId;

    private Integer toId;

    private Date createdDate;

    private Integer hasRead;

    private String conversationId;

    private String content;

}