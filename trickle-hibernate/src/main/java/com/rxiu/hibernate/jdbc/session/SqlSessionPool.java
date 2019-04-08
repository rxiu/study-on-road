package com.rxiu.hibernate.jdbc.session;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.rxiu.hibernate.common.Context;
import com.rxiu.hibernate.common.util.StringUtil;

import java.util.Map;

/**
 * Created by rxiu  on 2018/3/29.
 */
public class SqlSessionPool {

    private final Map<String, SqlSession> sessionMap = Maps.newHashMapWithExpectedSize(5);

    private static class Holder {
        private static SqlSessionPool sessionPool = new SqlSessionPool();

        public static SqlSessionPool instance() {
            return sessionPool;
        }
    }

    public static SqlSessionPool builder() {
        return Holder.instance();
    }

    public SqlSession getSqlSession(Context context) {
        String key = generateKey(context);

        SqlSession sqlSession = this.sessionMap.getOrDefault(key, null);

        if (null == sqlSession) {
            sqlSession = new SqlSession();
            sqlSession.configure(context);

            sqlSession.establish();
            this.sessionMap.put(key, sqlSession);
        }
        return sqlSession;
    }

    private static String generateKey(Context context) {
        String url = context.getString("connection.url");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "connection url is empty.");
        String username = context.getString("connection.username");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(username), "connection username is empty.");
        String password = context.getString("connection.password");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(password), "connection password is empty.");
        String dialect = context.getString("connection.dialect");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(dialect), "connection dialect class is empty.");

        String all = Joiner.on("#").join(dialect, url, username, password);
        String key = StringUtil.MD5(all);
        all = null;
        return key;
    }
}
