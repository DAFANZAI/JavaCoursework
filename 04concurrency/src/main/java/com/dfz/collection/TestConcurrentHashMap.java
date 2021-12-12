package com.dfz.collection;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 探究问题：ConcurrentHashMap为什么不能put null？
 * 结论：当你通过get(k)获取对应的value时，如果获取到的是null时，
 *      你无法判断，它是put(k,v)的时候value为null，还是这个key从来没有做过映射。
 *      HashMap是非并发的，可以通过contains(key)来做这个判断。
 *      而支持并发的Map在调用m.contains(key)和m.get(key)，m可能已经不同了。
 *      对于key也强制不为null倾向于开发规范问题，和value保持一致。
 */
public class TestConcurrentHashMap {
    public static void main(String[] args) {
        // HashMap允许key-value为null
        // key为null默认存在哈希数组index=0的位置
        HashMap<String, String> map = new HashMap<>();
        map.put(null, null);
        System.out.println(map.get(null));
        // HashMap获取一个存在的key也返回null
        // 但是不考虑并发情况可通过contains(key)来判断key是否存在
        System.out.println(map.get("test"));
        // concurrentHashMap不允许key或者value为null
        // 在put null时会报空指针异常
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        //concurrentHashMap.put(null, "test");
        concurrentHashMap.put("test", null);
    }
}
