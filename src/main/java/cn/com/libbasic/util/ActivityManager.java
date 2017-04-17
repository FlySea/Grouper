package cn.com.libbasic.util;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 * activity的管理
 * 
 *
 */
public class ActivityManager {

    static final String TAG = "ActivityManager";
    public static boolean mIsFromLogin = false;// 是否要再调用自动登陆的标识，与欢迎页，登陆页区别，以防重复调用自动登陆接口，统计不准
    public static boolean isDoctor = false;// 是否为医务专区

    static ArrayList<Activity> mList = new ArrayList<Activity>();// 主要用于存放没有destory的activity
    static ArrayList<Activity> mPauseList = new ArrayList<Activity>();// 存放最前面的activity,用于表示应用是否处理前后台
    static final int Msg_Check = 1;
    static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case Msg_Check:
                if (mPauseList.size() <= 0) {// 表示应用切换到后台
                }
                break;
            }
        }
    };

    public static void putActivity(Activity act) {
        mList.add(act);
    }

    public static void removeActivity(Activity act) {
        mList.remove(act);
    }

    public static void putPauseActivity(Activity act) {
        if (mPauseList.size() <= 0) {// 表示第一次进入,或者切换到前台

            long time = System.currentTimeMillis();
            if (!mIsFromLogin) {
                // 应用从后台切回前台时，调用自动登录，向后台签到
            }
//            PreferencesUtil
//                    .saveSenyintLong(KillApplication.mCon, PreferencesUtil.Prefe_Kill, PreferencesUtil.KEY_FIRST_ENTER, time);
        }
        mPauseList.add(act);
    }

    public static void removePauseActivity(Activity act) {
        int size = mPauseList.size();
        LogUtil.d(TAG, "----------removePauseActivity------size=" + size);
        mPauseList.remove(act);
        if (mPauseList.size() <= 0 && size > 0) {// 表示不存在前台页面
//            long first = PreferencesUtil.getSenyintLong(KillApplication.mCon, PreferencesUtil.Prefe_Kill,
//                    PreferencesUtil.KEY_FIRST_ENTER, (long)0);
//            String lastTime = PreferencesUtil.getSenyintString(KillApplication.mCon, PreferencesUtil.Prefe_Kill,
//                    PreferencesUtil.KEY_RUN_TIME, "");
//            if (StringUtil.isEmpty(lastTime)) {
//                lastTime = first + "," + System.currentTimeMillis();
//            } else {
//                lastTime = lastTime + ";" + first + "," + System.currentTimeMillis();
//            }
//            PreferencesUtil.saveSenyintString(KillApplication.mCon, PreferencesUtil.Prefe_Kill, PreferencesUtil.KEY_RUN_TIME,
//                    lastTime);
        }
        mHandler.sendEmptyMessageDelayed(Msg_Check, 1500);
    }

    /**
     * 关闭当前所有的activity，用于强制升级时关闭
     */
    public static void finishAll() {
        if (mList.size() > 0) {
            for (Activity act : mList) {
                act.finish();
            }
        }
    }
}
