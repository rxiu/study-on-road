/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.rxiu.flume.source.taildir;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rxiu.flume.source.taildir.TaildirSourceConfigurationConstants.*;

public class TailFile {
    private static final Logger logger = LoggerFactory.getLogger(TailFile.class);

    private static final byte BYTE_NL = (byte) 10;
    private static final byte BYTE_CR = (byte) 13;

    private final static int BUFFER_SIZE = 8192;
    private final static int NEED_READING = -1;

    private RandomAccessFile raf;
    private final String path;
    private final long inode;
    private long pos;
    private long lastUpdated;
    private boolean needTail;
    private static Map<String, String> headers;
    private byte[] buffer;
    private byte[] oldBuffer;
    private int bufferPos;
    private long lineReadPos;
    private static Map<String, Integer> fileState = Maps.newHashMapWithExpectedSize(10);//0初始化1采集中2已完成
//    private static HashMap<String, String> initEvenHashMap = Maps.newHashMapWithExpectedSize(2);

    public TailFile(File file, Map<String, String> headers, long inode, long pos)
            throws IOException {
        this.raf = new RandomAccessFile(file, "r");
        if (pos > 0) {
            raf.seek(pos);
            lineReadPos = pos;
        }
        this.path = file.getAbsolutePath();
        this.inode = inode;
        this.pos = pos;
        this.lastUpdated = 0L;
        this.needTail = true;
        this.headers = headers;
        this.oldBuffer = new byte[0];
        this.bufferPos = NEED_READING;

    }


    public RandomAccessFile getRaf() {
        return raf;
    }

    public String getPath() {
        return path;
    }

    public long getInode() {
        return inode;
    }

