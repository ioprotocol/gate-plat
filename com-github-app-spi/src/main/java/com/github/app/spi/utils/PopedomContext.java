package com.github.app.spi.utils;

import com.github.app.spi.dao.domain.Popedom;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PopedomContext {
    private List<Popedom> popedoms = new ArrayList<>();

    private static PopedomContext context;

    public static PopedomContext getInstance() {
        if(context == null)
            context = new PopedomContext();
        return context;
    }

    private PopedomContext() {
    }

    public void addPopedoms(List<Popedom> popedoms) {
        this.popedoms.addAll(popedoms);
    }

    public void addPopedom(Popedom popedom) {
        this.popedoms.add(popedom);
    }

    private Popedom match(String uri) {
        for (Popedom popedom : popedoms) {
            if(Pattern.matches(popedom.getCode(), uri)) {
                return popedom;
            }
        }
        return null;
    }

    public Popedom match(RoutingContext routingContext) {
        return match(routingContext.normalisedPath() + "/" + routingContext.request().method().name());
    }

    public List<Popedom> getPopedoms() {
        return popedoms;
    }
}
