package com.rxiu.gateway.core.ribbon;


import com.rxiu.gateway.entity.ServerAddress;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class LoadBalancerRuleUtil {

    /**
     * 返回最大公约数
     *
     * @param a
     * @param b
     * @return
     */
    private static int gcd(int a, int b) {
        BigInteger b1 = new BigInteger(String.valueOf(a));
        BigInteger b2 = new BigInteger(String.valueOf(b));
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    /**
     * 返回所有服务器权重的最大公约数
     *
     * @param addressList
     * @return
     */
    public static int getGCDForServers(List<ServerAddress> addressList) {
        int w = 0;
        for (int i = 0, len = addressList.size(); i < len - 1; i++) {
            if (w == 0) {
                w = gcd(addressList.get(i).getWeight(), addressList.get(i + 1).getWeight());
            } else {
                w = gcd(w, addressList.get(i + 1).getWeight());
            }
        }
        return w;
    }

    /**
     * 返回所有服务器中的最大权重
     *
     * @param addressList
     * @return
     */
    public static int getMaxWeightForServers(List<ServerAddress> addressList) {
        return addressList.stream().max(Comparator.comparing(ServerAddress::getWeight)).get().getWeight();
    }

    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值
     */
    public static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }
}
