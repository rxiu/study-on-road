package com.rxiu.hibernate.jdbc.session;

import com.alibaba.druid.support.hibernate.DruidConnectionProvider;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.rxiu.hibernate.common.Context;
import com.rxiu.hibernate.jdbc.HibernateDialect;
import com.rxiu.hibernate.jdbc.JdbcType;
import com.rxiu.hibernate.jdbc.exception.SqlExecutionException;
import com.rxiu.hibernate.jdbc.string.SqlStringBuilder;
import com.rxiu.hibernate.jdbc.wrapper.StatementWrapper;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.transform.Transformers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by rxiu on 2018/3/29.
 */
public class SqlSession {

    private String url;
    private String username;
    private String password;
    private String driver;

    Configuration config;

    private SessionFactory factory;
    private ServiceRegistry registry;

    private ThreadLocal<Session> sessionLocal = ThreadLocal.withInitial(() -> createSession());

    private static Map<String, String> hibernateMap = Maps.newHashMapWithExpectedSize(20);

    static {
        Properties prop = new Properties();
        try {
            prop.load(SqlSession.class.getResourceAsStream("/jdbc.common.yml"));
        } catch (IOException e) {
        }

        hibernateMap = (Map) prop;
    }

    public void configure(Context context) {
        String dialect = context.getString("connection.dialect");
        Preconditions.checkArgument(dialect != null, "connection dialect is empty.");

        this.url = context.getString("connection.url");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(this.url), "connection url is empty.");

        this.username = context.getString("connection.username");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(this.username), "connection username is empty.");

        this.password = context.getString("connection.password");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(this.password), "connection password is empty.");

        this.driver = loadDriver(dialect);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(this.driver), "connection driver class is empty.");

        this.config = new Configuration();
        this.addConfigProperty("hibernate.connection.provider_class", DruidConnectionProvider.class.getTypeName());

        this.addConfigProperty("driverClassName", this.driver);
        this.addConfigProperty("url", this.url);
        this.addConfigProperty("username", this.username);
        this.addConfigProperty("password", this.password);

        JdbcType type = JdbcType.valueOf(dialect.toUpperCase());
        String hibernateDialect = HibernateDialect.getDialect(type, "Microsoft SQL Server 2000");
        if(!Strings.isNullOrEmpty(hibernateDialect)) {
            addConfigProperty("hibernate.dialect", hibernateDialect);
        }

        if (hibernateMap != null && !hibernateMap.isEmpty()) {
            hibernateMap.forEach((k, v) -> {
                addConfigProperty(k, v);
            });
        }
    }

    public synchronized void establish() {
        this.registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        this.factory = config.buildSessionFactory(this.registry);
    }

    public Session createSession() {
        Session session = factory.openSession();
        session.setCacheMode(CacheMode.IGNORE);
        session.setDefaultReadOnly(true);
        return session;
    }

    public <T> T executeSingle(final String sqlString, Class<T> t, Object... parameters) throws SqlExecutionException {
        Session session = sessionLocal.get();
        try {
            Query query = session.createSQLQuery(sqlString);
            if (parameters != null && parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    query.setParameter(i, parameters[i]);
                }
            }
            return (T) query.setResultTransformer(Transformers.aliasToBean(t)).uniqueResult();
        } catch (Exception e) {
            if (e.getCause() == null)
                throw new SqlExecutionException(sqlString, e);
            throw new SqlExecutionException(sqlString, e.getCause());
        } finally {
            session.close();
            session = null;
            sessionLocal.remove();
        }
    }

    public <T> List<T> executeQuery(final String sqlString, Integer current, Integer maxResult, Integer fetchSize, Class<T> t, Object... parameters) throws SqlExecutionException {
        List<T> rowsList;
        Session session = sessionLocal.get();
        try {
            Query query = session.createSQLQuery(sqlString);
            if (current != null) {
                query.setFirstResult(current);
            }

            if (maxResult != null && maxResult > 0) {
                query = query.setMaxResults(maxResult);
            }

            if (fetchSize != null && fetchSize > 0) {
                query = query.setFetchSize(fetchSize);
            }

            if (parameters != null && parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    query.setParameter(i, parameters[i]);
                }
            }

            rowsList = query.setResultTransformer(Transformers.aliasToBean(t)).list();
        } catch (Exception e) {
            throw new SqlExecutionException(sqlString, e.getCause());
        } finally {
            session.close();
            session = null;
            sessionLocal.remove();
        }
        return rowsList;
    }

    public void executeWithTransaction(final SqlStringBuilder sqlString, final Wrapper wrapper) throws Exception {
        Session session = sessionLocal.get();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.doWork(connection -> {
                StatementWrapper statementWrapper = new StatementWrapper(this, sqlString, connection);
                if (wrapper != null) {
                    wrapper.accept(statementWrapper);
                }
                statementWrapper = null;
            });
            transaction.commit();
        } catch (SqlExecutionException ex) {
            throw ex;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (e.getCause() == null)
                throw e;

            throw (Exception) e.getCause();
        } finally {
            session.close();
            session = null;
            sessionLocal.remove();
        }
    }

    public synchronized void close() {
        factory.close();
    }

    private void addConfigProperty(String key, String value) {
        this.config.setProperty(key, value);
    }

    private String loadDriver(String dialect) {
        JdbcType type = JdbcType.valueOf(dialect.toUpperCase());
        switch (type) {
            case MYSQL:
                return "com.mysql.cj.jdbc.Driver";
            case ORACLE:
                return "oracle.jdbc.driver.OracleDriver";
            case MSSQL:
                //return "org.hibernate.dialect.SQLServerDialect";
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            default:
                return null;
        }
    }

    @FunctionalInterface
    public interface Wrapper {
        void accept(StatementWrapper wrapper) throws SQLException;
    }
}
