package com.github.server.service;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class ZookeeperService {
    private CuratorFramework zkClient;
    private List<PathChildrenCache> cacheList = Lists.newArrayList();

    public ZookeeperService(String connectString) {
        zkClient = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
        zkClient.start();
    }

    public void addChildrenListener(String path, PathChildrenCacheListener listener, boolean cacheData) throws Exception {
        Stat stat = zkClient.checkExists().forPath(path);
        if (stat == null) {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
        }
        PathChildrenCache cache = new PathChildrenCache(zkClient, path, cacheData);
        cache.getListenable().addListener(listener);
        cache.start();
        cacheList.add(cache);
    }

    public void createEphemeralNode(String path, byte[] data) throws Exception {
        zkClient.create().withMode(CreateMode.EPHEMERAL).forPath(path, data);
    }

    public void createPersistentNode(String path, byte[] data) throws Exception {
        zkClient.create().withMode(CreateMode.PERSISTENT).forPath(path, data);
    }

    public void updateData(String path, byte[] data) throws Exception {
        zkClient.setData().forPath(path, data);
    }

    public byte[] getData(String path) throws Exception {
        return zkClient.getData().forPath(path);
    }

    public void close() {
        for (PathChildrenCache cache : cacheList) {
            CloseableUtils.closeQuietly(cache);
        }
        CloseableUtils.closeQuietly(zkClient);
    }
}
