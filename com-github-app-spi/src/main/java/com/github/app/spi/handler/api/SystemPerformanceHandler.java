package com.github.app.spi.handler.api;

import com.github.app.spi.dao.domain.Popedom;
import com.github.app.spi.handler.AuthUriHandler;
import com.github.app.spi.plugin.HandlerPerformanceMonitor;
import com.github.app.spi.utils.RequestUtils;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SystemPerformanceHandler implements AuthUriHandler {

  @Override
  public void registeUriHandler(Router router) {
    router.get().path("/performance").produces(CONTENT_TYPE).blockingHandler(this::query, false);
  }

  @Override
  public void registePopedom(List<Popedom> list) {
    list.add(new Popedom.Builder().name("性能监控").code("/.+/performance/" + HttpMethod.GET.name())
        .remark("管理员的功能，可以监控平台接口使用情况").build());
  }

  public void query(RoutingContext routingContext) {
    MultiMap params = routingContext.queryParams();

    Integer rows = RequestUtils.getInteger(params, "rows");
    Integer offset = RequestUtils.getInteger(params, "offset");
    String sort = params.get("sort");
    Boolean isDesc = RequestUtils.getBoolean(params, "isDesc");

    String rateUnit = params.get("rateUnit");
    String durationUnit = params.get("durationUnit");

    if (StringUtils.isEmpty(rateUnit)) {
      rateUnit = "SECONDS";
    }

    if (StringUtils.isEmpty(durationUnit)) {
      durationUnit = "MILLISECONDS";
    }

    if (ObjectUtils.isEmpty(rows)) {
      rows = Integer.valueOf(20);
    }

    if (ObjectUtils.isEmpty(offset)) {
      offset = Integer.valueOf(0);
    }

    if (StringUtils.isEmpty(sort)) {
      sort = "mean";
    }

    if (ObjectUtils.isEmpty(isDesc)) {
      isDesc = true;
    }

    JsonArray list = HandlerPerformanceMonitor.listMetric(rows, offset, sort, isDesc,
        TimeUnit.valueOf(rateUnit.toUpperCase()), TimeUnit.valueOf(durationUnit.toUpperCase()));

    JsonObject map = new JsonObject();
    map.put("total", HandlerPerformanceMonitor.totalSize());
    map.put("list", list);

    responseSuccess(routingContext, map);
  }
}
