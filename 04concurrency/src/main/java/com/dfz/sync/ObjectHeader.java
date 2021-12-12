package com.dfz.sync;

import org.openjdk.jol.info.ClassLayout;

public class ObjectHeader {
    public static void main(String[] args) {
        ObjectHeader objectHeader = new ObjectHeader();
        System.out.println(ClassLayout.parseInstance(objectHeader).toPrintable());
    }
}
