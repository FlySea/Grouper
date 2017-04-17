package cn.com.libbasic.bean;

import cn.com.libbase.LibConfigs;

/**
 * 请求基类
 * 
 */
public class RootBean {
	//请求
	protected AppReqData appReqData;
	protected String token;
	protected String sign;
	protected String appid;
	protected String version = LibConfigs.SERVERVERSION_INVEST;
	//响应
	protected String result;
	protected String errMsg;
	protected String succMsg;
	protected String systemCode;
	protected AppResData appResData;

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public AppReqData getAppReqData() {
		return appReqData;
	}

	public void setAppReqData(AppReqData appReqData) {
		this.appReqData = appReqData;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public AppResData getAppResData() {
		return appResData;
	}

	public void setAppResData(AppResData appResData) {
		this.appResData = appResData;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String id) {
		appid = id;
	}

	public boolean isValidResult() {
		return result.equals("SUCCESS");
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSuccMsg() {
		return succMsg;
	}

	public void setSuccMsg(String succMsg) {
		this.succMsg = succMsg;
	}

	@Override
	public String toString() {
		return "RootBean [appReqData=" + appReqData + ", token=" + token
				+ ", sign=" + sign + ", appid=" + appid + ", result=" + result
				+ ", errMsg=" + errMsg + ", appResData=" + appResData + "]";
	}
	
	


}
