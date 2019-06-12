package com.rxiu.zkui.core.curator.pool;

import com.rxiu.zkui.core.curator.factory.Factory;
import com.rxiu.zkui.core.curator.validator.Validator;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shenyuhang
 * @date 2019/5/28
 **/
public class BoundedBlockingPool<T> extends AbstractPool<T> implements BlockingPool<T> {

    private final Validator<T> validator;
    private final Factory<T> factory;
    private final AtomicInteger poolSize;
    private boolean running;

    private final BlockingQueue<T> queue;
    private ExecutorService executor;

    public BoundedBlockingPool(int size, Validator validator) {
        this(size, validator, null);
    }

    public BoundedBlockingPool(int size, Validator validator, Factory factory) {
        this.poolSize = new AtomicInteger(size);
        this.validator = validator;
        this.factory = factory;
        this.running = true;
        this.queue = new LinkedBlockingQueue<>(size);
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public T get() {
        return get(null, null);
    }

    @Override
    public T get(Long time, TimeUnit unit) {
        if (running) {
            T t = null;
            try {
                if (time == null || unit == null) {
                    t = queue.take();
                } else {
                    t = queue.poll(time, unit);
                }
                return t;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }

        throw new IllegalStateException("Bounded Blocking Oool is already shutdown.");
    }

    @Override
    public void put(T t) {
        if (poolSize.get() <= queue.size()) {
            if (validator.validate(t)) {
                executor.submit(new ObjectHandler(t));
            }
        }
    }

    @Override
    protected boolean validate(T t) {
        return validator.validate(t);
    }

    @Override
    protected void doRelease(T t) {
        executor.submit(new ObjectHandler(t));
    }

    @Override
    protected void destroy(T t) {
    }

    @Override
    public void shutdown() {
        this.running = false;
        for (T t : queue) {
            validator.invalidate(t);
        }
    }

    private class ObjectHandler implements Callable {
        T t;

        public ObjectHandler(T t) {
            this.t = t;
        }

        @Override
        public Object call() {
            while (true) {
                try {
                    BoundedBlockingPool.this.queue.put(t);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
