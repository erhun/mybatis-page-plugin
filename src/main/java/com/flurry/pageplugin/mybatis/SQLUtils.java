package com.flurry.pageplugin.mybatis;

import com.flurry.pageplugin.mybatis.dialect.Dialect;
import com.flurry.pageplugin.mybatis.dialect.MySQLDialect;
import com.flurry.pageplugin.mybatis.dialect.OracleDialect;

/**
 * @author weichao<gorilla@aliyun.com>
 */
public class SQLUtils {


    public static final String MYSQL_DIALECT_NAME = "mysql";

    public static final String ORACLE_DIALECT_NAME = "oracle";

    private static Dialect dialect;

    public static Dialect getDialect() {
        return dialect;
    }

    static synchronized Dialect setDialect(String dialectName) {

        if(dialect != null){
            return dialect;
        }

        if(MYSQL_DIALECT_NAME.equals(dialectName)){
            dialect = new MySQLDialect();
        }else if(ORACLE_DIALECT_NAME.equals(dialectName)){
            dialect = new OracleDialect();
        }else{
            dialect = new MySQLDialect();
        }

        return dialect;

    }

}
