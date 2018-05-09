package com.github.app.api;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import io.vertx.ext.dropwizard.reporters.JmxReporter;


public class TimeTest {
    static MetricRegistry metrics = new MetricRegistry();

    public static void main(String[] args) {
        Timer responses = metrics.timer(MetricRegistry.name(TimeTest.class, "responses"));

        final JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
        reporter.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final Timer.Context context = responses.time();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    context.stop();
                }
            }
        }).start();
    }
    public void test() {

    }
}
