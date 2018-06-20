package com.github.app.spi.plugin;

import com.github.app.spi.handler.UriHandler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.validator.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidatorInterceptorImpl implements ValidatorInterceptor, UriHandler {
    private static final Logger logger = LogManager.getLogger(ValidatorInterceptorImpl.class);

    private Validator validator;
    private ValidatorResources validatorResources;

    public ValidatorInterceptorImpl() {
        try {
            validatorResources = new ValidatorResources(ValidatorInterceptorImpl.class.getResourceAsStream("/validator.xml"));
            validator = new Validator(validatorResources, "interceptorForm");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(RoutingContext routingContext) {
        //  http query params
        if (!processSqlEscapeAndValidate(routingContext.request().params(), routingContext)) {
            return;
        }
        //  http body forms
        if (!processSqlEscapeAndValidate(routingContext.request().formAttributes(), routingContext)) {
            return;
        }

        if (routingContext.getBody() != null) {
            routingContext.setBody(Buffer.buffer(ValidatorInterceptor.escape(routingContext.getBodyAsString())));

            try {
                JsonObject jsonObject = mapTo(routingContext, JsonObject.class);
                if (jsonObject != null) {
                    if (!processBodyObjectValidator(jsonObject.getMap(), routingContext)) {
                        return;
                    }
                }
            } catch (Exception e) {
            }

            try {
                JsonArray jsonArray = mapTo(routingContext, JsonArray.class);

                if (jsonArray != null) {
                    for (Object o : jsonArray) {
                        JsonObject obj = (JsonObject) o;
                        if (!processBodyObjectValidator(obj.getMap(), routingContext)) {
                            return;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        routingContext.next();
    }

    /**
     * 自动匹配表单内容，进行格式验证，并做简单的sql注入处理
     *
     * @param multiMap
     * @param routingContext
     * @return
     */
    private boolean processSqlEscapeAndValidate(MultiMap multiMap, RoutingContext routingContext) {
        Iterator<Map.Entry<String, String>> it = multiMap.iterator();
        try {
            if(multiMap != null && multiMap.size() > 0) {
                validator.setParameter(Validator.BEAN_PARAM, multiMap);
                ValidatorResults results = validator.validate();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    entry.setValue(ValidatorInterceptor.escape(entry.getValue()));

                    ValidatorResult validatorResult = results.getValidatorResult(entry.getKey());
                    if (validatorResult != null) {
                        List<String> dependencyList = validatorResult.getField().getDependencyList();
                        for (String dep : dependencyList) {
                            if (!validatorResult.isValid(dep)) {
                                responseOperationFailed(routingContext, validatorResult.getField().getMessage(entry.getKey()).getBundle());
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (ValidatorException e) {
        }

        return true;
    }

    private boolean processBodyObjectValidator(Map<String, Object> map, RoutingContext routingContext) {
        Set<Map.Entry<String, Object>> it = map.entrySet();
        try {
            if(map != null && map.size() > 0) {
                validator.setParameter(Validator.BEAN_PARAM, map);
                ValidatorResults results = validator.validate();
                for (Map.Entry<String, Object> entry : it) {
                    ValidatorResult validatorResult = results.getValidatorResult(entry.getKey());
                    if (validatorResult != null) {
                        List<String> dependencyList = validatorResult.getField().getDependencyList();
                        for (String dep : dependencyList) {
                            if (!validatorResult.isValid(dep)) {
                                responseOperationFailed(routingContext, validatorResult.getField().getMessage(entry.getKey()).getBundle());
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (ValidatorException e) {
        }
        return true;
    }


    @Override
    public void registeUriHandler(Router router) {

    }
}
