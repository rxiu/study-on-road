package com.rxiu.email.core;

import java.io.Serializable;

/**
 * Created by shenyuhang on 2018/3/13.
 */
public class EMail implements Serializable {

    private String[] to;
    private String[] cc;

    private String subject;
    private String text;
    private String fileName;
    private boolean isSimpleMail = true;

    public String[] getTo() {
        return to;
    }

    public EMail setTo(String[] to) {
        this.to = to;
        return this;
    }

    public String[] getCc() {
        return cc;
    }

    public EMail setCc(String[] cc) {
        this.cc = cc;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public EMail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getText() {
        return text;
    }

    public EMail setText(String text) {
        this.text = text;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public EMail setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public boolean isSimpleMail() {
        return isSimpleMail;
    }

    public EMail setSimpleMail(boolean simpleMail) {
        isSimpleMail = simpleMail;
        return this;
    }
}
