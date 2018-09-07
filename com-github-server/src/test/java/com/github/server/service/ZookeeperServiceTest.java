package com.github.server.service;

import com.github.app.utils.JacksonUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

public class ZookeeperServiceTest {
	public static void main(String[] args) throws Exception {
		ZookeeperService service = new ZookeeperService("zk1.com:2181,zk2.com:2181,zk3.com:2181");
		service.addChildrenListener("/gateway/config", new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				System.out.println(JacksonUtils.serialize(client.getChildren().forPath("/gateway/config")));
				System.out.println(event.getType().toString());
			}
		}, true);

		service.createEphemeralNode("/gateway/config/node1", new byte[]{1, 2, 3});

		Thread.sleep(15*1000);
		service.close();
	}
}
