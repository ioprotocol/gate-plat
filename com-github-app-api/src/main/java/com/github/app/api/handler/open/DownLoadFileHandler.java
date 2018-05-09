package com.github.app.api.handler.open;

import com.github.app.api.handler.UriHandler;
import com.github.app.api.utils.RequestUtils;
import com.github.app.api.utils.SessionConstant;
import com.github.app.utils.MD5Utils;
import com.github.app.utils.ServerEnvConstant;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import org.springframework.util.StringUtils;


@Component
public class DownLoadFileHandler implements UriHandler {

    @Override
    public void registeUriHandler(Router router) {
        router.get().path("/download").blockingHandler(this::downloadFile, false);
        router.get().path("/avatar").blockingHandler(this::getAvatar, false);
    }

    /**
     * @param routingContext
     */
    public void downloadFile(RoutingContext routingContext) {
        if (!ObjectUtils.isEmpty(routingContext.session().get(SessionConstant.SESSION_ACCOUNT))) {
            responseOperationFailed(routingContext, "not login, can't download this file");
            return;
        }

        List<String> list = routingContext.queryParam("fileName");
        if(list == null || list.size() < 1) {
            responseOperationFailed(routingContext, "paremeter missing");
            return;
        }
        List<String> list1 = routingContext.queryParam("code");
        if(list1 == null || list1.size() < 1) {
            responseOperationFailed(routingContext, "operation not permited");
            return;
        }

        if (!MD5Utils.validateMd5WithSalt(list.get(0), list1.get(0))) {
            responseOperationFailed(routingContext, "sign is error");
            return;
        }

        String sqlFileName = ServerEnvConstant.getAppServerHome()
                + File.separator + "data"
                + File.separator + "backup"
                + File.separator + list.get(0);

        routingContext.response().headers().add("content-disposition", "attachment;filename=" + list.get(0));
        routingContext.response().sendFile(sqlFileName);
    }

    public void getAvatar(RoutingContext routingContext) {

        MultiMap params = routingContext.queryParams();

        routingContext.response().putHeader("Pragma", "No-cache");
        routingContext.response().putHeader("Cache-Control", "no-cache");
        routingContext.response().putHeader("Expires", "0");
        routingContext.response().putHeader("content-type", "image/jpeg");

        try {
            String imgName = params.get("imgName");
            if (StringUtils.isEmpty(imgName) || imgName.equalsIgnoreCase("undefined")) {
                imgName = File.separator + "default_header.jpg";
            }
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(ServerEnvConstant.getAppServerHome() + File.separator + "web" + imgName));
            Image bi = ImageIO.read(in);
            Integer height = RequestUtils.getInteger(params, "height");
            Integer width = RequestUtils.getInteger(params, "width");
            if (ObjectUtils.isEmpty(height)) {
                height = bi.getHeight(null);
            }
            if (ObjectUtils.isEmpty(width)) {
                width = bi.getWidth(null);
            }
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(bi, 0, 0, width, height, null);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(tag, "jpeg", os);

            Buffer buffer = Buffer.buffer();
            buffer.appendBytes(os.toByteArray());
            in.close();
            os.close();

            routingContext.response().end(buffer);
        } catch (Exception e) {
            responseOperationFailed(routingContext, e);
        }
    }
}
