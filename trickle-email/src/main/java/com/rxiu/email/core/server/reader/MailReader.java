package com.rxiu.email.core.server.reader;

import com.rxiu.email.core.EMail;
import com.rxiu.email.core.MailQueue;
import com.rxiu.email.core.server.sender.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rxiu on 2018/3/13.
 */
@Component
public class MailReader {

    @Autowired
    MailSender sender;

    private ExecutorService executor;

    public MailReader() {
        executor = Executors.newScheduledThreadPool(3);
    }

    public void start() {
        executor.execute(new PullMail());
    }

    private class PullMail implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    if (MailQueue.builder().size() > 0) {
                        EMail email = MailQueue.builder().pull();
                        if (email != null) {
                            if (email.isSimpleMail()) {
                                MailReader.this.sender.sendSimpleMail(email);
                            } else {
                                MailReader.this.sender.sendMultiMail(email);
                            }

                        }
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
