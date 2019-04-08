package com.rxiu.hibernate.common.util;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * Created by rxiu on 2018/3/29.
 */
public class StringUtil {

    public static String MD5(String text) {
        if (Strings.isNullOrEmpty(text)) {
            return null;
        }

        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(text, Charsets.UTF_8);
        return hasher.hash().toString();
    }

}
