package com.dfz.collection;

import java.util.Arrays;

/**
 * 由CopyOnWrtieArrayList中迭代器的源码测试：快照和数组拷贝的区别？
 * 结论：快照其实就是新创建了一个引用指向旧数组，然后通过拷贝new一个新的数组来操作，
 *      不会影响快照指向的内容。
 */
public class TestArraySnapshot {
    
    private final Integer[] snapshot;

    private TestArraySnapshot(Integer[] elements) {
        snapshot = elements;
    }

    public Integer[] getSnapshot() {
        return snapshot;
    }

    public static void main(String[] args) {
        Integer[] arr = new Integer[]{1, 2, 3};
        TestArraySnapshot testArraySnapshot = new TestArraySnapshot(arr);
        arr[1] = 1;
        // 这里注意int类型的数组就算转成list也无法直接调println输出数组中的内容
        // arr使用Integer类型的数组
        System.out.println(Arrays.asList(arr));
        System.out.println(Arrays.asList(testArraySnapshot.getSnapshot()));
    }
}
