package com.rxiu.flume.file.pipeline;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.channel.BasicChannelSemantics;
import org.apache.flume.channel.BasicTransactionSemantics;
import org.apache.flume.channel.file.FileChannelConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rxiu
 * @date 2018/08/27.
 **/
public class MultiPipelineChannel extends BasicChannelSemantics {
    List<PipelineChannel> pipelineChannels;

    private Context context;

    private Integer channels;

    private String checkpointDir;

    private String dataDir;

    private AtomicInteger balance;

    @Override
    public void configure(Context context) {
        this.context = context;

        this.channels = context.getInteger("channels", 0);
        Preconditions.checkArgument(channels > 0, "channels must be larger then zore.");

        checkpointDir = context.getString(FileChannelConfiguration.CHECKPOINT_DIR);
        Preconditions.checkState(StringUtils.isNotEmpty(checkpointDir),
                "checkpointDir must not be empty");

        dataDir = context.getString(FileChannelConfiguration.DATA_DIRS);
        Preconditions.checkState(StringUtils.isNotEmpty(dataDir), "dataDir must not be empty");

        mkDir(checkpointDir);
        mkDir(dataDir);

        pipelineChannels = Lists.newArrayListWithExpectedSize(channels);
        for (int i = 0; i < channels; i++) {
            PipelineChannel channel = new PipelineChannel();
            channel.setName(String.format("file channel%s", i));
            Context ctx = new Context(this.context.getParameters());
            ctx.put(FileChannelConfiguration.CHECKPOINT_DIR, new File(checkpointDir, String.valueOf(i)).getAbsolutePath());
            ctx.put(FileChannelConfiguration.DATA_DIRS, new File(dataDir, String.valueOf(i)).getAbsolutePath());
            channel.configure(ctx);
            pipelineChannels.add(channel);
        }
        balance.set(0);
        super.configure(context);
    }

    @Override
    public synchronized void start() {
        pipelineChannels.forEach(PipelineChannel::start);
        super.start();
    }

    @Override
    public synchronized void stop() {
        pipelineChannels.forEach(PipelineChannel::stop);
        pipelineChannels.clear();
        pipelineChannels = null;

        balance = null;
        super.stop();
    }

    @Override
    protected BasicTransactionSemantics createTransaction() {
        if (balance.getAndAdd(1) > Integer.MAX_VALUE - BigDecimal.ONE.intValue()) {
            balance.getAndSet(0);
        }
        return (BasicTransactionSemantics) pipelineChannels.get(balance.get() % channels).getTransaction();
    }

    private File mkDir(String... paths) {
        Preconditions.checkArgument(paths.length > 0, "flume.paths must be specify.");

        StringBuilder builder = new StringBuilder();
        for (String path : paths) {
            builder.append(path).append(File.separator);
        }

        File file = new File(builder.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
