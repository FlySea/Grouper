package cn.com.libbasic.net;

import java.io.Serializable;

import android.os.Handler;
import android.os.Message;

import cn.com.libbasic.util.LogUtil;

/**
 * 任务基类
 *
 */
public abstract class BaseTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * task优先级跟优先级数量
     */
    public static final class TaskPrio {
        public static final int PRIO_DATA = 1;
        public static final int PRIO_BITMAP = 2;
        public static final int PRIO_FILE = 3;
        public static final int PRIO_COUNT = 3;
    }

    public static String TAG = "BaseTask";

    protected int connectTimeout = 5000;
    protected int readTimeout = 10000;

    public TaskData mTaskData;

    static Handler resultHandler = new Handler() {
        public void handleMessage(Message msg) {
            TaskData call = (TaskData) msg.getData().getSerializable("taskDate");
            if (call != null && call.callBack != null) {
                call.callBack.onResult(call);
            }
        }
    };

    /**
     * 区别任务唯一标识
     *
     * @return
     */
    abstract String getMD5();

    public void sendData() {
        Message msg = new Message();
        msg.getData().putSerializable("taskDate", mTaskData);
        resultHandler.sendMessage(msg);
    }

    /**
     * 任务
     */
    abstract void doJob();

    /**
     * 请求数据，有次数，可以加入缓存
     */
    abstract void getData();

    /**
     * 任务具体执行内容
     *
     * @return
     */
    abstract boolean process();

}
