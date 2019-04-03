package com.rxiu.flume;

import com.google.common.collect.Lists;
import com.rxiu.flume.sink.taildir.SafeRollingFileSink;
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
 * Unit test for simple App.
 */
public class FTPTest {
    Context defaultContext = new Context();

    @Before
    public void init() throws Exception {
        Map<String, String> prop = new HashMap<>();
        prop.put("channel.capacity", "1000");
        prop.put("channel.transactionCapacity", "1000");

        prop.put("source.client.source", "ftp");
        prop.put("source.name.server", "10.0.5.221");
        prop.put("source.port", "21");
        prop.put("source.user", "shenyuhang");
        prop.put("source.password", "666666");
        prop.put("source.working.directory", "/ftp/source");
        //prop.put("source.filter.pattern", ".+\\.doc");
        // prop.put("source.folder", "/ftp");
        prop.put("source.flushlines", "false");

        prop.put("sink.sink.directory", "j:/ftp/target/rolling");
        prop.put("sink.sink.moveFile", "false");
        prop.put("sink.sink.targetDirectory", "j:/ftp/target/PDFfiles");
        prop.put("sink.sink.useCopy", "true");
        prop.put("sink.sink.copyDirectory", "j:/ftp/target/copy");
        prop.put("sink.sink.useFileSuffix", "false");
        prop.put("sink.sink.fileSuffix", ".log");
        defaultContext.putAll(prop);
    }

    public MemoryChannel getChannel() {
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
        SafeRollingFileSink sink = new SafeRollingFileSink();
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
        MemoryChannel channel = getChannel();
        Source source = getSource(channel);
        Sink sink = getSink(channel);

        PollableSourceRunner sourceRunner = new PollableSourceRunner();
        sourceRunner.setSource(source);
        channel.start();
        sourceRunner.start();

        SinkProcessor sinkProcessor = new DefaultSinkProcessor();
        sinkProcessor.setSinks(Arrays.asList(sink));
        SinkRunner sinkRunner = new SinkRunner(sinkProcessor);

        channel.start();
        sourceRunner.start();
        sinkRunner.start();

        while (!Thread.interrupted()) {
            Thread.sleep(200);
        }
    }

}