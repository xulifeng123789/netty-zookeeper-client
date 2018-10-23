package com.beidahuang.discovery;

import org.apache.zookeeper.KeeperException;

import java.util.List;

public interface IDiscovery {

    /**
     * 根据服务名称找出注册的url地址集合
     * @param serviceName 服务的名称
     * @return
     */
    public String discovery(String serviceName) throws KeeperException, InterruptedException;
}
