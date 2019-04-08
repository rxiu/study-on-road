package com.rxiu.hibernate.jdbc;

import com.google.common.base.Strings;

/**
 * Created by rxiu on 2019/2/28
 * 各种数据库方言：https://blog.csdn.net/yxl_1207/article/details/81293134
 */
public enum HibernateDialect {
    DB2("org.hibernate.dialect.DB2Dialect"),
    PostgreSQL("org.hibernate.dialect.PostgreSQLDialect"),
    MySQL5("org.hibernate.dialect.MySQL5Dialect"),
    MySQL57("org.hibernate.dialect.MySQL57Dialect"),
    MySQL5_with_InnoDB("org.hibernate.dialect.MySQL5InnoDBDialect"),
    MySQL_with_MyISAM("org.hibernate.dialect.MySQLMyISAMDialect"),
    Oracle_any_version("org.hibernate.dialect.OracleDialect"),
    Oracle_9i("org.hibernate.dialect.Oracle9iDialect"),
    Oracle_10g("org.hibernate.dialect.Oracle10gDialect"),
    Oracle_11g("org.hibernate.dialect.Oracle10gDialect"),
    Sybase("org.hibernate.dialect.SybaseASE15Dialect"),
    Sybase_Anywhere("org.hibernate.dialect.SybaseAnywhereDialect"),
    //Microsoft_SQL_Server_2000("net.sf.hibernate.dialect.SQLServerDialect"),
    Microsoft_SQL_Server_2000("com.rxiu.hibernate.jdbc.dialect.SQLServer2000Dialect"),
    Microsoft_SQL_Server_2005("org.hibernate.dialect.SQLServer2005Dialect"),
    Microsoft_SQL_Server_2008("org.hibernate.dialect.SQLServer2008Dialect"),
    Microsoft_SQL_Server_2012("org.hibernate.dialect.SQLServer2012Dialect"),
    Microsoft_SQL_Server_2016("org.hibernate.dialect.SQLServer2012Dialect"),
    H2_Database("org.hibernate.dialect.H2Dialect");

    private String dialect;

    HibernateDialect(String dialect) {
        this.dialect = dialect;
    }

    @Override
    public String toString() {
        return this.dialect;
    }

    private static boolean isDB2(String version) {
        return false;
    }

    private static boolean isPostgreSQL(String version) {
        return false;
    }

    private static boolean isMySQL5(String version) {
        return false;
    }

    private static boolean isMySQL57(String version) {
        return !Strings.isNullOrEmpty(version) && version.contains("5.7");
    }

    private static boolean isMySQL5_with_InnoDB(String version) {
        return false;
    }

    private static boolean isMySQL_with_MyISAM(String version) {
        return false;
    }

    private static boolean isOracle_any_version(String version) {
        return false;
    }

    private static boolean isOracle_9i(String version) {
        return !Strings.isNullOrEmpty(version) && version.startsWith("Oracle Database 9i");
    }

    private static boolean isOracle_10g(String version) {
        return !Strings.isNullOrEmpty(version) && version.startsWith("Oracle Database 10g");
    }

    private static boolean isOracle_11g(String version) {
        return !Strings.isNullOrEmpty(version) && version.startsWith("Oracle Database 11g");
    }

    private static boolean isSybase(String version) {
        return false;
    }

    private static boolean isMicrosoft_SQL_Server_2000(String version) {
        return !Strings.isNullOrEmpty(version) && version.startsWith("Microsoft SQL Server 2000");
    }

    private static boolean isMicrosoft_SQL_Server_2005(String version) {
        return !Strings.isNullOrEmpty(version) && version.startsWith("Microsoft SQL Server 2005");
    }

    private static boolean isMicrosoft_SQL_Server_2008(String version) {
        return !Strings.isNullOrEmpty(version) && version.startsWith("Microsoft SQL Server 2008");
    }

    private static boolean isMicrosoft_SQL_Server_2012(String version) {
        return !Strings.isNullOrEmpty(version) && version.startsWith("Microsoft SQL Server 2012");
    }

    private static boolean isMicrosoft_SQL_Server_2016(String version) {
        return !Strings.isNullOrEmpty(version) && version.startsWith("Microsoft SQL Server 2016");
    }

    private static boolean isH2_Database(String version) {
        return false;
    }

    public static String getDialect(JdbcType type, String version) {
        switch (type) {
            case MYSQL:
                if (isMySQL5(version)) {
                    return HibernateDialect.MySQL5.toString();
                } else if (isMySQL57(version)) {
                    return HibernateDialect.MySQL57.toString();
                } else if (isMySQL5_with_InnoDB(version)) {
                    return HibernateDialect.MySQL5_with_InnoDB.toString();
                } else if (isMySQL_with_MyISAM(version)) {
                    return HibernateDialect.MySQL_with_MyISAM.toString();
                }
                break;
            case ORACLE:
                if (isOracle_9i(version)) {
                    return HibernateDialect.Oracle_9i.toString();
                } else if (isOracle_10g(version)) {
                    return HibernateDialect.Oracle_10g.toString();
                } else if (isOracle_any_version(version)) {
                    return HibernateDialect.Oracle_any_version.toString();
                } else if (isOracle_11g(version)) {
                    return HibernateDialect.Oracle_11g.toString();
                }
                break;
            case MSSQL:
                if (isMicrosoft_SQL_Server_2000(version)) {
                    return HibernateDialect.Microsoft_SQL_Server_2000.toString();
                } else if (isMicrosoft_SQL_Server_2005(version)) {
                    return HibernateDialect.Microsoft_SQL_Server_2005.toString();
                } else if (isMicrosoft_SQL_Server_2008(version)) {
                    return HibernateDialect.Microsoft_SQL_Server_2008.toString();
                } else if (isMicrosoft_SQL_Server_2012(version)) {
                    return HibernateDialect.Microsoft_SQL_Server_2012.toString();
                } else if (isMicrosoft_SQL_Server_2016(version)) {
                    return HibernateDialect.Microsoft_SQL_Server_2016.toString();
                }
                break;
            case MSSQL2000:
                return HibernateDialect.Microsoft_SQL_Server_2000.toString();
            default:
                if (isDB2(version)) {
                    return HibernateDialect.DB2.toString();
                } else if (isPostgreSQL(version)) {
                    return HibernateDialect.PostgreSQL.toString();
                } else if (isSybase(version)) {
                    return HibernateDialect.Sybase.toString();
                } else if (isH2_Database(version)) {
                    return HibernateDialect.H2_Database.toString();
                }
        }

        return null;
    }
}
