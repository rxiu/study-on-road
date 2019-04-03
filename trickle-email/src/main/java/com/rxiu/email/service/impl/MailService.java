package com.rxiu.email.service.impl;

import com.rxiu.email.core.EMail;
import com.rxiu.email.core.MailQueue;
import com.rxiu.email.service.IMailService;
import org.springframework.stereotype.Service;

/**
 * Created by shenyuhang on 2018/3/13.
 */
@Service
public class MailService implements IMailService {

    @Override
    public void send(EMail email) {
        try {
            MailQueue.builder().push(email);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
