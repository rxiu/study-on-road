package com.rxiu.email.core.server.sender;

import com.google.common.base.Strings;
import com.rxiu.email.core.EMail;
import com.rxiu.email.core.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by shenyuhang on 2018/3/13.
 */
@Component
public class MailSender {

    @Autowired
    JavaMailSender sender;

    @Autowired
    MailProperties prop;

    public void sendSimpleMail(EMail email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(prop.getUsername());
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getText());

        if (email.getCc()!= null && email.getCc().length > 0) {
            message.setCc(email.getCc());
        }

        sender.send(message);
    }


    public void sendMultiMail(EMail email) {
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(prop.getUsername());
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText(), true);

            if(!Strings.isNullOrEmpty(email.getFileName())) {
                File file = new File(email.getFileName());
                String fileName = email.getFileName().substring(email.getFileName().lastIndexOf(File.separator));
                helper.addAttachment(fileName, file);
            }
            //
            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
