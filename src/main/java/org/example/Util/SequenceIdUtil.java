package org.example.Util;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceIdUtil {
    static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static Integer getSequenceId() {
        return atomicInteger.getAndIncrement();
    }
}
