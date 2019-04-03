package com.rxiu.email.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shenyuhang on 2018/3/13.
 */
public class MailQueue {

    static BlockingQueue<EMail> queue = new LinkedBlockingQueue<EMail>(1000);

    private static class Holder {
        private static MailQueue queue = new MailQueue();
    }

    public static MailQueue builder() {
        return Holder.queue;
    }

    public void push(EMail email) throws InterruptedException {
        queue.put(email);
    }

    public EMail pull() throws InterruptedException {
        return queue.take();
    }

    public int size() {
        return queue.size();
    }
}
