package cn.com.libbasic;

import android.content.Context;
import android.content.pm.PackageInfo;

import cn.com.libbase.base.BaseApplication;
import cn.com.libbase.utils.ProjectConfigUtil;

/**
 * App常量信息
 */

public class AppInfo {

    public final static String OS_NAME = "android";

    public final static int APP_VERSION_CODE;
    public final static String APP_VERSION_NAME;
    public final static String APP_PACKAGE_NAME;

    // update apk filename
    public final static String UPDATE_PACKAGE_FILENAME;
    // user agent
    public final static String USERAGENT;
    // project name
    public final static String PROJECT_NAME;
    // base share preferences
    public final static String SHARE_STORAGE_NAME;

    static {
        int versionCode = 1;
        String versionName = "1.0.0";
        String packageName = "";

        BasicApplication app = BasicApplication.getInstance();
        try {
            Context ctx = app.getBaseContext();
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            packageName = info.packageName;
            versionCode = info.versionCode;
            versionName = info.versionName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        APP_PACKAGE_NAME = packageName;
        APP_VERSION_CODE = versionCode;
        APP_VERSION_NAME = versionName;

        UPDATE_PACKAGE_FILENAME = String.format("%s_update.apk", APP_PACKAGE_NAME);

        USERAGENT = DeviceInfo.getUserAgent(BasicApplication.mCon);

        PROJECT_NAME = BasicApplication.getInstance().getProjectName();

        SHARE_STORAGE_NAME = PROJECT_NAME + "_storage";
    }

}
