package com.rxiu.zkui.core.curator;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.rxiu.zkui.core.PropertyPlaceHolder;
import com.rxiu.zkui.core.curator.pool.Pool;
import com.rxiu.zkui.core.curator.validator.Validator;
import com.rxiu.zkui.core.exception.BasicException;
import com.rxiu.zkui.core.exception.ExceptionResult;

import java.util.Map;

/**
 * @author shenyuhang
 * @date 2019/5/28
 **/
public class ZkCuratorManager {
    public final static String ZK_SYSTEM_NODE = "zookeeper";

    private static class Holder {
        private final static Map<String, Pool> map = Maps.newConcurrentMap();

        private static ZkCurator getCurator(String zkServer) {
            synchronized (map) {
                Pool pool = null;
                ZkCurator curator;
                if (map.containsKey(zkServer)) {
                    pool =  map.get(zkServer);
                }
                if (pool == null) {
                    Validator validator = new ZkCuratorValidator();
                    pool = new ZkCuratorPool(10, validator);
                    map.put(zkServer, pool);
                }
                curator = (ZkCurator) pool.get();
                if (curator == null) {
                    curator = buildCurator(zkServer, "");
                    pool.put(curator);
                }
                return curator;
            }
        }

        private static ZkCurator buildCurator(String zkServer, String basePath) {
            if (Strings.isNullOrEmpty(zkServer)) {
                throw new BasicException(ExceptionResult.PARAMETER_NULL_EXCEPTION, "zkServer");
            }

            Integer connectTimeout = PropertyPlaceHolder.getInteger("zkConnectTimeout");

            Integer sessionTimeout = PropertyPlaceHolder.getInteger("zkSessionTimeout");

            Integer retry = PropertyPlaceHolder.getInteger("zkMaxRetry");

            ZkCurator curator = new ZkCurator(zkServer, basePath, connectTimeout, sessionTimeout, retry);

            return curator;
        }

        private static void release(String zkServer, ZkCurator curator) {
            synchronized (map) {
                if (map.containsKey(zkServer)) {
                    Pool pool = map.get(zkServer);
                    pool.release(curator);
                }
            }
        }

        private static void destroy() {
            for (Map.Entry<String, Pool> entry : map.entrySet()) {
                Pool pool = entry.getValue();
                pool.shutdown();
            }
            map.clear();
        }
    }

    public static ZkCurator getCurator(String zkServer) {
        return Holder.getCurator(zkServer);
    }

    public static void release(String zkServer, ZkCurator curator){
        Holder.release(zkServer, curator);
    }

    public static void destroy() {

    }
}
