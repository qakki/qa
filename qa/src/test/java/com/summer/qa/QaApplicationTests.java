package com.summer.qa;

import com.summer.qa.dao.QuestionMapper;
import com.summer.qa.dao.UserMapper;
import com.summer.qa.model.Question;
import com.summer.qa.model.User;
import com.summer.qa.service.QuestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QaApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void userTest(){
        User user=new User();
        user.setName("鹧鸪天");
        user.setSalt("123");
        user.setPassword("123");
        user.setHeadUrl("http://ps2a1gxx1.bkt.clouddn.com/44188a19512f4bd6b022886877901705.jpg");
        userMapper.insertSelective(user);
    }

    @Test
    public void questionTest(){
        /*
        for(int i=0;i<10;i++){
            Question question=new Question();
            question.setTitle("Question"+i);
            question.setUserId(1);
            question.setCreatedDate(new Date());
            question.setContent("content");
            questionMapper.insertSelective(question);
        }*/
        System.out.println(questionService.getLatestQuestions(1,1,5));
    }

}
