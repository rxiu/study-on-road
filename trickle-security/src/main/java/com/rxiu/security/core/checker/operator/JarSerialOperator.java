package com.rxiu.security.core.checker.operator;

import com.rxiu.security.common.util.JarUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rxiu
 * @date 2018/09/28.
 **/
public class JarSerialOperator extends AbstractSerialOperator {

    private static final String FILE_SCHEME = "jar";

    @Override
    protected byte[] getValueByType(Type type) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(getPathByType(type));
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException("信息加载失败。");
        }
    }

    @Override
    protected void writeValueByType(byte[] value, Type type) throws Exception {
        String entryPath = getPathByType(type);
        String path = this.getClass().getClassLoader().getResource(entryPath).getPath();
        String jarPath = getJarPath(path);
        JarUtil.writeJarFile(jarPath, entryPath, value);
    }

    private String getJarPath(String path) {
        try {
            String scheme = this.getClass().getClassLoader().getResource(SERIAL_ENTRY).toURI().getScheme();
            if (FILE_SCHEME.equals(scheme)) {
                throw new RuntimeException("不支持的文件类型。");
            }

            String regex = "(?<=file:)(?<jarPath>.*)(?=." + scheme + "!.*)";
            Matcher matcher = Pattern.compile(regex).matcher(path);
            while (matcher.find()) {
                return String.join(".", matcher.group("jarPath"), scheme);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("未jar找到文件");
        }

        return null;
    }

    private String getPathByType(Type type) {
        switch (type) {
            case PUBLIC_KEY:
                return PUBLIC_ENTRY;
            case PRIVATE_KEY:
                return PRIVATE_ENTRY;
            case SERIAL:
                return SERIAL_ENTRY;
            default:
                throw new UnsupportedOperationException("未知类型。");
        }
    }
}
