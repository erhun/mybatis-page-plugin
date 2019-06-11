package com.flurry.pageplugin.mybatis;

import java.util.List;

/**
 * @Author weichao <gorilla@aliyun.com>
 * @Date 2018/10/10
 */
public class PageResultImpl<E> implements PageResult {

    private long maxRecords;

    private List <E> data;

    public PageResultImpl() {
    }

    public PageResultImpl(long maxRecords, List<E> data) {
        this.maxRecords = maxRecords;
        this.data = data;
    }

    @Override
    public long getMaxRecords() {
        return maxRecords;
    }

    @Override
    public List <E> getData() {
        return data;
    }

    void setMaxRecords(long maxRecords) {
        this.maxRecords = maxRecords;
    }

    void setData(List<E> data) {
        this.data = data;
    }
}
