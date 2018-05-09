package com.github.app.api.plugin;

import com.codahale.metrics.Metered;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.github.app.api.dao.domain.Popedom;
import com.github.app.api.utils.PopedomContext;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.reporters.JmxReporter;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HandlerPerformanceMonitor {

    private static MetricRegistry metrics;

    static {
        metrics = new MetricRegistry();
//    JmxReporter.forRegistry(metrics).build().start();
    }

    public static void update(RoutingContext routingContext, long duration) {
        Popedom popedom = PopedomContext.getInstance().match(routingContext);
        if (popedom != null) {
            metrics.timer(popedom.getCode()).update(duration, TimeUnit.NANOSECONDS);
        }
    }

    public static JsonArray listMetric(Integer rows, Integer offset, String sort,
                                       Boolean isDesc, TimeUnit rateUnit, TimeUnit durationUnit) {

        List<JsonObject> list = new ArrayList<>();
        for (Popedom popedom : PopedomContext.getInstance().getPopedoms()) {
            JsonObject jsonObject = JsonObject.mapFrom(popedom);
            jsonObject.put("count", metrics.timer(popedom.getCode()).getCount());
            fillToJson(jsonObject, metrics.timer(popedom.getCode()), rateUnit, durationUnit);
            list.add(jsonObject);
        }

        if (isDesc) {
            list.sort((o1, o2) -> Double.compare(o2.getDouble(sort), o1.getDouble(sort)));
        } else {
            list.sort(Comparator.comparingDouble(o -> o.getDouble(sort)));
        }

        JsonArray jsonArray = new JsonArray();

        for (int i = offset; i < offset + rows && i < list.size(); i++) {
            jsonArray.add(list.get(i));
        }

        return jsonArray;
    }

    public static int totalSize() {
        return metrics.getTimers().size();
    }

    private static JsonObject fillToJson(JsonObject json, Timer timer, TimeUnit rateUnit,
                                         TimeUnit durationUnit) {
        Snapshot snapshot = timer.getSnapshot();

        // Meter
        populateMetered(json, timer, rateUnit);

        // Snapshot
        double factor = 1.0 / durationUnit.toNanos(1);
        populateSnapshot(json, snapshot, factor);

        // Duration rate
//    json.put("durationUnit", durationUnit.toString().toLowerCase());
//    json.put("rateUnit", "events/" + rateUnit.toString().toLowerCase());

        return json;
    }

    private static void populateMetered(JsonObject json, Metered meter, TimeUnit rateUnit) {
        double factor = rateUnit.toSeconds(1);
        json.put("count", meter.getCount());
        json.put("meanRate", meter.getMeanRate() * factor);
        json.put("oneMinuteRate", meter.getOneMinuteRate() * factor);
        json.put("fiveMinuteRate", meter.getFiveMinuteRate() * factor);
        json.put("fifteenMinuteRate", meter.getFifteenMinuteRate() * factor);
    }

    private static void populateSnapshot(JsonObject json, Snapshot snapshot, double factor) {
        json.put("min", snapshot.getMin() * factor);
        json.put("max", snapshot.getMax() * factor);
        json.put("mean", snapshot.getMean() * factor);
        json.put("stddev", snapshot.getStdDev() * factor);
        json.put("median", snapshot.getMedian() * factor);
        json.put("p75", snapshot.get75thPercentile() * factor);
        json.put("p95", snapshot.get95thPercentile() * factor);
        json.put("p98", snapshot.get98thPercentile() * factor);
        json.put("p99", snapshot.get99thPercentile() * factor);
        json.put("p999", snapshot.get999thPercentile() * factor);
    }
}
