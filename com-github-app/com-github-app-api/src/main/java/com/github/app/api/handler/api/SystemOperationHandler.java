package com.github.app.api.handler.api;

import com.github.app.api.dao.domain.Popedom;
import com.github.app.api.handler.UriHandler;
import com.github.app.api.services.SystemOperationService;
import com.github.app.api.utils.ConfigLoader;
import com.github.app.api.utils.RequestUtils;
import com.github.app.utils.ServerEnvConstant;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class SystemOperationHandler implements UriHandler {

	@Autowired
	private SystemOperationService operationService;

	@Override
	public void registeUriHandler(Router router) {
		router.get().path("/sysbackup").produces(CONTENT_TYPE).blockingHandler(this::list, false);
		router.post().path("/sysbackup").produces(CONTENT_TYPE).blockingHandler(this::uploadFile, false);
		router.put().path("/sysbackup").produces(CONTENT_TYPE).blockingHandler(this::backup, false);
		router.delete().path("/sysbackup").produces(CONTENT_TYPE).blockingHandler(this::del, false);
		router.put().path("/sysrestore").produces(CONTENT_TYPE).blockingHandler(this::restore, false);
	}

	@Override
	public void registePopedom(List<Popedom> list) {
		list.add(new Popedom.Builder().name("*查询备份").code("/[a-zA-Z]+/sysbackup/" + HttpMethod.GET.name()).build());
		list.add(new Popedom.Builder().name("*上传备份").code("/[a-zA-Z]+/sysbackup/" + HttpMethod.POST.name()).build());
		list.add(new Popedom.Builder().name("*系统备份").code("/[a-zA-Z]+/sysbackup/" + HttpMethod.PUT.name()).build());
		list.add(new Popedom.Builder().name("*删除备份").code("/[a-zA-Z]+/sysbackup/" + HttpMethod.DELETE.name()).build());
		list.add(new Popedom.Builder().name("*系统恢复").code("/[a-zA-Z]+/sysrestore/" + HttpMethod.PUT.name()).build());
	}

	public void uploadFile(RoutingContext routingContext) {
		String backupHome = System.getenv(ServerEnvConstant.APP_HOME) + File.separator + "data" + File.separator + "backup";

		Set<FileUpload> set = routingContext.fileUploads();
		set.forEach(fileUpload -> {
			File file = new File(fileUpload.uploadedFileName());
			try {
				String name = fileUpload.fileName();
				while (name.indexOf(File.separator) > 0) {
					name = name.substring(name.indexOf(File.separator));
				}
				while (name.indexOf("/") > 0) {
					name = name.substring(name.indexOf("/"));
				}
				FileUtils.moveFile(file, new File(backupHome + File.separator + name));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		responseSuccess(routingContext);
	}

	public void list(RoutingContext routingContext) {
		MultiMap params = routingContext.queryParams();
		JsonObject jsonObject = operationService.list(RequestUtils.getInteger(params, "offset"), RequestUtils.getInteger(params, "rows"));
		responseSuccess(routingContext, jsonObject);
	}

	public void backup(RoutingContext routingContext) {
		JsonObject config = ConfigLoader.getServerCfg();
		operationService.backup(config);
		responseSuccess(routingContext);
	}

	public void del(RoutingContext routingContext) {
		List<String> list = routingContext.queryParam("fileName");
		if (list == null || list.size() < 1) {
			responseFailure(routingContext, "必须选择一个文件");
			return;
		}

		operationService.deleteSqlFile(list.get(0));
		responseSuccess(routingContext);
	}

	public void restore(RoutingContext routingContext) {
		List<String> list = routingContext.queryParam("fileName");
		if (list == null || list.size() < 1) {
			responseFailure(routingContext, "必须选择一个文件");
			return;
		}

		JsonObject config = ConfigLoader.getServerCfg();

		operationService.restore(config, list.get(0));
		responseSuccess(routingContext);
	}

	public void download(RoutingContext routingContext) {
		List<String> list = routingContext.queryParam("fileName");
		if (list == null || list.size() < 1) {
			responseFailure(routingContext, "必须选择一个文件");
			return;
		}

		String sqlFileName = System.getenv(ServerEnvConstant.APP_HOME) + File.separator + "data" + File.separator + "backup" + File.separator + list.get(0);

		routingContext.response().headers().add("content-disposition", "attachment;filename=" + list.get(0));
		routingContext.response().sendFile(sqlFileName);
	}
}
