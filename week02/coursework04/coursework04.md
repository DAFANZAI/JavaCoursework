# GC和堆内存总结
## 测试源码为：GCLogAnalysis.java
## JDK版本：1.8.0_261
## 串行GC
> java -XX:+UseSerialGC -Xms256m -Xmx256m -XX:+PrintGCDetails GCLogAnalysis

* 创建对象4682个
* FullGC执行47次，每次耗时1ms~26ms
* YoungGC执行9次，每次耗时16ms~60ms

> java -XX:+UseSerialGC -Xms1g -Xmx1g -XX:+PrintGCDetails -Xloggc:SerialGC-02.log GCLogAnalysis

* 创建对象9591个
* FullGC执行0次
* YoungGC执行9次， 耗时在56ms~96ms

> java -XX:+UseSerialGC -Xms4g -Xmx4g -XX:+PrintGCDetails -Xloggc:SerialGC-03.log GCLogAnalysi

* 创建对象4071个
* FullGC执行0次
* YoungGC执行1次，每次耗时273ms

## 并行GC
> java -XX:+UseParallelGC -Xms256m -Xmx256m -XX:+PrintGCDetails -Xloggc:ParallelGC-01.log GCLogAnalysis

```log
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at GCLogAnalysis.generateGarbage(GCLogAnalysis.java:48)
	at GCLogAnalysis.main(GCLogAnalysis.java:25)
```

* FullGC执行33次，每次耗时2ms~17ms
* YoungGC执行13次，每次耗时1ms~10ms

> java -XX:+UseParallelGC -Xms1g -Xmx1g -XX:+PrintGCDetails -Xloggc:ParallelGC-02.log GCLogAnalysis

* 创建对象7635个
* FullGC执行0次
* YoungGC执行10次，每次耗时5ms~80ms

> java -XX:+UseParallelGC -Xms4g -Xmx4g -XX:+PrintGCDetails -Xloggc:ParallelGC-03.log GCLogAnalysis

* 创建对象3927个
* FullGC执行0次
* YoungGC执行1次，每次耗时125ms

## CMS GC
> java -XX:+UseConcMarkSweepGC -Xms256m -Xmx256m -XX:+PrintGCDetails -Xloggc:ConcMarkSweepGC-01.log GCLogAnalysis

* 创建对象4366个
* FullGC执行12次, 每次耗时0ms~37ms
* YoungGC执行22次，每次耗时0ms~16ms

> java -XX:+UseConcMarkSweepGC -Xms1g -Xmx1g -XX:+PrintGCDetails -Xloggc:ConcMarkSweepGC-02.log GCLogAnalysis

* 创建对象10346个
* FullGC执行0次
* YoungGC执行9次，每次耗时14ms~62ms

> java -XX:+UseConcMarkSweepGC -Xms4g -Xmx4g -XX:+PrintGCDetails -Xloggc:ConcMarkSweepGC-03.log GCLogAnalysis

* 创建对象5251个
* FullGC执行0次
* YoungGC执行2次，每次耗时250ms~263ms

## G1 GC
> java -XX:+UseG1GC -Xms256m -Xmx256m -XX:+PrintGC -Xloggc:G1GC-01.log GCLogAnalysis

```log
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at GCLogAnalysis.generateGarbage(GCLogAnalysis.java:48)
	at GCLogAnalysis.main(GCLogAnalysis.java:25)
```

* FullGC执行14次，每次耗时1ms~17ms
* YoungGC执行39次，每次耗时0ms~9ms

> java -XX:+UseG1GC -Xms1g -Xmx1g -XX:+PrintGC -Xloggc:G1GC-02.log GCLogAnalysis

* 创建对象8388个
* FullGC执行0次
* YoungGC执行10次，每次耗时5ms~65ms

> java -XX:+UseG1GC -Xms4g -Xmx4g -XX:+PrintGC -Xloggc:G1GC-03.log GCLogAnalysis

* 创建对象12806个
* FullGC执行0次
* YoungGC执行12次，每次耗时15ms~60ms

## 总结
* 随着内存的逐渐增大GC次数会减少，但每次GC的执行耗时会增加
* 随着内存的逐渐增大FullGC次数逐渐减少，young区有足够的空间来缓存对象
* 当内存较小时会导致oom异常，频繁触发YoungGC和FullGC
* 内存相同情况下并行GC的执行效率要高于串行GC，执行耗时更短
* CMS GC相比于Parallel GC执行时间较长，吞吐量比较低
* G1 GC执行效率要明显高于其他垃圾回收器，这种优势在内存比较大时更加明显