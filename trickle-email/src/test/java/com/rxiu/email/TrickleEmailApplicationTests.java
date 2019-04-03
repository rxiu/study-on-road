package com.rxiu.email;

import com.rxiu.email.core.EMail;
import com.rxiu.email.core.server.reader.MailReader;
import com.rxiu.email.service.IMailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author rxiu
 * @date 2019/4/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrickleEmailApplication.class)
public class TrickleEmailApplicationTests {

    @Autowired
    IMailService mailService;

    @Autowired
    MailReader reader;

    @Test
    public void run() throws InterruptedException {

        reader.start();

        EMail email = new EMail();
        email.setCc(null)
                .setTo(new String[]{"123@qq.com"})
                .setSubject("TEST SPRING BOOT MAIL")
                .setText("this is a test letter for spring boot mail.")
                .setSimpleMail(true);

        mailService.send(email);
    }

}