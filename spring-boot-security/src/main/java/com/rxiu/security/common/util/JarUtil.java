package com.rxiu.security.common.util;

import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @author rxiu
 * @date 2018/09/19.
 **/
public class JarUtil {

    /**
     * jar包拷贝
     * @param jarFilePath   jar包路径
     * @param des   目标文件路径
     * @throws IOException
     */
    public static void copyJarFile(File jarFilePath, File des) throws IOException {
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> entries = jarFile.entries();
        JarOutputStream jarOut = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(des)));
        byte[] bytes = new byte[1024];

        while (entries.hasMoreElements()) {
            JarEntry entryTemp = entries.nextElement();
            jarOut.putNextEntry(entryTemp);
            BufferedInputStream in = new BufferedInputStream(jarFile.getInputStream(entryTemp));
            int len = in.read(bytes, 0, bytes.length);
            while (len != -1) {
                jarOut.write(bytes, 0, len);
                len = in.read(bytes, 0, bytes.length);
            }
            in.close();
            jarOut.closeEntry();
        }

        jarOut.finish();
        jarOut.close();
        jarFile.close();
    }

    /**
     * 读取jar包里面指定文件的内容
     * @param jarFilePath jar包文件路径
     * @param fileName  文件名
     * @throws IOException
     */
    public static String readJarFile(String jarFilePath, String fileName) throws IOException{
        JarFile jarFile = new JarFile(jarFilePath);
        JarEntry entry = jarFile.getJarEntry(fileName);
        InputStream input = jarFile.getInputStream(entry);
        String result = readFile(input);
        jarFile.close();
        return result;
    }


    public static String readFile(InputStream input) throws IOException{
        InputStreamReader in = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(in);
        StringBuilder builder = new StringBuilder();
        String line ;

        while((line = reader.readLine())!=null){
            builder.append(line).append(System.getProperty("line.separator"));
        }
        reader.close();
        return builder.toString();
    }

    /**
     * 读取流
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 修改Jar包里的文件或者添加文件
     * @param jarFilePath jar包路径
     * @param entryName 要写的文件名
     * @param data   文件内容
     * @throws Exception
     */
    public static void writeJarFile(String jarFilePath,String entryName,byte[] data) throws Exception{

        JarFile  jarFile = new JarFile(jarFilePath);
        TreeMap tm = new TreeMap();
        Enumeration es = jarFile.entries();
        while(es.hasMoreElements()){
            JarEntry je = (JarEntry)es.nextElement();
            byte[] b = readStream(jarFile.getInputStream(je));
            tm.put(je.getName(),b);
        }

        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFilePath));
        Iterator it = tm.entrySet().iterator();
        boolean has = false;

        while(it.hasNext()){
            Map.Entry item = (Map.Entry) it.next();
            String name = (String)item.getKey();
            JarEntry entry = new JarEntry(name);
            jos.putNextEntry(entry);
            byte[] temp ;
            if(name.equals(entryName)){
                temp = data;
                has = true ;
            }else{
                temp = (byte[])item.getValue();
            }
            jos.write(temp, 0, temp.length);
        }

        if(!has){
            JarEntry newEntry = new JarEntry(entryName);
            jos.putNextEntry(newEntry);
            jos.write(data, 0, data.length);
        }
        jos.finish();
        jos.close();
    }

    /**
     * 测试案例
     * @param args
     * @throws Exception
     */
    /*public static void main(String args[]) throws Exception{

        String jarPath = "G:\\workspace\\transportman\\transportman-dist-app\\war\\server\\serial.jar";
        String entry = "application_serial";

        copyJarFile(new File("G:\\workspace\\transportman\\transportman-dist-app\\war\\server\\serial - 副本.jar"),
                new File(jarPath));

        System.out.println("修改前："+readJarFile(jarPath,entry));

        String data = "helloBabydsafsadfasdfsdafsdgasdgweqtqwegtqwfwefasdfasfadfasf";
        writeJarFile(jarPath,entry,data.getBytes());
        System.out.println("修改后："+ readJarFile(jarPath,entry));


    }*/

}
