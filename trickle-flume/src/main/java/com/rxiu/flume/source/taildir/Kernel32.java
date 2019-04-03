package com.rxiu.flume.source.taildir;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinBase.FILETIME;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

import java.util.*;

/** * Created by yangyibo on 17/3/14. */
public interface Kernel32 extends StdCallLibrary {
    final static Map WIN32API_OPTIONS = new HashMap() {
        private static final long serialVersionUID = 1L;

        {
            put(Library.OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
            put(Library.OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
        }
    };

    Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("Kernel32",
            Kernel32.class, WIN32API_OPTIONS);

    int GetLastError();

    class BY_HANDLE_FILE_INFORMATION extends Structure {
        public DWORD dwFileAttributes;
        public FILETIME ftCreationTime;
        public FILETIME ftLastAccessTime;
        public FILETIME ftLastWriteTime;
        public DWORD dwVolumeSerialNumber;
        public DWORD nFileSizeHigh;
        public DWORD nFileSizeLow;
        public DWORD nNumberOfLinks;
        public DWORD nFileIndexHigh;
        public DWORD nFileIndexLow;

        public static class ByReference extends BY_HANDLE_FILE_INFORMATION implements Structure.ByReference {

        }

        ;

        public static class ByValue extends BY_HANDLE_FILE_INFORMATION implements Structure.ByValue {

        }

        @Override
        protected List getFieldOrder() {
            List fields = new ArrayList();
            fields.addAll(Arrays.asList(new String[]{"dwFileAttributes",
                    "ftCreationTime", "ftLastAccessTime", "ftLastWriteTime",
                    "dwVolumeSerialNumber", "nFileSizeHigh", "nFileSizeLow",
                    "nNumberOfLinks", "nFileIndexHigh", "nFileIndexLow"}));
            return fields;

        }

        ;
    }

    ;

    boolean GetFileInformationByHandle(HANDLE hFile,
                                       BY_HANDLE_FILE_INFORMATION lpFileInformation);
}