package com.flurry.pageplugin.test;

import com.flurry.pageplugin.mybatis.PageResult;

import java.util.Map;

/**
 * @Author weichao <gorilla@aliyun.com>
 * @Date 2019/6/10
 */
public interface TestDAO {


    PageResult queryByPage(Map<String, Object> params);
}
