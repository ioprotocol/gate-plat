package com.github.app.utils;

import com.google.common.annotations.VisibleForTesting;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorTest {
    public class LongEvent {
        private long value;

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }

    public class LongEventFactory implements EventFactory<LongEvent> {

        @Override
        public LongEvent newInstance() {
            return new LongEvent();
        }
    }

    public class LongEventHandler implements EventHandler<LongEvent> {

        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("Event: " + event);
            System.out.println("Event: " + event.getValue());
        }
    }

    static class Translator implements EventTranslatorOneArg<LongEvent, Long> {
        @Override
        public void translateTo(LongEvent event, long sequence, Long data) {
            event.setValue(data);
        }
    }

    public static Translator TRANSLATOR = new Translator();

    public void test() {
        EventFactory<LongEvent> eventFactory = new LongEventFactory();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；

        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory,
                ringBufferSize, executor, ProducerType.SINGLE,
                new YieldingWaitStrategy());

        EventHandler<LongEvent> eventHandler = new LongEventHandler();
        disruptor.handleEventsWith(eventHandler);

        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        long data = System.currentTimeMillis();
        ringBuffer.publishEvent(TRANSLATOR, data);
    }

    public static void main(String[] args) {
        new DisruptorTest().test();
    }
}
