package com.beidahuang.rpcrequest;

import java.io.Serializable;

public class RpcRequest implements Serializable {

    private String className;//方法对应的类名
    private String methodName;//方法名称
    private Class<?>[] methodTypeParam;//方法参数类型数组
    private Object[] params;//方法的参数值


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getMethodTypeParam() {
        return methodTypeParam;
    }

    public void setMethodTypeParam(Class<?>[] methodTypeParam) {
        this.methodTypeParam = methodTypeParam;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
