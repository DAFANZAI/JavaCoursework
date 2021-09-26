package com.dfz.coursework02;

import okhttp3.*;

import java.io.IOException;

public class OkHttpUtils {
    // 客户端实例
    public static OkHttpClient client = new OkHttpClient();

    // GET 调用
    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8801";
        String text = OkHttpUtils.get(url);
        System.out.println("访问HttpServer01 \nurl: " + url + "\nresponse: " + text);

        url = "http://localhost:8802";
        text = OkHttpUtils.get(url);
        System.out.println("访问HttpServer02 \nurl: " + url + "\nresponse: " + text);

        url = "http://localhost:8803";
        text = OkHttpUtils.get(url);
        System.out.println("访问HttpServer03 \nurl: " + url + "\nresponse: " + text);

        // 清理资源
        OkHttpUtils.client = null;
    }
}
