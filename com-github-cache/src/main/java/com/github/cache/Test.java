package com.github.cache;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//        jedisClusterNodes.add(new HostAndPort("gatedep.com", 30001));
//        jedisClusterNodes.add(new HostAndPort("gatedep.com", 30002));
        jedisClusterNodes.add(new HostAndPort("gatedep.com", 30003));
//        jedisClusterNodes.add(new HostAndPort("gatedep.com", 30004));
//        jedisClusterNodes.add(new HostAndPort("gatedep.com", 30005));
//        jedisClusterNodes.add(new HostAndPort("gatedep.com", 30006));
        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(500);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);
        JedisCluster jedis = new JedisCluster(jedisClusterNodes, jedisPoolConfig);
//        Jedis jedis = new Jedis("192.168.56.30", 30003);
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        System.out.println(value);
    }
}
