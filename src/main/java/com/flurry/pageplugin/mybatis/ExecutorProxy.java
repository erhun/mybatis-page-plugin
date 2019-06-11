package com.flurry.pageplugin.mybatis;


import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author weichao <gorilla@aliyun.com>
 * @Date 2019/06/10
 */
public class ExecutorProxy implements Executor {

    private Executor delegate;

    private Properties properties;

    private Map <String, Boolean> pageStatements = new ConcurrentHashMap<>();

    private MappedStatement countMappedStatement;

    public ExecutorProxy(Executor delegate, Properties properties) {
        this.delegate = delegate;
        this.properties = properties;
    }

    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    public void close(boolean forceRollback) {
        delegate.close(forceRollback);
    }

    @Override
    public boolean isClosed() {
        return delegate.isClosed();
    }

    @Override
    public int update(MappedStatement ms, Object parameterObject) throws SQLException {
        return delegate.update(ms, parameterObject);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
        return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
    }

    @Override
    public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
        return delegate.queryCursor(ms, parameter, rowBounds);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)
            throws SQLException {

        if (isPagingMappedStatement(ms.getId())) {
            return queryByPage(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
        }

        return delegate.<E> query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
    }

    private <E> List<E> queryByPage(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
        PageResultImpl pageResult = new PageResultImpl();
        PageBoundSql pageBoundSql = new PageBoundSql(ms.getConfiguration(), boundSql);
        List <E> dataList = delegate.<E> query(ms, parameterObject, rowBounds, resultHandler, key, pageBoundSql);
        if(countable(parameterObject)) {
            List<Long> countList = executeCount(ms, parameterObject, rowBounds, boundSql);
            pageResult.setMaxRecords(countList.get(0));
        }else{
            pageResult.setMaxRecords(dataList.size());
        }
        pageResult.setData(dataList);
        return (List<E>) Arrays.asList(pageResult);
    }

    private List<Long> executeCount(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) throws SQLException {
        PageCountBoundSql pageCountBoundSql = new PageCountBoundSql(ms.getConfiguration(), boundSql);
        if (countMappedStatement == null) {
            MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId() + "count", new StaticSqlSource(ms.getConfiguration(), ""), null);
            List resultMaps = new ArrayList<ResultMap>(1);
            ResultMap inlineResultMap = new ResultMap.Builder(
                    ms.getConfiguration(),
                    ms.getId() + "-count-inline",
                    Long.class,
                    new ArrayList<>(1),
                    null).build();
            resultMaps.add(inlineResultMap);
            builder.resultMaps(resultMaps);
            countMappedStatement = builder.build();
        }
        return delegate.query(countMappedStatement, parameterObject, rowBounds, null, null, pageCountBoundSql);
    }

    private boolean countable(Object parameterObject) {
        if(parameterObject instanceof Map){
            Map tmp = (Map) parameterObject;
            if(tmp != null){
                Object lm = tmp.get("limit");
                if(lm instanceof Limits){
                    Limits limits = (Limits) lm;
                    int pageSize = ConvertUtils.toInt(tmp.get("pageSize"), PageUtils.getDefaultPageSize());
                    if(limits.getOffset() == 0 && pageSize < limits.getSize()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isPagingMappedStatement(String id) {

        Boolean pageabled = pageStatements.get(id);

        if(pageabled != null){
            return pageabled;
        }

        int idx = id.lastIndexOf(".");

        String className = id.substring(0, idx);
        String methodName = id.substring(idx + 1);

        Class clazz = getClass(className);

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if(methodName.equals(method.getName()) && PageResult.class.isAssignableFrom(method.getReturnType())){
                pageStatements.put(id, Boolean.TRUE);
                return true;
            }
        }

        pageStatements.put(id, Boolean.FALSE);

        return false;
    }

    @Override
    public List<BatchResult> flushStatements() throws SQLException {
        return delegate.flushStatements();
    }

    @Override
    public void commit(boolean required) throws SQLException {
        delegate.commit(required);
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        delegate.rollback(required);
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        return delegate.createCacheKey(ms, parameterObject, rowBounds, boundSql);
    }

    @Override
    public boolean isCached(MappedStatement ms, CacheKey key) {
        return delegate.isCached(ms, key);
    }

    @Override
    public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType) {
        delegate.deferLoad(ms, resultObject, property, key, targetType);
    }

    @Override
    public void clearLocalCache() {
        delegate.clearLocalCache();
    }

    @Override
    public void setExecutorWrapper(Executor executor) {
        delegate.setExecutorWrapper(executor);
    }

    private Class getClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
