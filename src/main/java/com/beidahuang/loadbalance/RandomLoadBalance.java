package com.beidahuang.loadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {

    public String doSelect(List<String> urls) {
        int len = urls.size();
        Random random = new Random();
        return urls.get(random.nextInt(len));
    }
}
