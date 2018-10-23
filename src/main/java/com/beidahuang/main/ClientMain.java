package com.beidahuang.main;

import com.beidahuang.discovery.IDiscovery;
import com.beidahuang.discovery.impl.DiscoveryImpl;
import com.beidahuang.handler.RpcClientHandler;
import com.beidahuang.hello.Hello;
import com.beidahuang.rpcrequest.RpcRequest;
import com.beidahuang.user.User;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class ClientMain {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        IDiscovery discovery = new DiscoveryImpl();
        final String discoveryUrl = discovery.discovery("com.beidahuang.hello.Hello");

        //创建访问接口的代理
        User proxy = (User) Proxy.newProxyInstance(User.class.getClassLoader(), new Class[]{User.class}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //封装RpcRequest对象
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setMethodName(method.getName());
                rpcRequest.setClassName(method.getDeclaringClass().getName());
                rpcRequest.setMethodTypeParam(method.getParameterTypes());
                rpcRequest.setParams(args);
                String[] split = discoveryUrl.split(":");
                String ip = split[0];
                String port = split[1];
                final RpcClientHandler rpcClientHandler = new RpcClientHandler();
                //与服务端通信
                EventLoopGroup group = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ChannelPipeline channelPipeline = ch.pipeline();
                        channelPipeline.addLast("encoder",new ObjectEncoder());
                        channelPipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                        channelPipeline.addLast(rpcClientHandler);
                    }
                });

                ChannelFuture f = bootstrap.connect(ip,Integer.parseInt(port)).sync();
                f.channel().writeAndFlush(rpcRequest);//写数据
                f.channel().closeFuture().sync();
                return rpcClientHandler.getResponse();
            }
        });

       List<String> resultList =  proxy.findUserNameById(1L);
        System.out.println(resultList.size());

    }
}
