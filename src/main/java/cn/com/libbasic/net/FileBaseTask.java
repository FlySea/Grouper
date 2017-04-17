package cn.com.libbasic.net;

import cn.com.libbasic.util.StringUtil;

/**
 * 上传下载基类
 *
 */
public abstract class FileBaseTask extends BaseTask {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    static final String TAG = "FileBaseTask";

    public boolean mCancel;

    @Override
    String getMD5() {
        return StringUtil.getMD5(mTaskData.filePath);
    }

    @Override
    void doJob() {
        process();
    }

    @Override
    void getData() {

    }

    public void sendMessage(int status, int progress) {
        mTaskData.status = status;
        mTaskData.progress = progress;
        sendData();
    }

}
