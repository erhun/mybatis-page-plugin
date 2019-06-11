package com.flurry.pageplugin.mybatis.dialect;

/**
 * 
 * @author weichao<gorilla@aliyun.com>
 *
 */
public interface Dialect {

    /**
     * 
     * @param sql
     * @return
     */
    public void getLimitSQL(StringBuilder sql);

    /**
     *
     * @param sql
     * @return
     */
    public String getLimitCountSql(StringBuilder sql);

    /**
     *
     * @param value
     * @return
     */
    public String convertDate(String value);

    /**
     *
     * @param value
     * @return
     */
    public String convertTimestamp(String value);
    
    /**
     * 
     * @param sql
     * @return
     */
    public void getSingleLimitSQL(StringBuilder sql);

    public String getStartsLikeSQL(String value);

    public String getEndsLikeSQL(String value);

    public String getLikeSQL(String value);

}
