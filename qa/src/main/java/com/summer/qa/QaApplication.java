package com.summer.qa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;

@SpringBootApplication(exclude = SolrAutoConfiguration.class)
public class QaApplication {

  public static void main(String[] args) {
    SpringApplication.run(QaApplication.class, args);
  }
}
