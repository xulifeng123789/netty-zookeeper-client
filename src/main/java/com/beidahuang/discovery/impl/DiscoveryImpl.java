package com.beidahuang.discovery.impl;

import com.beidahuang.discovery.IDiscovery;
import com.beidahuang.loadbalance.LoadBalance;
import com.beidahuang.loadbalance.RandomLoadBalance;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

public class DiscoveryImpl implements IDiscovery {

    private ZooKeeper zooKeeper;
    private static final String CONNECT_STR = "localhost:2181";

        //构造函数中连接zookeeper
     public DiscoveryImpl() throws IOException {

            zooKeeper = new ZooKeeper(CONNECT_STR, 5000, new Watcher() {
                public void process(WatchedEvent event) {

                }
            });
        }
    public String discovery(String serviceName) throws KeeperException, InterruptedException {

        String servicePath = "/register/" + serviceName;

        List<String> children = zooKeeper.getChildren(servicePath, true);

        //负载均衡选择一个服务器进行请求
        LoadBalance loadBalance = new RandomLoadBalance();
        String selectUrl = loadBalance.select(children);

        return selectUrl;
    }
}
