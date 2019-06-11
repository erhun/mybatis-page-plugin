package com.flurry.pageplugin.mybatis.dialect;


/**
 * 
 * @author weichao<gorilla@aliyun.com>
 *
 */
public class MySQLDialect implements Dialect {

    @Override
	public void getLimitSQL(StringBuilder sql) {
        sql.append(" limit #{limit.offset}, #{limit.size}");
    }

    @Override
    public String getLimitCountSql(StringBuilder sql) {
        sql.insert(0, "select count(*) from (").append(") t");
        return sql.toString();
    }

    @Override
    public String convertDate(String value) {
        return "'" + value + "'";
    }

    @Override
    public String convertTimestamp(String value) {
        return "'" + value + "'";
    }

    @Override
    public void getSingleLimitSQL(StringBuilder sql) {
       sql.append(" limit 1");
    }

    @Override
    public String getStartsLikeSQL(String value) {
        return " like '" + value + "%'";
    }

    @Override
    public String getEndsLikeSQL(String value) {
        return " like '%" + value + "'";
    }

    @Override
    public String getLikeSQL(String value) {
        return " like '%" + value + "%'";
    }


}
