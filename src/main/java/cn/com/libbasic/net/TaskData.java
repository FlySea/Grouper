package cn.com.libbasic.net;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;

import cn.com.libbasic.bean.RootBean;
import cn.com.libbasic.util.LibStatus.RefreshType;

/**
 * 请求带的数据跟返回带的数据
 *
 */
public class TaskData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 请求必须有的字段
     */
    public ThreadCallBack callBack;//请求回调
    public int requestCode;// 请求唯一标识,跟requestAction可以只为一个
    public String requestAction;// 请求唯一标识
    public String url = null;//请求地址
    public int prio = BaseTask.TaskPrio.PRIO_DATA;

    /**
     * 参数,三个只选其一
     */
    public ArrayList<NameValuePair> paramList;
    public HashMap<String, String> paramMap;//请求参数
    public String paramStr = "";

    /**
     * 返回必须有的字段
     */
    public String resultData; // 接口返回值
    public int retStatus = ResStatus.Error_Exception; // 请求结果网络状态

    /**
     * 上传下载路径
     */
    public String filePath;
    public int status;//状态
    public int progress;//进度


    /**
     * 可选字段
     */
    public boolean showNetError = true;//表示在基类中是否弹出网络异常的toast
    public Type resultType;//返回json解析对象
    public RootBean rootBean;
    public int times = 3; // 重试次数
    public long expire = 0; // 缓存过期时间(毫秒)
    public boolean call = false; // 如果已从缓存中获取，是否还要从网络获取并返回
    public int refreshType = RefreshType.REFRESH;// 请求刷新类型
    public boolean showErrorView;//是否显示网络异常View
    public boolean showEmptyView;//是否显示空 View
    public boolean post = false;//是否为post请求,用于UrlTask

}
