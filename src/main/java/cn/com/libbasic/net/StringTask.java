package cn.com.libbasic.net;

import java.util.Iterator;
import java.util.Map;

import org.apache.http.NameValuePair;

import cn.com.libbasic.util.LogUtil;
import cn.com.libbasic.util.StringUtil;

/**
 * 字符串数据请求基类
 *
 */
public abstract class StringTask extends BaseTask {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getParames() {
        String param = mTaskData.paramStr;
        if (mTaskData.paramMap != null && !mTaskData.paramMap.isEmpty()) {
            Iterator iter = mTaskData.paramMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                param += key + "=" + val + "&";
            }
        }
        if (mTaskData.paramList != null && mTaskData.paramList.size() > 0) {
            for (NameValuePair pair : mTaskData.paramList) {
                param += pair.getName() + "=" + pair.getValue() + "&";
            }
        }
        if (param != null && param.length() > 0) {
            char ch = param.charAt(param.length() - 1);
            if ("&".equals("" + ch)) {
                param = param.replace(param.charAt(param.length() - 1) + "", "");
            }
        }
        return param;
    }

    @Override
    String getMD5() {
        String urlStr = "";
        if (mTaskData.url.contains("?")) {
            urlStr = mTaskData.url + "&" + getParames();
        } else {
            urlStr = mTaskData.url + "?" + getParames();
        }
        return StringUtil.getMD5(urlStr);
    }

    public void doJob() {
        if (mTaskData.expire > 0) {
            mTaskData.resultData = HttpCache.getInstance().getCacheJsonByUrl(getMD5(), mTaskData.expire);
        }
        // 如果缓存有有效数据，先向上返回，如果需要再重新获取，那获取后了再向上返；如果没有有效数据，重新获取，再向上返；
        if (!StringUtil.isEmpty(mTaskData.resultData)) {
            mTaskData.retStatus = ResStatus.Success;
            sendData();
            if (mTaskData.call) {
                getData();
                sendData();
            }
        } else {
            getData();
            sendData();
        }
    }

    public void getData() {
        int count = 0;
        while (count < mTaskData.times) {
            LogUtil.i(TAG, "begin count: " + count + ";url:" + mTaskData.url + "\n" + "params:" + getParames());
            if (process()) {
                break;
            }
            count++;
        }
        if (mTaskData.expire > 0 && mTaskData.retStatus == ResStatus.Success) {
            HttpCache.getInstance().addCacheJson(getMD5(), mTaskData.resultData);
        }
    }
}
