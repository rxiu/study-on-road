package com.rxiu.flume;

import com.google.common.collect.Lists;
import com.rxiu.flume.sink.hdfs.HDFSEventSink;
import com.rxiu.flume.source.ftp.source.Source;
import org.apache.flume.*;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.channel.MemoryChannel;
import org.apache.flume.channel.ReplicatingChannelSelector;
import org.apache.flume.conf.Configurables;
import org.apache.flume.sink.DefaultSinkProcessor;
import org.apache.flume.source.PollableSourceRunner;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class HDFSTest {

    Context defaultContext = new Context();

    @Before
    public void init() throws Exception {
        Map<String, String> prop = new HashMap<>();
        prop.put("channel.capacity", "100000");
        prop.put("channel.transactionCapacity", "100000");

        prop.put("source.client.source", "ftp");
        prop.put("source.name.server", "10.0.1.55");
        prop.put("source.port", "21");
        prop.put("source.user", "shenyuhang");
        prop.put("source.password", "666666");
        prop.put("source.working.directory", "/ftp/source");
        prop.put("source.filter.pattern", ".+\\.pdf");
        prop.put("source.flushlines", "false");

        //setAloneHdfs(prop);
        setHaHdfs(prop);
    }

    private void setAloneHdfs(Map<String, String> prop) {
        prop.put("sink.hdfs.path", "hdfs://zoe-001:8020/flume-wrapper/pdf-201902131960");
        //prop.put("sink.hdfs.fileType", "DataStream");
        prop.put("sink.hdfs.writeFormat", "Text");
        prop.put("sink.hdfs.useLocalTimeStamp", "true");
        prop.put("sink.hdfs.batchSize", "3000");
        prop.put("sink.hdfs.rollSize", "0");
        prop.put("sink.hdfs.rollInterval", "60");
        prop.put("sink.hdfs.rollCount", "0");
        defaultContext.putAll(prop);
    }

    private void setHaHdfs(Map<String, String> prop) {
        prop.put("sink.hdfs.HA", "true");
        prop.put("sink.hdfs.nameNodeServer", "zoeBigData");
        prop.put("sink.hdfs.server.nn1", "master:9000");
        prop.put("sink.hdfs.server.nn2", "slave1:9000");
        prop.put("sink.hdfs.path", "hdfs://zoeBigData/flume-wrapper/pdf-201902131800");
        prop.put("sink.hdfs.writeFormat", "Text");
        prop.put("sink.hdfs.useLocalTimeStamp", "true");
        prop.put("sink.hdfs.batchSize", "3000");
        prop.put("sink.hdfs.rollSize", "0");
        prop.put("sink.hdfs.rollInterval", "60");
        prop.put("sink.hdfs.rollCount", "0");
        defaultContext.putAll(prop);
    }

    public Channel getChannel() {
        MemoryChannel channel = new MemoryChannel();
        channel.setName("channel");

        configure(channel, "channel.");
        return channel;
    }

    public Source getSource(Channel channel) {
        Source source = new Source();
        source.setName("source");

        ChannelSelector selector = new ReplicatingChannelSelector();
        selector.setChannels(Lists.newArrayList(channel));

        ChannelProcessor processor = new ChannelProcessor(selector);
        source.setChannelProcessor(processor);

        configure(source, "source.");
        return source;
    }

    public Sink getSink(Channel channel) {

        HDFSEventSink sink = new HDFSEventSink();
        sink.setName("sink");
        sink.setChannel(channel);
        configure(sink, "sink.");
        return sink;
    }

    public void configure(Object target, String prefixProperty) {
        Context context = new Context();
        context.putAll(defaultContext.getSubProperties(prefixProperty));
        Configurables.configure(target, context);
    }

    @Test
    public void contextLoads() throws Exception {
        Channel channel = getChannel();
        Source source = getSource(channel);
        Sink sink = getSink(channel);

        PollableSourceRunner sourceRunner = new PollableSourceRunner();
        sourceRunner.setSource(source);
        channel.start();
        sourceRunner.start();

        SinkProcessor sinkProcessor = new DefaultSinkProcessor();
        sinkProcessor.setSinks(Arrays.<Sink>asList(sink));
        SinkRunner sinkRunner = new SinkRunner(sinkProcessor);

        channel.start();
        sourceRunner.start();
        sinkRunner.start();

        while (!Thread.interrupted()) {
            Thread.sleep(200);
        }
    }

}