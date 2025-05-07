package org.example.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    private static final int THREAD_POOL_SIZE = 5;
    private static final ExecutorService dbExecutorService;

    static {
        dbExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    /**
     * 获取数据库操作线程池
     * @return 数据库操作线程池
     */
    public static ExecutorService getDBExecutorService() {
        return dbExecutorService;
    }
}