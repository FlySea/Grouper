package cn.com.libbasic.util;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import cn.com.libbasic.BasicApplication;

/**
 * 获取系统信息的工具类
 * 
 */
public class SystemInfoUtil {

    /**
     * 收起软键盘
     * @param act
     */
    public static void closeSoftKeyBoard(Activity act) {
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(act.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * 判断网络是否可用
     * 
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
     * 
     * @param context
     * @return
     */
    public static String getNetWorkType(Context context) {
        String netType = "unknown";
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null) {
                int nType = networkInfo.getType();
                if (nType == ConnectivityManager.TYPE_MOBILE) {
                    netType = networkInfo.getExtraInfo().toLowerCase();
                } else if (nType == ConnectivityManager.TYPE_WIFI) {
                    netType = "wifi";
                }
            }
        } catch (Exception e) {
        }
        return netType;
    }

    /**
     * 判断当前应用是否是在最前运行
     * 
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(BasicApplication.mCon.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前运行的activity
     * 
     * @param context
     * @return
     */
    public static boolean getForegroundActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(BasicApplication.mCon.getPackageName())) {

        }
        return false;
    }

    /**
     * 得到当前的手机网络类型
     * 
     * @param context
     * @return
     */
    public static String getCurrentNetType(Context context) {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "null";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4g";
            }
        }
        return type;
    }

    /**
     * 获取手机ip地址
     * 
     * @return
     */
    public static String getNetIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取mac地址
     * 
     * @return
     */
    public static String getMacAddress() {
        WifiManager wifi = (WifiManager) BasicApplication.mCon.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
            return info.getMacAddress();
        }
        return "";
    }

    public static String getScreenSize(Context context) {
        String defaultSize = "800_480";
        try {
            Display d = ((Activity) context).getWindowManager().getDefaultDisplay();
            defaultSize = d.getHeight() + "_" + d.getWidth();
        } catch (Exception e) {
        }
        return defaultSize;
    }

    public static Rect getDefaultImageBounds(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = (int) (width * 9 / 16);

        Rect bounds = new Rect(0, 0, width, height);
        return bounds;
    }

    /**
     * 判断是否存在sd卡
     * 
     * @return
     */
    public static boolean avaiableSdcard() {
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取sd卡路径
     * 
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();

    }

    /**
     * 获取当前应用的版本名称
     * 
     * @return
     */
    public static String getVersionName() {
        String version = "";
        try {
            PackageManager packageManager = BasicApplication.mCon.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(BasicApplication.mCon.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取当前应用的版本号
     * 
     * @return
     */
    public static int getVersionCode() {
        int version = 1;
        try {
            PackageManager packageManager = BasicApplication.mCon.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(BasicApplication.mCon.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getApplicationMeta(Activity act,String name) {
        ApplicationInfo appInfo = null;
        String value = "";
        try {
            appInfo = act.getPackageManager().getApplicationInfo(act.getPackageName(), PackageManager.GET_META_DATA);
            Object obj = appInfo.metaData.get(name);
            value = obj.toString();
//            if(String.class.isInstance(obj)){
//            }else if(Integer.class.isInstance(obj)){
//            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        LogUtil.d("cheng", "name="+name+";value="+value);
        return value;
    }

}
