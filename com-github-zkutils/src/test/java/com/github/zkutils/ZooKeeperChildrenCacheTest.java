package com.github.zkutils;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.bookkeeper.common.util.OrderedExecutor;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperChildrenCacheTest {

    public static void main(String[] args) throws Exception {
        ZooKeeperChildrenCache cache;
        CompletableFuture<ZooKeeper> zooKeeper;

        zooKeeper = new ZookeeperClientFactoryImpl().create("127.0.0.1:2181", ZooKeeperClientFactory.SessionType.ReadWrite, 3000);

        LocalZooKeeperConnectionService.checkAndCreatePersistNode(zooKeeper.get(), "/gateway");
        LocalZooKeeperConnectionService.checkAndCreatePersistNode(zooKeeper.get(), "/gateway/cluster");

        cache = new ZooKeeperChildrenCache(new LocalZooKeeperCache(zooKeeper.get(), OrderedExecutor.newBuilder().build()), "/gateway/cluster");
        cache.registerListener((path, data, stat) -> {
            System.out.println("child:" + path);
        });

        Set<String> paths = cache.get("/");
        System.out.println(paths.size());

        LocalZooKeeperConnectionService.createIfAbsent(zooKeeper.get(), "/gateway/cluster/g1", "config", CreateMode.EPHEMERAL);

        Thread.sleep(10*1000);
    }
}
