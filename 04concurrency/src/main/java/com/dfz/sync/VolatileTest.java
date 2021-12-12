package com.dfz.sync;

import static java.lang.Thread.sleep;

public class VolatileTest {
    private int a = 0;
    
    public void volatileTest() throws InterruptedException {
        new Thread(() -> {
            while (a != 1) {
                // JVM会在cpu空闲时，尽量保证多线程间共享变量的可见性
                // System.out.println底层使用Synchronized同步，获取同步锁资源时cpu有足够的时间来保证可见性
                // System.out.println(a);
                // System.out.println("test");
                try {
                    // 也可通过sleep使cpu空闲保证可见性
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("子线程1退出");
        }).start();
        new Thread(() -> {
            try {
                sleep(10);
                a = 1;
                System.out.println("子线程2执行sleep");
                sleep(3000);
                System.out.println("子线程2退出");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        VolatileTest JMMObject = new VolatileTest();
        try {
            JMMObject.volatileTest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
