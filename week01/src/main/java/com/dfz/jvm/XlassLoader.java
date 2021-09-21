package com.dfz.jvm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class XlassLoader extends ClassLoader{
    public static void main(String[] args) {
        ClassLoader classLoader = new XlassLoader();
        try {
            // 加载Hello.xlass
            Class<?> clazz = classLoader.loadClass("Hello");
            // 实例化对象
            Object obj = clazz.getDeclaredConstructor().newInstance();
            // 获取hello方法
            Method method = clazz.getMethod("hello");
            // 调用方法
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 支持包名
        String resourcePath = name.replace(".", "/");
        // 获取输入流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath + ".xlass");
        try {
            // 读取数据
            int length = inputStream.available();
            byte[] byteArray = new byte[length];
            inputStream.read(byteArray);
            byte[] classBytes = decode(byteArray);
            // 使用字节数组定义一个class
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(name, e);
        } finally {
            // 关闭流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 对应xlass文件进行解码
     * @param byteArray
     * @return
     */
    private byte[] decode(byte[] byteArray) {
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) (255 - byteArray[i]);
        }
        return byteArray;
    }
}
