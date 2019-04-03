package com.rxiu.flume.sink.taildir;

import com.google.common.collect.Maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Map;


/**
 * Created by Jenner on 2015/4/9.
 * <p>
 * 修正org.apache.flume.formatter.output.PathManager在移动文件时可能出现文件名重复的问题
 */
public class SafePathManager {
    private File baseDirectory;

    public Map<String, RandomAccessFile> currentRFs = Maps.newHashMapWithExpectedSize(10);
    public Map<String, File> currentFiles = Maps.newHashMapWithExpectedSize(10);

    public SafePathManager() {
    }

    /*    public File getCurrentFile(String newpathString, String rootPath) {
            if (currentFiles.get(newpathString) == null && rootPath != null) {
                int begin = newpathString.indexOf(rootPath) + rootPath.length();
                int end = newpathString.lastIndexOf("\\");
                String fileName = newpathString.substring(end, newpathString.length());
                File newpath = new File(baseDirectory.getPath() + newpathString.substring(begin, end));
                if (newpath.exists()) {
                    currentFiles.put(newpathString, new File(baseDirectory.getPath() + newpathString.substring(begin, end) + fileName));
                } else {
                    boolean mkdir = newpath.mkdirs();
                    if (mkdir) {
                        currentFiles.put(newpathString, new File(baseDirectory.getPath() + newpathString.substring(begin, end) + fileName));
                    } else {
                        currentFiles.put(newpathString, new File(baseDirectory.getPath() + fileName));
                    }
                }
            }
            return currentFiles.get(newpathString);
        }*/
    public RandomAccessFile getRandomAccessFile(String newpathString, String rootPath) {
        if (currentFiles.get(newpathString) == null && rootPath != null) {
            try {
                int begin = newpathString.indexOf(rootPath) + rootPath.length();
                int end = newpathString.lastIndexOf("/");
                String fileName = newpathString.substring(end);
                File newpath = new File(baseDirectory.getPath() + newpathString.substring(begin, end));
                if (newpath.exists()) {
                    File file = new File(baseDirectory.getPath() + newpathString.substring(begin, end) + fileName);
                    currentRFs.put(newpathString, new RandomAccessFile(file, "rw"));
                    currentFiles.put(newpathString, file);

                } else {
                    boolean mkdir = newpath.mkdirs();
                    if (mkdir) {
                        File file = new File(baseDirectory.getPath() + newpathString.substring(begin, end) + fileName);
                        currentRFs.put(newpathString, new RandomAccessFile(file, "rw"));
                        currentFiles.put(newpathString, file);
                    } else {
                        File file = new File(baseDirectory.getPath() + fileName);
                        currentRFs.put(newpathString, new RandomAccessFile(file, "rw"));
                        currentFiles.put(newpathString, file);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return currentRFs.get(newpathString);
    }
    
    public File getCurrentFile(String newpathString) {
        return currentFiles.get(newpathString);
    }

    public void rotate(String pathString) {
        currentFiles.remove(pathString);
        currentRFs.remove(pathString);
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
}