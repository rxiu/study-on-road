package com.rxiu.security.common;

/**
 * Created by rxiu on 2018/3/20.
 */
public enum PlatformType {
    Any("any"),
    Linux("Linux"),
    MacOS("Mac OS"),
    MacOSX("Mac OS X"),
    Windows("Windows"),
    OS2("OS/2"),
    Solaris("Solaris"),
    SunOS("SunOS"),
    MPEiX("MPE/iX"),
    HP_UX("HP-UX"),
    AIX("AIX"),
    OS390("OS/390"),
    FreeBSD("FreeBSD"),
    Irix("Irix"),
    Digital_Unix("Digital Unix"),
    NetWare("NetWare"),
    OSF1("OSF1"),
    OpenVMS("OpenVMS"),
    Others("Others");

    private String description;

    PlatformType(String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        return description;
    }

    private static String OS_NAME = System.getProperty("os.name").toLowerCase();

    private static boolean isLinux() {
        return OS_NAME.indexOf("linux") >= 0;
    }

    private static boolean isMacOS() {
        return OS_NAME.indexOf("mac") >= 0 && OS_NAME.indexOf("os") > 0 && OS_NAME.indexOf("x") < 0;
    }

    private static boolean isMacOSX() {
        return OS_NAME.indexOf("mac") >= 0 && OS_NAME.indexOf("os") > 0 && OS_NAME.indexOf("x") > 0;
    }

    private static boolean isWindows() {
        return OS_NAME.indexOf("windows") >= 0;
    }

    private static boolean isOS2() {
        return OS_NAME.indexOf("os/2") >= 0;
    }

    private static boolean isSolaris() {
        return OS_NAME.indexOf("solaris") >= 0;
    }

    private static boolean isSunOS() {
        return OS_NAME.indexOf("sunos") >= 0;
    }

    private static boolean isMPEiX() {
        return OS_NAME.indexOf("mpe/ix") >= 0;
    }

    private static boolean isHPUX() {
        return OS_NAME.indexOf("hp-ux") >= 0;
    }

    private static boolean isAix() {
        return OS_NAME.indexOf("aix") >= 0;
    }

    private static boolean isOS390() {
        return OS_NAME.indexOf("os/390") >= 0;
    }

    private static boolean isFreeBSD() {
        return OS_NAME.indexOf("freebsd") >= 0;
    }

    private static boolean isIrix() {
        return OS_NAME.indexOf("irix") >= 0;
    }

    private static boolean isDigitalUnix() {
        return OS_NAME.indexOf("digital") >= 0 && OS_NAME.indexOf("unix") > 0;
    }

    private static boolean isNetWare() {
        return OS_NAME.indexOf("netware") >= 0;
    }

    private static boolean isOSF1() {
        return OS_NAME.indexOf("osf1") >= 0;
    }

    private static boolean isOpenVMS() {
        return OS_NAME.indexOf("openvms") >= 0;
    }

    /**
     * 获取操作系统名字
     *
     * @return 操作系统名
     */
    public static PlatformType currentPlatform() {
        if (isAix()) {
            return PlatformType.AIX;
        } else if (isDigitalUnix()) {
            return PlatformType.Digital_Unix;
        } else if (isFreeBSD()) {
            return PlatformType.FreeBSD;
        } else if (isHPUX()) {
            return PlatformType.HP_UX;
        } else if (isIrix()) {
            return PlatformType.Irix;
        } else if (isLinux()) {
            return PlatformType.Linux;
        } else if (isMacOS()) {
            return PlatformType.MacOS;
        } else if (isMacOSX()) {
            return PlatformType.MacOSX;
        } else if (isMPEiX()) {
            return PlatformType.MPEiX;
        } else if (isNetWare()) {
            return PlatformType.NetWare;
        } else if (isOpenVMS()) {
            return PlatformType.OpenVMS;
        } else if (isOS2()) {
            return PlatformType.OS2;
        } else if (isOS390()) {
            return PlatformType.OS390;
        } else if (isOSF1()) {
            return PlatformType.OSF1;
        } else if (isSolaris()) {
            return PlatformType.Solaris;
        } else if (isSunOS()) {
            return PlatformType.SunOS;
        } else if (isWindows()) {
            return PlatformType.Windows;
        }
        return PlatformType.Others;
    }
}
