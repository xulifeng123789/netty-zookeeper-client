package com.beidahuang.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalance  implements LoadBalance{

    public String select(List<String> urls) {
        if(urls.size() == 0 ) {
            return null;
        }
        if(urls.size() == 1) {
            return urls.get(0);
        }
        return doSelect(urls);
    }

    public  abstract  String doSelect(List<String> urls);
}