    public long getPos() {
        return pos;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public boolean needTail() {
        return needTail;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public long getLineReadPos() {
        return lineReadPos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setNeedTail(boolean needTail) {
        this.needTail = needTail;
    }

    public void setLineReadPos(long lineReadPos) {
        this.lineReadPos = lineReadPos;
    }

    public boolean updatePos(String path, long inode, long pos) throws IOException {
        if (this.inode == inode && this.path.equals(path)) {
            setPos(pos);
            updateFilePos(pos);
            logger.info("Updated position, file: " + path + ", inode: " + inode + ", pos: " + pos);
            return true;
        }
        return false;
    }

    public void updateFilePos(long pos) throws IOException {
        raf.seek(pos);
        lineReadPos = pos;
        bufferPos = NEED_READING;
        oldBuffer = new byte[0];
    }


    private void setInitEvenHashMap() {
        this.headers.put("pos", "init");
        this.headers.put("filePath", this.path);
        this.headers.put("filePathMd5", DigestUtils.md5Hex(this.path));
    }

    public List<Event> readEvents(int numEvents, boolean backoffWithoutNL,
                                  boolean addByteOffset) throws IOException {
        if (fileState.get(this.path) == null) {
            fileState.put(this.path, 0);
        }

        if (!DigestUtils.md5Hex(this.path).equals(this.headers.get("filePathMd5"))) {
            setInitEvenHashMap();
        }
        List<Event> events = Lists.newLinkedList();
        for (int i = 0; i < numEvents; i++) {
            Event event = readEvent1(backoffWithoutNL, addByteOffset);
            if (event == null) {
                if (fileState.get(this.path) == 1) {
                    HashMap<String, String> heartMap = Maps.newHashMapWithExpectedSize(2);
                    heartMap.putAll(this.headers);
                    heartMap.put("pos", "end");
                    Event noBodyEvent = EventBuilder.withBody(null, heartMap);
                    events.add(noBodyEvent);
                    fileState.put(this.path, 2);
                }
                break;
            } else {
                if (fileState.get(this.path) != 1) {
                    fileState.put(this.path, 1);
         /*           if (fileState.get(this.path) == 2) {
                        event = null;
                    } else {
                        fileState.put(this.path, 1);
                    }*/
                }
                events.add(event);
            }
        }
        return events;
    }

    private Event readEvent1(boolean backoffWithoutNL, boolean addByteOffset) throws IOException {
        Long posTmp = getLineReadPos();
        Event event = null;
        if (raf.getFilePointer() < raf.length()) {
            readFile();
            setLineReadPos(lineReadPos + buffer.length);
            this.headers.put("pos", String.valueOf(lineReadPos));
            event = EventBuilder.withBody(buffer, this.headers);
        } else {
            return null;
        }
        return event;
    }

    private Event readEvent(boolean backoffWithoutNL, boolean addByteOffset) throws IOException {
        Long posTmp = getLineReadPos();
        LineResult line = readLine();
        if (line == null) {
            return null;
        }
/*        if (backoffWithoutNL && !line.lineSepInclude) {
            logger.info("Backing off in file without newline: "
                    + path + ", inode: " + inode + ", pos: " + raf.getFilePointer());
            updateFilePos(posTmp);
            return null;
        }*/
        Event event = EventBuilder.withBody(line.line, this.headers);
        if (addByteOffset == true) {
            event.getHeaders().put(BYTE_OFFSET_HEADER_KEY, posTmp.toString());
        }
        return event;
    }

    private void readFile() throws IOException {
        if ((raf.length() - raf.getFilePointer()) < BUFFER_SIZE) {
            buffer = new byte[(int) (raf.length() - raf.getFilePointer())];
        } else {
            buffer = new byte[BUFFER_SIZE];
        }
        raf.read(buffer, 0, buffer.length);
        bufferPos = 0;
    }

    private byte[] concatByteArrays(byte[] a, int startIdxA, int lenA, byte[] b, int startIdxB, int lenB) {
        byte[] c = new byte[lenA + lenB];
        System.arraycopy(a, startIdxA, c, 0, lenA);
        System.arraycopy(b, startIdxB, c, lenA, lenB);
        return c;
    }

    public LineResult readLine() throws IOException {
        LineResult lineResult = null;
        while (true) {
            if (bufferPos == NEED_READING) {
                if (raf.getFilePointer() < raf.length()) {
                    readFile();
                } else {
                    if (oldBuffer.length > 0) {
                        lineResult = new LineResult(false, oldBuffer);
                        oldBuffer = new byte[0];
                        setLineReadPos(lineReadPos + lineResult.line.length);
                    }

                    break;
                }
            }
            for (int i = bufferPos; i < buffer.length; i++) {
                if (buffer[i] == BYTE_NL) {
                    int oldLen = oldBuffer.length;
                    // Don't copy last byte(NEW_LINE)！！！！！
                    int lineLen = i - bufferPos;
                    // For windows, check for CR
/*                    if (i > 0 && buffer[i - 1] == BYTE_CR) {
                        lineLen -= 1;
                    } else if (oldBuffer.length > 0 && oldBuffer[oldBuffer.length - 1] == BYTE_CR) {
                        oldLen -= 1;
                    }*/
                    lineResult = new LineResult(true,
                            concatByteArrays(oldBuffer, 0, oldLen, buffer, bufferPos, lineLen + 1));
                    setLineReadPos(lineReadPos + (oldBuffer.length + (i - bufferPos + 1)));
                    oldBuffer = new byte[0];
                    if (i + 1 < buffer.length) {
                        bufferPos = i + 1;
                    } else {
                        bufferPos = NEED_READING;
                    }
                    break;
                }
            }
            if (lineResult != null) {
                break;
            }
            // NEW_LINE not showed up at the end of the buffer
            oldBuffer = concatByteArrays(oldBuffer, 0, oldBuffer.length, buffer, bufferPos, (buffer.length - bufferPos));
            bufferPos = NEED_READING;
        }
        return lineResult;
    }

    public void close() {
        try {
            raf.close();
            raf = null;
            long now = System.currentTimeMillis();
            setLastUpdated(now);
        } catch (IOException e) {
            logger.error("Failed closing file: " + path + ", inode: " + inode, e);
        }
    }

    public static class CompareByLastModifiedTime implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
        }
    }

    private class LineResult {
        final boolean lineSepInclude;
        final byte[] line;

        public LineResult(boolean lineSepInclude, byte[] line) {
            super();
            this.lineSepInclude = lineSepInclude;
            this.line = line;
        }
    }
}
