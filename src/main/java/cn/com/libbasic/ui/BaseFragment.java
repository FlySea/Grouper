package cn.com.libbasic.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.com.libUI.view.LoadingDialog;
import cn.com.libbasic.LibConfigs;
import cn.com.libbasic.R;
import cn.com.libbasic.net.BaseTask;
import cn.com.libbasic.net.DownloadTask;
import cn.com.libbasic.net.HttpClientTask;
import cn.com.libbasic.net.ResStatus;
import cn.com.libbasic.net.TaskData;
import cn.com.libbasic.net.ThreadCallBack;
import cn.com.libbasic.net.ThreadScheduler;
import cn.com.libbasic.net.URLTask;
import cn.com.libbasic.net.UploadTask;
import cn.com.libbasic.net.XingHuoTask;
import cn.com.libbasic.util.LibStatus.LoadingType;
import cn.com.libbasic.util.LibStatus.TaskType;
import cn.com.libbasic.util.LogUtil;
import cn.com.libbasic.util.StringUtil;
import cn.com.libbasic.util.SystemInfoUtil;

public class BaseFragment extends Fragment implements ThreadCallBack, OnClickListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected static final String TAG = "BaseFragment";

    protected Activity mBaseActivity;
    protected Context mContext;
    protected ViewGroup mViewGroup;
    protected View mEmptyView, mErrorView;
    protected LoadingDialog mLoadingDialog;
    protected ListNullView mListNullView;

    protected List<BaseTask> requestList = null;
    protected Gson mGson = new Gson();

    public static final int DELAY_TIME = 200;

    //子类MSG id以MSG_BEGIN开始增加，本类以MSG_BEGIN递减
    public static final int MSG_BEGIN = 10;
    public static final int MSG_REQUEST_DATA = MSG_BEGIN - 1;

    //子类TASK id以TASK_BEGIN开始增加，本类以TASK_BEGIN递减
    public static final int TASK_BEGIN = 10;

    protected boolean isStart = false;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestList = new ArrayList<BaseTask>();
        mBaseActivity = getActivity();
        mContext = mBaseActivity.getBaseContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mViewGroup != null) {
            if (mViewGroup.getParent() != null) {
                ((ViewGroup) mViewGroup.getParent()).removeView(mViewGroup);
            }
        }
        return mViewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();
        isStart = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isStart = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public Handler mBaseHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_REQUEST_DATA:
                    requestData();
                    break;
            }
            handleMessageSecond(msg);
        }
    };

    public void handleMessageSecond(android.os.Message msg) {

    }

    public boolean checkNetwork(boolean showToast) {
        boolean network = SystemInfoUtil.isNetworkConnected(mContext);
        if (showToast && !network) {
            if (isAdded()) {
                showToast(this.getString(R.string.network_error));
            } else {
                LogUtil.e(TAG, " checkNetwork fragment not added");
            }
        }
        return network;
    }

    Toast toast = null;

    protected void showToast(int resId) {
        if (isAdded()) {
            showToast(getString(resId));
        } else {
            LogUtil.e(TAG, " 1 showToast fragment not added");
        }
    }

    protected void showToast(String message) {
        if (toast != null) {
            return;
        }
        if (!isAdded()) {
            LogUtil.e(TAG, " 2 showToast fragment not added");
        } else {
            return;
        }
        toast = Toast.makeText(this.mContext, (!StringUtil.isEmpty(message)) ? message : this.getString(R.string.network_error), Toast.LENGTH_SHORT);
        toast.show();
        mBaseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
            }
        }, 2000);
    }

    protected void showLoadingDialog() {
        showLoadingDialog(getActivity(), null);
    }

    protected void showLoadingDialog(Context context, String text) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(context, text);
        }
        if (!isAdded()) {
            LogUtil.e(TAG, "  showLoadingDialog fragment not added");
            return;
        }
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    protected void initEmptyView() {

    }

    protected void initErrorView() {

    }

    /**
     * 获取空跟网络异常的view,只有没有数据的时候才会显示
     *
     * @param error true是网络异常,false是空
     * @return
     */
    protected View getEmptyErrorView(boolean error) {
        if (error) {
            if (mErrorView != null) {
                return mErrorView;
            }
        } else {
            if (mEmptyView != null) {
                return mEmptyView;
            }
        }
        return null;
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Exception e) {
            if (null != e) {
                e.printStackTrace();
            }
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.startActivityIfNeeded(intent, -1);
            }
        }

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            if (null != e) {
                e.printStackTrace();
            }
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.startActivityIfNeeded(intent, requestCode);
            }
        }
    }

    public void release() {
        if (mViewGroup != null) {
            if (mViewGroup.getParent() != null) {
                ((ViewGroup) mViewGroup.getParent()).removeView(mViewGroup);
            }
            mViewGroup.removeAllViews();
            mViewGroup = null;
        }
    }

    /**
     * 加载数据
     *
     * @param taskType     任务类型
     * @param taskData     请求任务
     * @param loadingType  显示加载框类型,默认不显示
     */
    public void startRequestTask(int taskType, TaskData taskData, int loadingType) {
        if (taskData == null) {
            return;
        }
        taskData.callBack = this;
        if (!checkNetwork(taskData.showNetError) && taskData.expire == 0) {
            taskData.retStatus = ResStatus.Error_Exception;
            onResultCallBack(taskData);
            return;
        }
        if (loadingType == LoadingType.LOAD_DIALOG) {
            showLoadingDialog();
        }
        BaseTask task = null;
        if (TaskType.URL == taskType) {
            task = new URLTask(taskData);
        }else if (TaskType.FileDown == taskType) {
            task = new DownloadTask(taskData);
        }else if (TaskType.FileUP == taskType) {
            task = new UploadTask(taskData);
        }else if (TaskType.XingHuo == taskType) {
            task = new XingHuoTask(taskData);
        }else if (TaskType.HttpClient == taskType) {
            task = new HttpClientTask(taskData);
        }
        if (task != null) {
            ThreadScheduler.getInstance().addTask(task);
        }
    }

    /**
     * 加载数据，网络不好点击时的请求接口
     *
     */
    public void requestData() {

    }

    ;

    /**
     * 数据返回接口
     *
     * @param taskData
     */
    public void onResultCallBack(TaskData taskData) {

    }

    @Override
    public void onResult(TaskData taskData) {
        if (isStart) {
            dismissLoadingDialog();
            //token过期
            if(taskData.rootBean!=null&&!taskData.rootBean.isValidResult()&& LibConfigs.TIMEOUT.equals(taskData.rootBean.getSystemCode())){
                //
                return;
            }
            if(taskData.rootBean!=null && taskData.rootBean.isValidResult()&& StringUtil.isEmpty(taskData.rootBean.getErrMsg())){
                showToast(taskData.rootBean.getErrMsg());
            }
            if(taskData.showNetError && taskData.retStatus != ResStatus.Success){
                showToast(R.string.network_error);
            }
            onResultCallBack(taskData);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
