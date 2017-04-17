package cn.com.libbasic.net;

import java.io.Serializable;

/**
 * 回调
 *
 */
public interface ThreadCallBack extends Serializable {

    /**
     * 回调
     *
     * @param taskData
     */
    public void onResult(TaskData taskData);


}
