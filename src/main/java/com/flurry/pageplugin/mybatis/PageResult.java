package com.flurry.pageplugin.mybatis;

import java.util.List;

/**
 * @Author weichao <gorilla@aliyun.com>
 * @Date 2018/10/10
 */
public interface PageResult<E>{


    /**
     * 最大记录数
     * @return
     */
    long getMaxRecords();

    /**
     * 数据记录
     * @return
     */
    List <E> getData();


}
