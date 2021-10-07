package com.dfz.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

public class ProxyBizFilter implements HttpRequestFilter{

    public static ProxyBizFilter newInstance() {
        return new ProxyBizFilter();
    }

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        String uri = fullRequest.uri();
        System.out.println("ProxyBizFilter接收到的请求,url: " + uri);
        if (!uri.startsWith("/nio/hello")) {
            throw new RuntimeException("不支持的uri:" + uri);
        }
        HttpHeaders headers = fullRequest.headers();
        if (headers == null) {
            headers = new DefaultHttpHeaders();
        }
        headers.add("proxy-tag", this.getClass().getSimpleName());
    }
}
