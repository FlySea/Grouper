package cn.com.libbasic.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

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

import cn.com.libbasic.BasicApplication;
import cn.com.libbasic.util.LogUtil;

public class HttpClientTask extends StringTask{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpUriRequest mUriRequest = null;


	public HttpClientTask(TaskData taskData){
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
				mTaskData.retStatus = ResStatus.Success;
				mTaskData.resultData = NetUtil.getContent(response, "UTF-8");
				LogUtil.i(TAG, "result->"+mTaskData.resultData);
				return true;
			} else {
				mTaskData.retStatus = ResStatus.Error_Exception;
			}
		} catch (HttpHostConnectException e){
			mTaskData.retStatus = ResStatus.Error_Exception;
			e.printStackTrace();
		} catch (UnsupportedEncodingException e){
			mTaskData.retStatus = ResStatus.Error_Exception;
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			mTaskData.retStatus = ResStatus.Error_Exception;
			e.printStackTrace();
		} catch (SocketTimeoutException e){
			mTaskData.retStatus = ResStatus.Error_Exception;
			e.printStackTrace();
		} catch (ConnectTimeoutException e){
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
				LogUtil.d(TAG, "getHttpRequestByApi-"+e);
			}
		}
		return post;
	}

}
