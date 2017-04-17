package cn.com.libbasic.util;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {
    static final String TAG = "Preferences";
    public static String Prefe_Live = "xinghuo_file";

    /**
     * 设置字符值
     * 
     * @param context
     * @param name
     * @param value
     */
    public static void saveSenyintString(Context context, String prefeName, String name, String value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putString(name, value).commit();
    }

    /**
     * 获取字符值
     * 
     * @param context
     * @param name
     * @param def
     * @return
     */
    public static String getSenyintString(Context context, String prefeName, String name, String def) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        return sp.getString(name, def);
    }

    /**
     * 设置long
     * 
     * @param context
     * @param name
     * @param value
     */
    public static void saveSenyintLong(Context context, String prefeName, String name, long value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putLong(name, value).commit();
    }

    /**
     * 获取long
     * 
     * @param context
     * @param name
     * @param def
     * @return
     */
    public static long getSenyintLong(Context context, String prefeName, String name, long def) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        return sp.getLong(name, def);
    }

    /**
     * 设置int值
     * 
     * @param context
     * @param name
     * @param value
     */
    public static void saveSenyintInt(Context context, String prefeName, String name, int value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putInt(name, value).commit();
    }

    /**
     * 设置int值,在之前的值上加
     * 
     * @param context
     * @param name
     * @param value
     */
    public static void saveSenyintIntAdd(Context context, String prefeName, String name, int value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putInt(name, value + getSenyintInt(context, prefeName, name, 0)).commit();
    }

    /**
     * 获取int值
     * 
     * @param context
     * @param name
     * @param def
     * @return
     */
    public static int getSenyintInt(Context context, String prefeName, String name, int def) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        return sp.getInt(name, def);
    }

    /**
     * 设置boolean值
     * 
     * @param context
     * @param name
     * @param value
     */
    public static void saveSenyintBoolean(Context context, String prefeName, String name, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(name, value).commit();
    }

    /**
     * 获取boolean值
     * 
     * @param context
     * @param name
     * @param def
     * @return
     */
    public static boolean getSenyintBoolean(Context context, String prefeName, String name, boolean def) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        return sp.getBoolean(name, def);
    }

    /**
     * 删除name
     * 
     * @param context
     * @param name
     * @param value
     */
    public static void removeSenyintName(Context context, String prefeName, String name) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().remove(name).commit();
    }

    /**
     * 清除数据
     * 
     * @param context
     * @param prefeName
     */
    public static void clearSenyint(Context context, String prefeName) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
    
    public static void clearUserData(Context context){
        String phone = getSenyintString(context, Prefe_Live, PrefeKey.Key_Phone, "");
        SharedPreferences sp = context.getSharedPreferences(Prefe_Live, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
        saveSenyintString(context, Prefe_Live, PrefeKey.Key_Phone, phone);
    }

    /**
     * 删除Prefe
     * 
     * @param context
     */
    public static void deletePrefe(Context context, String prefeName) {
        File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs", prefeName);
        LogUtil.d(TAG, "---deletePrefe=" + file.getAbsolutePath() + ";exists=" + file.exists() + ";prefeName=" + prefeName);
        if (file.exists()) {
            clearSenyint(context, prefeName.replace(".xml", ""));
            file.delete();
        }
    }

}
