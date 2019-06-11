package com.flurry.pageplugin.mybatis;

/**
 * @author weichao<gorilla@aliyun.com>
 */
public class PageUtils {

    public static final String PAGE_SIZE_KEY = "pageSize";

    public static final String PAGE_NO_KEY = "pageNo";

    private static int defaultPageSize;

    public static int getDefaultPageSize() {
        return defaultPageSize;
    }

    public static void setDefaultPageSize(int defaultPageSize) {
        PageUtils.defaultPageSize = defaultPageSize;
    }
}
