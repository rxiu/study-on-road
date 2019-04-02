package com.rxiu.security.common.util;

import com.google.common.base.Strings;
import com.rxiu.security.common.PlatformType;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author rxiu
 * @date 2018/09/18.
 **/
public class SerialNumberUtil {

    /**
     * 获取主板序列号
     *
     * @return
     */
    private static String getMotherboardSN() {
        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);

            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_BaseBoard\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.SerialNumber \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    /**
     * 获取硬盘序列号(该方法获取的是 盘符的逻辑序列号,并不是硬盘本身的序列号)
     * 硬盘序列号还在研究中
     *
     * @param drive 盘符
     * @return
     */
    private static String getHardDiskSN(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\""
                    + drive
                    + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber"; // see note
            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    /**
     * 获取CPU序列号
     *
     * @return
     */
    private static String getCPUSerial() {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.ProcessorId \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            // + "    exit for  \r\n" + "Next";
            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (result.trim().length() < 1 || result == null) {
            result = "无CPU_ID被读取";
        }
        return result.trim();
    }

    private static List<String> getLocalHostLANAddress() throws UnknownHostException, SocketException {
        List<String> ips = new ArrayList<String>();
        Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
        while (interfs.hasMoreElements()) {
            NetworkInterface interf = interfs.nextElement();
            Enumeration<InetAddress> addres = interf.getInetAddresses();
            while (addres.hasMoreElements()) {
                InetAddress in = addres.nextElement();
                if (in instanceof Inet4Address) {
                    if (!"127.0.0.1".equals(in.getHostAddress())) {
                        ips.add(in.getHostAddress());
                    }
                }
            }
        }
        return ips;
    }

    /**
     * MAC
     * 通过jdk自带的方法,先获取本机所有的ip,然后通过NetworkInterface获取mac地址
     *
     * @return
     */
    private static String getMac() {
        try {
            List<String> ls = getLocalHostLANAddress();
            if (ls == null || ls.isEmpty()) {
                return "";
            }
            InetAddress ia = InetAddress.getByName(ls.get(0));
            byte[] mac = NetworkInterface.getByInetAddress(ia)
                    .getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            Stream.iterate(0, i -> i + 1).limit(mac.length).forEach(i -> {
                if (i != 0) {
                    sb.append("-");
                }
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            });
            return sb.toString().toUpperCase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /***************************linux*********************************/

    private static String executeScript(String[] cmd) {
        try {
            Runtime run = Runtime.getRuntime();
            Process process;
            process = run.exec(cmd);
            InputStream in = process.getInputStream();
            BufferedReader bs = new BufferedReader(new InputStreamReader(in));
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[8192];
            for (int n; (n = in.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }

            in.close();
            process.destroy();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String executeLinuxCmd(String cmd) {
        return executeScript(new String[]{"/bin/sh", "-c", cmd});
    }

    /**
     * @param cmd    命令语句
     * @param record 要查看的字段
     * @param symbol 分隔符
     * @return
     */
    private static String getSerialNumber(String cmd, String record, String symbol) {
        String execResult = executeLinuxCmd(cmd);
        String[] infos = execResult.split("\n");

        for (String info : infos) {
            final String inf = info.trim();
            info = info.trim();
            String[] res = record.split("\\|");
            boolean temp;
            if (res.length > 1) {
                temp = Arrays.stream(res).filter(r -> inf.indexOf(r) != -1).count() > 0;
            } else {
                temp = info.indexOf(record) != -1;
            }
            if (temp) {
                if (!" ".equals(symbol)) {
                    info.replace(" ", "");
                }
                String[] sn = info.split(symbol);
                return sn[1];
            }
        }

        return "";
    }

    /**
     * 获取CPUID、硬盘序列号、MAC地址、主板序列号
     *
     * @return
     */
    private static String getServerInfo() {
        String os = System.getProperty("os.name");
        os = os.toUpperCase();

        String cpuid, mainboard, disk, mac;

        if (PlatformType.Linux == PlatformType.currentPlatform()) {
            cpuid = getSerialNumber("dmidecode -t processor | grep 'ID'", "ID", ":");
            mainboard = getSerialNumber("dmidecode |grep 'Serial Number'", "Serial Number", ":");
            disk = getSerialNumber("fdisk -l", "Disk identifier|磁盘标识符", ":|：");
            mac = getSerialNumber("ifconfig -a", "ether", " ");
        } else {
            cpuid = SerialNumberUtil.getCPUSerial();
            mainboard = SerialNumberUtil.getMotherboardSN();
            disk = SerialNumberUtil.getHardDiskSN("c");
            mac = SerialNumberUtil.getMac();
        }

        return String.join("#", cpuid, mainboard, disk, mac);
    }

    private static String getServerUUID() {
        if (PlatformType.Linux == PlatformType.currentPlatform()) {
            String result = executeLinuxCmd("cat /sys/class/dmi/id/board_serial");
            if (Strings.isNullOrEmpty(result) || "0\n".equals(result)) {
                return "";
            }
            return result;
        } else {
            String result = executeScript(new String[]{"cmd", "/c", "wmic csproduct get uuid"});
            String[] keyAndValue = result.split(System.getProperty("line.separator"));
            return keyAndValue.length > 1 ? keyAndValue[1].trim() : "";
        }
    }

    public static String getSerialNumber() {
        String uuid = getServerUUID();
        if (Strings.isNullOrEmpty(uuid)) {
            uuid = getServerInfo();
            if (Strings.isNullOrEmpty(uuid)) {
                throw new NullPointerException("加载服务器uuid失败");
            }
        }
        return uuid;
    }

    /**
     * linux
     * cpuid : dmidecode -t processor | grep 'ID'
     * mainboard : dmidecode |grep 'Serial Number'
     * disk : fdisk -l
     * mac : ifconfig -a
     *
     * @param args
     */
/*    public static void main(String[] args) {
        System.out.println(getSerialNumber());
    }*/
}
