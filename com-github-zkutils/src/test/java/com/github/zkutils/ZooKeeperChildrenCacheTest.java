package com.github.zkutils;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.github.app.utils.JacksonUtils;
import org.apache.bookkeeper.common.util.OrderedExecutor;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
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
            try {
                Set<String> paths = cache.get("/gateway/cluster");
                System.out.println(Arrays.toString(paths.toArray()));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        cache.get("/gateway/cluster");
//        System.out.println(paths.size());

//        LocalZooKeeperConnectionService.createIfAbsent(zooKeeper.get(), "/gateway/cluster/g1", "config", CreateMode.PERSISTENT);
        LocalZooKeeperConnectionService service = new LocalZooKeeperConnectionService(new ZookeeperClientFactoryImpl(), "127.0.0.1:2181", 3000);

        service.start(new ZooKeeperSessionWatcher.ShutdownService() {
            @Override
            public void shutdown(int exitCode) {

            }
        });

        LocalZooKeeperConnectionService.createIfAbsent(service.getLocalZooKeeper(), "/gateway/cluster/g1", "config", CreateMode.EPHEMERAL);
        LocalZooKeeperConnectionService.createIfAbsent(service.getLocalZooKeeper(), "/gateway/cluster/g2", "config", CreateMode.EPHEMERAL);

        Thread.sleep(4*1000);
        LocalZooKeeperConnectionService.createIfAbsent(service.getLocalZooKeeper(), "/gateway/cluster/g3", "config", CreateMode.EPHEMERAL);
        Thread.sleep(3*1000);
        service.close();

    }
}
