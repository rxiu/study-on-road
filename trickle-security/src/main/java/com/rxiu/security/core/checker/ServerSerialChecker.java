package com.rxiu.security.core.checker;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.rxiu.security.common.util.RSAUtil;
import com.rxiu.security.common.util.SerialNumberUtil;
import com.rxiu.security.core.checker.operator.AbstractSerialOperator;
import com.rxiu.security.core.checker.operator.JarSerialOperator;
import com.rxiu.security.core.checker.operator.SerialOperator;

import java.util.Map;

/**
 * @author rxiu
 * @date 2018/09/28.
 **/
public class ServerSerialChecker {

    SerialOperator operator;
    Map<String, Object> keyMap;
    private String publicKey;
    private String privateKey;

    public ServerSerialChecker setOperator(SerialOperator operator) {
        this.operator = operator;
        return this;
    }

    private static class Singleton {
        private static final ServerSerialChecker checker = new ServerSerialChecker();
    }

    public static ServerSerialChecker builder() {
        ServerSerialChecker checker = Singleton.checker;
        // 默认jar
        checker.setOperator(new JarSerialOperator());
        return checker;
    }

    public ServerSerialChecker configure() throws Exception {
        try {
            keyMap = RSAUtil.genKeyPair();
            publicKey = operator.get(AbstractSerialOperator.Type.PUBLIC_KEY.name());
            if (Strings.isNullOrEmpty(publicKey)) {
                publicKey = RSAUtil.getPublicKey(keyMap);
                operator.write(publicKey, AbstractSerialOperator.Type.PUBLIC_KEY.name());
            }

            privateKey = operator.get(AbstractSerialOperator.Type.PRIVATE_KEY.name());
            if (Strings.isNullOrEmpty(privateKey)) {
                privateKey = RSAUtil.getPrivateKey(keyMap);
                operator.write(privateKey, AbstractSerialOperator.Type.PRIVATE_KEY.name());
            }
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean verify() throws Exception {
        //是否首次加载
        boolean instance = checkInstance();
        String serialNumber = getServerUniqueCode();

        if (!instance) return verifySign(serialNumber);

        operator.write(serialNumber, AbstractSerialOperator.Type.SERIAL.name());
        return instance;
    }

    private boolean checkInstance() throws Exception {
        byte[] serialNumber = operator.get(AbstractSerialOperator.Type.SERIAL.name()).getBytes();
        if (serialNumber != null && serialNumber.length > 0) {
            for (int i = 0, len = serialNumber.length; i < len; i++) {
                if (serialNumber[i] != '0') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private String getServerUniqueCode() {
        return SerialNumberUtil.getSerialNumber();
    }

    private boolean verifySign(String... code) throws Exception {
        byte[] data = encode(code);
        String sign = RSAUtil.sign(operator.get(AbstractSerialOperator.Type.SERIAL.name()).getBytes(), privateKey);
        return RSAUtil.verify(data, publicKey, sign);
    }

    private byte[] encode(String ...code) throws Exception {
        String md5 = Hashing.md5().newHasher().putString(String.join("#", code), Charsets.UTF_8).hash().toString();
        byte[] data = md5.getBytes();
        return RSAUtil.encryptByPrivateKey(data, privateKey);
    }


}
