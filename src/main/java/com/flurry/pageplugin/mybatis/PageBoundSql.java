package com.flurry.pageplugin.mybatis;

import com.flurry.pageplugin.mybatis.dialect.MySQLDialect;
import com.flurry.pageplugin.mybatis.dialect.OracleDialect;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author weichao <gorilla@aliyun.com>
 * @Date 2018/10/10
 */
public class PageBoundSql extends BoundSql {

    private final static String offsetKey = "#{limit.offset}";

    private final static String sizeKey = "#{limit.size}";

    private final static String limitKey = "#{limit.limit}";

    private List<ParameterMapping> parameterMappings;

    public PageBoundSql(Configuration configuration, BoundSql boundSql){

        super(configuration, boundSql.getSql(), boundSql.getParameterMappings(), boundSql.getParameterObject());

        parameterMappings = new ArrayList<>(super.getParameterMappings());

        int size = parameterMappings.size();

        if(size == 0){
            parameterMappings = new ArrayList<>(2);
        }

        Object parameterObject = boundSql.getParameterObject();

        if(parameterObject instanceof Map){
            Map tmp = ((Map) parameterObject);
            if(!tmp.containsKey("limit") && tmp.containsKey(PageUtils.PAGE_NO_KEY)){
                Integer pageNo = ConvertUtils.toInt(tmp.get(PageUtils.PAGE_NO_KEY), 1);
                Integer pageSize = ConvertUtils.toInt(tmp.get(PageUtils.PAGE_SIZE_KEY), PageUtils.getDefaultPageSize());
                if(pageSize < 1 || pageSize > 100){
                    pageSize = PageUtils.getDefaultPageSize();
                }
                tmp.put("limit", Limits.of(pageNo, pageSize));
            }
        }

        parameterMappings.add(new ParameterMapping.Builder(configuration, "limit.offset", Long.class).build());

        if(SQLUtils.getDialect() instanceof MySQLDialect) {
            parameterMappings.add(new ParameterMapping.Builder(configuration, "limit.size", Integer.class).build());
        }else if(SQLUtils.getDialect() instanceof OracleDialect) {
            parameterMappings.add(new ParameterMapping.Builder(configuration, "limit.limit", Integer.class).build());
        }
    }

    @Override
    public String getSql() {
        StringBuilder sql = new StringBuilder(super.getSql());
        SQLUtils.getDialect().getLimitSQL(sql);

        int idx = sql.indexOf(offsetKey);

        if(idx > -1) {
            sql.replace(idx, idx + offsetKey.length(), "?");
        }

        idx = sql.indexOf(sizeKey);

        if(idx > -1) {
            sql.replace(idx, idx + sizeKey.length(), "?");
        }

        idx = sql.indexOf(limitKey);

        if(idx > -1) {
            sql.replace(idx, idx + limitKey.length(), "?");
        }

        return sql.toString();
    }

    @Override
    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }
}
