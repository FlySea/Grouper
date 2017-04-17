package cn.com.libbasic;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


/**
 * 设备相关信息
 */

public class DeviceInfo {

    /**
     * @return 返回HTTP发送请求的User Agent信息
     */
    public static String getUserAgent(Context context) {   // app标示，app版本，设备型号，系统版本，硬件厂商，设置发售终端
        return AppInfo.APP_PACKAGE_NAME + "/" + AppInfo.APP_VERSION_NAME + " |" + android.os.Build.MODEL + "," + android.os.Build.VERSION.RELEASE + "," + getDeviceId(context) + "," + android.os.Build.HARDWARE;
    }

    /**
     * @param act
     * @return 返回设备id
     */
    public static String getDeviceId(Context act) {
        String deviceId = "000000000000";
        try {
            TelephonyManager tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
        } catch (Exception e) {
        }
        return deviceId;
    }

    /**
     * @return 设备版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 设备名称
     *
     * @return
     */
    public static String getDeviceName() {
        String deviceName = android.os.Build.MODEL;
        if (deviceName == null) {
            return "";
        } else {
            return deviceName;
        }
    }

    /**
     * mac 地址
     *
     * @param act
     * @return
     */
    public static String getMacAddress(Context act) {
        if (act == null) {
            return "";
        }
        WifiManager wifi = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return "";
        }
        try {
            WifiInfo info = wifi.getConnectionInfo();
            if (info == null) {
                return "";
            }
            return info.getMacAddress();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 获得系统的api level
     *
     * @return
     */
    public static int getSysVersion() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        return currentapiVersion;
    }

    /**
     * 获取用户的ip
     *
     * @return
     */
    public static String getIp(Context context) {
        String ip = "127.0.0.1";
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        } else {
            ip = getLocalIpAddress();
        }
        return ip;
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * 获取GPRS 状态的ip
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "127.0.0.1";
    }


}
