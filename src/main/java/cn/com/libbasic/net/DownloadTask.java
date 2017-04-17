package cn.com.libbasic.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import cn.com.libbasic.LibConfigs;
import cn.com.libbasic.util.LibStatus.FileStatus;
import cn.com.libbasic.util.LogUtil;


/**
 * 下载文件
 *
 */
public class DownloadTask extends FileBaseTask {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DownloadTask(TaskData taskData) {
        mTaskData = taskData;
    }

    @SuppressWarnings("resource")
    @Override
    boolean process() {

        File file = new File(mTaskData.filePath);
        InputStream is = null;
        OutputStream os = null;
        long total = 0, count = 0;
        try {
            sendMessage(FileStatus.START, 0);
            // 构造URL
            URL url = new URL(mTaskData.url);
            HttpURLConnection con;
            if (url.getProtocol().toUpperCase().equals("HTTPS") && LibConfigs.DEBUG == true) {
                NetUtil.trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(NetUtil.DO_NOT_VERIFY);
                con = https;
            } else {
                con = (HttpURLConnection) url.openConnection();
            }

            con.setRequestProperty("Accept-Encoding", "identity");
            total = con.getContentLength();

            if (file.exists()) {
                if (file.length() == total) {
                    LogUtil.d(TAG, "---count=" + count + ";total=" + total);
                    if (count == total) {
                        sendMessage(FileStatus.COMPLETE, 0);
                        return true;
                    }
                }
            }
            String tmp = "tmp";
            File tempFile = new File(mTaskData.filePath + tmp);
            if (tempFile.exists() && tempFile.length() > 0) {
                tempFile.delete();
            }
            is = con.getInputStream();
            // 2K的数据缓冲
            byte[] bs = new byte[2 * 1024];
            int len = 0, temp = 0;
            os = new FileOutputStream(mTaskData.filePath + tmp);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                if (mCancel) {
                    return false;
                }
                os.write(bs, 0, len);
                count += len;
                temp += len;
                int pro = (int) (count * 100 / total);
                int rate = (int) (temp * 100 / total);
                if (rate >= 3) {
                    sendMessage(FileStatus.START, pro);
                    temp = 0;
                }
                if (count == total) {
                    sendMessage(FileStatus.START, 100);
                }
            }
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }

            if (count == total) {
                File fileTemp = new File(mTaskData.filePath + tmp);
                fileTemp.renameTo(new File(mTaskData.filePath));
                sendMessage(FileStatus.COMPLETE, 0);
                return true;
            } else {
                sendMessage(FileStatus.FIAL, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(FileStatus.FIAL, 0);
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(FileStatus.FIAL, 0);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;

    }


}
