package com.flurry.pageplugin.mybatis;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;

/**
 * @Author weichao <gorilla@aliyun.com>
 * @Date 2018/10/10
 */
public class PageCountBoundSql extends BoundSql {

    public PageCountBoundSql(Configuration configuration, BoundSql boundSql){
        super(configuration, boundSql.getSql(), boundSql.getParameterMappings(), boundSql.getParameterObject());
    }

    @Override
    public String getSql() {
        StringBuilder sql = new StringBuilder(super.getSql());
        return SQLUtils.getDialect().getLimitCountSql(sql);
    }

}
