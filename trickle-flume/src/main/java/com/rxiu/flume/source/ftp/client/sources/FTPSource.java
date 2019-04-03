/*
 * KEEDIO
 */
package com.rxiu.flume.source.ftp.client.sources;

import com.rxiu.flume.source.ftp.client.KeedioSource;
import com.rxiu.flume.source.ftp.client.filters.KeedioFileFilter;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Luis Lázaro lalazaro@keedio.com Keedio
 */
public class FTPSource extends KeedioSource<FTPFile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPSource.class);
    private FTPClient ftpClient = new FTPClient();

    /**
     * @return boolean Opens a Socket connected to a server and login to return
     * True if successfully completed, false if not.
     */
    @Override
    public boolean connect() {
        setConnected(true);
        try {
            getFtpClient().connect(getServer(), getPort());
            int replyCode = getFtpClient().getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                getFtpClient().disconnect();
                LOGGER.error("Connect Failed due to FTP, server refused connection.");
                this.setConnected(false);
            }

            if (!(ftpClient.login(user, password))) {
                LOGGER.error("Could not login to the server");
                this.setConnected(false);
            }

            ftpClient.enterLocalPassiveMode();
            ftpClient.setControlKeepAliveTimeout(300);
            if (getWorkingDirectory() != null) {
                getFtpClient().changeWorkingDirectory(getWorkingDirectory());
            }

            if (getBufferSize() != null) {
                getFtpClient().setBufferSize(getBufferSize());
            }

        } catch (IOException e) {
            this.setConnected(false);
            LOGGER.error("", e);
        }
        return isConnected();
    }

    /**
     * Disconnect and logout from current connection to server
     */
    @Override
    public void disconnect() {
        try {
            getFtpClient().logout();
            getFtpClient().disconnect();
            setConnected(false);

        } catch (IOException e) {
            LOGGER.error("Source " + this.getClass().getName() + " failed disconnect", e);
        }
    }

    @Override
    /**
     * @return void
     * @param String destination
     */
    public void changeToDirectory(String dir) throws IOException {
        ftpClient.changeWorkingDirectory(dir);
    }

    @Override
    /**
     * @return list with objects in directory
     * @param current directory
     */
    public List<FTPFile> listElements(String dir) throws IOException {
        FTPFile[] subFiles = getFtpClient().listFiles(dir);
        return Arrays.asList(subFiles);
    }

    @Override
    /**
     * @param Object
     * @return InputStream
     */
    public InputStream getInputStream(FTPFile file) throws IOException {
        if (isFlushLines()) {
            this.setFileType(FTP.ASCII_FILE_TYPE);
        } else {
            this.setFileType(FTP.BINARY_FILE_TYPE);
        }
        return getFtpClient().retrieveFileStream(new String(file.getName().getBytes("gbk"), "ISO-8859-1"));
    }

    @Override
    /**
     * @return name of the file
     * @param object as file
     */
    public String getObjectName(FTPFile file) {
        return file.getName();
    }

    @Override
    /**
     * @return boolean
     * @param Object to check
     */
    public long getModifiedTime(FTPFile file) {
        return file.getTimestamp().getTimeInMillis();
    }

    @Override
    /**
     * @return boolean
     * @param FTPFile to check
     */
    public boolean isDirectory(FTPFile file) {
        return file.isDirectory();
    }

    @Override
    /**
     * @param FTPFile
     * @return boolean
     */
    public boolean isFile(FTPFile file) {
        return file.isFile();
    }

    /**
     * This method calls completePendigCommand, mandatory for FTPClient
     *
     * @return boolean
     * @see <a href="http://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html#completePendingCommand()">completePendigCommmand</a>
     */
    @Override
    public boolean particularCommand() {
        boolean success = true;
        try {
            success = getFtpClient().completePendingCommand();
        } catch (IOException e) {
            LOGGER.error("Error on command completePendingCommand of FTPClient", e);
        }
        return success;
    }

    @Override
    /**
     * @return long size
     * @param object file
     */
    public long getObjectSize(FTPFile file) {
        return file.getSize();
    }

    @Override
    /**
     * @return boolean is a link
     * @param object as file
     */
    public boolean isLink(FTPFile file) {
        return file.isSymbolicLink();
    }

    @Override
    /**
     * @return String name of the link
     * @param object as file
     */
    public String getLink(FTPFile file) {
        return file.getLink();
    }

    /**
     * @return String directory retrieved for server on connect
     * @throws IOException
     */
    @Override
    public String getDirectoryserver() throws IOException {
        return getFtpClient().printWorkingDirectory();
    }

    /**
     * @return the ftpClient
     */
    public FTPClient getFtpClient() {
        return ftpClient;
    }

    /**
     * @param ftpClient the ftpClient to set
     */
    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    /**
     * @return object as cliente of ftpsource
     */
    @Override
    public Object getClientSource() {
        return ftpClient;
    }

    @Override
    public void setFileType(int fileType) throws IOException {
        ftpClient.setFileType(fileType);
    }

    @Override
    public List<FTPFile> listElements(String dirToList, KeedioFileFilter filter) throws IOException {
        List<FTPFile> list = new ArrayList<>();
        FTPFile[] subFiles = getFtpClient().listFiles(dirToList, filter);
        for (FTPFile ftpFile : subFiles) {
            byte[] bytes = ftpFile.getName().getBytes("iso-8859-1");
            ftpFile.setName(new String(bytes, "gbk"));
        }
        list = Arrays.asList(subFiles);
        return list;
    }

} //endclass
