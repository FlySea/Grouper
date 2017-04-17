package cn.com.libbasic.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import cn.com.libbase.LibAppConst;
import cn.com.libbasic.BasicApplication;
import cn.com.libbasic.bean.AppResData;
import cn.com.libbasic.bean.AppResDataAdapter;
import cn.com.libbasic.bean.RootBean;
import cn.com.libbasic.util.LogUtil;

public class XingHuoTask extends StringTask {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    HttpUriRequest mUriRequest = null;


    public XingHuoTask(TaskData taskData) {
        this.mTaskData = taskData;
    }


    @Override
    boolean process() {
        try {
            DefaultHttpClient httpClient;
            HttpParams mHttpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion(mHttpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(mHttpParams, "utf-8");
            HttpConnectionParams.setConnectionTimeout(mHttpParams, connectTimeout);
            HttpConnectionParams.setSoTimeout(mHttpParams, readTimeout);
            HttpClientParams.setCookiePolicy(mHttpParams, CookiePolicy.BROWSER_COMPATIBILITY);
            NetUtil.fillProxy(BasicApplication.mCon, mHttpParams);

            mUriRequest = getHttpRequestByApi(null, mTaskData.paramList);

            if (NetUtil.isHttps(mTaskData.url)) {
                httpClient = (DefaultHttpClient) NetUtil.getNewHttpClient(mHttpParams);
            } else {
                httpClient = new DefaultHttpClient(mHttpParams);
            }
            HttpResponse response = httpClient.execute(mUriRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                mTaskData.resultData = NetUtil.getContent(response, "UTF-8");
                if (parseBean()) {
                    mTaskData.retStatus = ResStatus.Success;
                } else {
                    mTaskData.retStatus = ResStatus.Error_Exception;
                }
                LogUtil.i(TAG, "result->" + mTaskData.resultData);
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
        mTaskData.retStatus = ResStatus.Error_Exception;
        return false;
    }

    public HttpUriRequest getHttpRequestByApi(ArrayList<NameValuePair> urlSuffixData, ArrayList<NameValuePair> formData) {
        mTaskData.url += NetUtil.buildParamListInHttpRequestUrlEncode(urlSuffixData);
        HttpPost post = new HttpPost(mTaskData.url);
        if (formData != null && (!formData.isEmpty())) {
            try {
                post.setEntity(new UrlEncodedFormEntity(formData, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                LogUtil.d(TAG, "getHttpRequestByApi-" + e);
            }
        }
        return post;
    }

    private boolean parseBean() {
        try {
            RootBean rootBean = null;
            GsonBuilder builder = new GsonBuilder();
            if (mTaskData.resultType != null) {
                builder.registerTypeAdapter(AppResData.class, new AppResDataAdapter(mTaskData.resultType));
            }
            Gson gson = builder.create();
            rootBean = gson.fromJson(filterJsonp(mTaskData.resultData), RootBean.class);
            mTaskData.rootBean = rootBean;
            return true;
        } catch (JsonSyntaxException e) {
        }
        return false;
    }

    private static String filterJsonp(String message) {
        if (message != null && (message.trim().endsWith(")") || message.trim().endsWith(");"))) {
            int startIndex = message.indexOf("(");
            if (startIndex == -1) {
                return message;
            }
            int endIndex = message.lastIndexOf(")");
            if (endIndex == -1) {
                return message;
            }
            return message.substring(startIndex + 1, endIndex);
        }
        return message;
    }

}
