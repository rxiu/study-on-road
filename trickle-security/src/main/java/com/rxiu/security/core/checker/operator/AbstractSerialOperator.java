package com.rxiu.security.core.checker.operator;

/**
 * @author rxiu
 * @date 2018/09/28.
 **/
public abstract class AbstractSerialOperator implements SerialOperator {

    protected static final String SERIAL_ENTRY = "application_serial";
    protected static final String PRIVATE_ENTRY = "private_key";
    protected static final String PUBLIC_ENTRY = "public_key";

    @Override
    public String get(String name) {
        Type type = Type.valueOf(name);
        return new String(getValueByType(type));
    }

    /**
     * 获取公钥、私钥、服务器标识
     * @param type
     * @return
     */
    protected abstract byte[] getValueByType(Type type);

    @Override
    public void write(String code, String name) {
        Type type = Type.valueOf(name);
        try {
            writeValueByType(code.getBytes(), type);
        } catch (Exception e) {
            throw new RuntimeException(String.format("%s写入失败{}", type), e);
        }
    }

    protected abstract void writeValueByType(byte[] value, Type type) throws Exception;

    public static enum Type {
        SERIAL,
        PRIVATE_KEY,
        PUBLIC_KEY;
    }
}
