package com.beidahuang.loadbalance;

import java.util.List;

/**
 * 负载均衡
 */
public interface LoadBalance {

    public String select(List<String> urls);
}
