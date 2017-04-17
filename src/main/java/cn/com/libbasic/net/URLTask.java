package cn.com.libbasic.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import cn.com.libbasic.LibConfigs;
import cn.com.libbasic.util.LogUtil;
import cn.com.libbasic.util.StringUtil;

/**
 * URL请求
 *
 */
public class URLTask extends StringTask {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public URLTask(TaskData taskData) {
        mTaskData = taskData;
    }

    @Override
    boolean process() {
        try {
            // 建立连接
            URL url = new URL(mTaskData.url);
            HttpURLConnection httpConn = null;
            if (url.getProtocol().toUpperCase().equals("HTTPS") && LibConfigs.DEBUG == true) {
                NetUtil.trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(NetUtil.DO_NOT_VERIFY);
                httpConn = https;
            } else {
                httpConn = (HttpURLConnection) url.openConnection();
            }
            if (mTaskData.post) {
                // 设置参数
                httpConn.setDoOutput(true); // 需要输出
                httpConn.setDoInput(true); // 需要输入
                httpConn.setUseCaches(false); // 不允许缓存
                httpConn.setRequestMethod("POST"); // 设置POST方式连接
                // 设置请求属性
                httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpConn.setRequestProperty("Charset", "UTF-8");
            }
            httpConn.connect();
            if (mTaskData.post) {
                // 建立输入流，向指向的URL传入参数
                DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
                dos.writeBytes(getParames());
                dos.flush();
                dos.close();
            }
            // 获得响应状态
            int resultCode = httpConn.getResponseCode();
            LogUtil.i(TAG, "resultCode=" + resultCode);
            if (HttpURLConnection.HTTP_OK == resultCode) {
                StringBuffer sb = new StringBuffer();
                String readLine = new String();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                while ((readLine = reader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                reader.close();
                mTaskData.resultData = StringUtil.convertUnicode(sb.toString());
                LogUtil.i(TAG, "result->" + mTaskData.resultData);
                mTaskData.retStatus = ResStatus.Success;
                return true;
            } else {
                mTaskData.retStatus = ResStatus.Error_Exception;
            }
        } catch (HttpHostConnectException e) {
            mTaskData.retStatus = ResStatus.Error_Exception;
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            mTaskData.retStatus = ResStatus.Error_Exception;
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            mTaskData.retStatus = ResStatus.Error_Exception;
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            mTaskData.retStatus = ResStatus.Error_Exception;
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            mTaskData.retStatus = ResStatus.Error_Exception;
            e.printStackTrace();
        } catch (IOException e) {
            mTaskData.retStatus = ResStatus.Error_Exception;
            e.printStackTrace();
        } catch (Exception e) {
            mTaskData.retStatus = ResStatus.Error_Exception;
            e.printStackTrace();
        }

        return false;
    }

}
