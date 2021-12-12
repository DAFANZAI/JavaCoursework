package com.dfz.homework;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程
 */
public class ThreadSynchronization {
    /**
     * 方法1 使用sleep等待线程执行完拿到结果
     */
    @Test
    public void method1() {
        // 创建一个原子变量保证子线程修改后主线程立刻看到
        final AtomicInteger result = new AtomicInteger();
        // 创建一个线程执行方法
        new Thread(() -> {
            result.set(sum());
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("异步计算结果为：" + result);
    }

    /**
     * 方法2 使用join等待线程执行完成拿到结果
     */
    @Test
    public void method2() {
        // 创建一个原子变量保证子线程修改后主线程立刻看到
        final AtomicInteger result = new AtomicInteger();
        // 创建一个线程执行方法
        Thread thread = new Thread(() -> {
            result.set(sum());
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("异步计算结果为：" + result);
    }

    /**
     * 方法3 使用自旋机制拿到结果
     */
    @Test
    public void method3() {
        // 创建一个原子变量保证子线程修改后主线程立刻看到，初始值为-1
        final AtomicInteger result = new AtomicInteger(-1);
        // 创建一个线程执行方法
        new Thread(() -> {
            result.set(sum());
        }).start();
        while (result.get() < 0) {
            // 自旋等待结果
        }
        System.out.println("异步计算结果为：" + result);
    }

    /**
     * 方法4 使用Future拿到结果
     */
    @Test
    public void method4() {
        // 创建一个线程池
        ExecutorService service = Executors.newSingleThreadExecutor();
        // 执callable任务
        Future<Integer> submit = service.submit(() -> sum());
        Integer result = 0;
        try {
            // 获取任务执行结果
            result = submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // 关闭线程池
        service.shutdown();
        System.out.println("异步计算结果为：" + result);
    }

    /**
     * 方法5 使用FutureTask拿到执行结果
     */
    @Test
    public void method5() {
        // 定义一个FutureTask
        FutureTask futureTask = new FutureTask(() -> sum());
        // 创建一个线程来执行FutureTask
        new Thread(futureTask).start();
        int result = 0;
        try {
            // 获取任务执行结果
            result = (int) futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("异步计算结果为：" + result);
    }

    /**
     * 方法5 使用阻塞队列拿到结果
     */
    @Test
    public void method6() {
        // 创建一个阻塞队列
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
        // 创建一个线程执行任务并把任务执行结果方法队列中
        new Thread(() -> queue.offer(sum())).start();
        int result = 0;
        try {
            // 从阻塞队列中获取结果
            result = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("异步计算结果为：" + result);
    }
    
    private static int sum() {
        return fibonacci(36);
    }

    /**
     * 方法7 使用wait notify/notifyAll机制拿到结果
     */
    @Test
    public void method7() {
        // 定义一个对象锁
        final Object obj = new Object();
        // 创建一个原子变量
        AtomicInteger result = new AtomicInteger();
        // 主线程获取对象锁
        synchronized (obj) {
            try {
                // 触发一个子线程
                new Thread(() -> {
                    result.set(sum1(obj));
                }).start();
                // 调用wait方法并释放锁
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("异步计算结果为：" + result.get());
    }
    
    private int sum1(Object obj) {
        int result = 0;
        synchronized (obj) {
            result = fibonacci(36);
            obj.notify();
        }
        return result;
    }

    /**
     * 方法8 使用await signal/signalAll机制拿到结果
     */
    @Test
    public void method8() {
        // 创建一个lock锁
        final Lock lock = new ReentrantLock();
        // 创建一个Condition
        final Condition condition = lock.newCondition();
        // 创建一个原子变量存放计算结果
        AtomicInteger result = new AtomicInteger();
        // 加锁
        lock.lock();
        // 创建一个子线程执行计算任务
        new Thread(() -> {
            result.set(sum2(lock, condition));
        }).start();
        try {
            // 调用await方法
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 释放锁
        lock.unlock();
        System.out.println("异步计算结果为：" + result.get());
    }
    
    private int sum2(Lock lock, Condition condition) {
        // 加锁
        lock.lock();
        // 执行计算任务
        int result = fibonacci(36);
        // 唤醒主线程
        condition.signal();
        // 解锁
        lock.unlock();
        return result;
    }

    /**
     * 方法9 使用CountDownLatch机制拿到结果
     */
    @Test
    public void method9() {
        // 创建一个CountDownLatch对象
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        // 创建一个原子变量存放异步计算结果
        AtomicInteger result = new AtomicInteger();
        // 创建一个线程异步执行计算任务
        new Thread(() -> {
           result.set(sum3(countDownLatch)); 
        }).start();
        try {
            // 调用CountDownLatch对象的await方法
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("异步计算结果为：" + result.get());
    }
    
    private int sum3(CountDownLatch countDownLatch) {
        int result = fibonacci(36);
        countDownLatch.countDown();
        return result;
    }

    /**
     * 方法10 使用LockSupport机制拿到结果
     */
    @Test
    public void method10() {
        // 定义一个原子变量
        AtomicInteger result = new AtomicInteger();
        // 由于LockSupport机制需要拿到线程对象，所以先获取当前线程的对象
        final Thread thread = Thread.currentThread();
        // 创建一个线程异步执行计算任务
        new Thread(() -> {
            result.set(sum4(thread));
        }).start();
        LockSupport.park();
        System.out.println("异步计算结果为：" + result);
    }
    
    private int sum4(Thread thread) {
        int result = fibonacci(36);
        LockSupport.unpark(thread);
        return result;
    }

    /**
     * 方法11 使用CyclicBarrier+ThreadLocal机制拿到结果，必须是main线程
     */
    public static void main(String[] args) {
        // 定以ThreadLocal线程对象
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        // 定以CyclicBarrier对象
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(1, () -> {
            // 最后一个达到条件的线程即最后调await()方法的线程输出计算结果
            System.out.println("异步计算结果为：" + threadLocal.get());
        });
        // 创建线程执行计算任务
        new Thread(() -> {
            sum5(threadLocal, cyclicBarrier);
        }).start();
    }
    
    private static void sum5(ThreadLocal<Integer> threadLocal, CyclicBarrier cyclicBarrier) {
        int result = fibonacci(36);
        threadLocal.set(result);
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
    
    private static int fibonacci(int a) {
        if (a < 2) {
            return a;
        }
        return fibonacci(a - 1) + fibonacci(a - 2);
    }
}
