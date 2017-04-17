package cn.com.libbasic.net;

import android.content.Context;
import cn.com.libbasic.BasicApplication;
import cn.com.libbasic.R;

/**
 * 数据请求返回状态信息
 * 
 *
 */
public class ResStatus {

	public static final int Success = 1;//成功,一般由对应业务来判断是否给提示
	public static final int FAIL = 2;//业务出错,一般有服务器给出提示
	public static final int Error_Parse = 3;//数据解析出错
	public static final int Error_Exception = 4;//网络异常

	public static String getTipString(int status) {
		Context con = BasicApplication.mCon;
		String res = "";
		switch (status) {
		case Error_Parse:
			res = con.getString(R.string.data_error);
			break;
		case Error_Exception:
			res = con.getString(R.string.network_error);
			break;
		}
		return res;
	}
}
