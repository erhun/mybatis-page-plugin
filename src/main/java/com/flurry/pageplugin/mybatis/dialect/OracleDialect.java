package com.flurry.pageplugin.mybatis.dialect;


/**
 * 
 * @author weichao<gorilla@aliyun.com>
 *
 */
public class OracleDialect implements Dialect {

    @Override
	public void getLimitSQL(StringBuilder sql) {
        
        StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);
        
        pagingSelect.append("select * from (select row_.*, rownum rownum_ from (").append(sql).append(") row_ where rownum &lt;= #{limits.limit} ) where rownum_ &gt; limits.offset");

    }

    @Override
    public String getLimitCountSql(StringBuilder sql) {
        sql.insert(0, "select count(*) from (").append(") t");
        return sql.toString();
    }

    @Override
    public String convertDate(String value) {
        return "date'" + value + "'";
    }

    @Override
    public String convertTimestamp(String value) {
        return "timestamp'" + value + "'";
    }

    @Override
    public void getSingleLimitSQL(StringBuilder sql) {
       sql.append(" and rownum=1 ");
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
