package com.flurry.pageplugin.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.util.Properties;

/**
 * @Author weichao <gorilla@aliyun.com>
 * @Date 2019/6/10
 */
public class PageInterceptor implements Interceptor {

    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {

        if(target instanceof Executor){
            Executor executor = (Executor) target;
            return new ExecutorProxy(executor, properties);
        }

        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
        String dialectName = (String) properties.getOrDefault("dialect", "mysql");
        Integer pageSize = ConvertUtils.toInt(properties.getProperty("default.page.size"), 20);
        SQLUtils.setDialect(dialectName);
        PageUtils.setDefaultPageSize(pageSize);
    }


}
