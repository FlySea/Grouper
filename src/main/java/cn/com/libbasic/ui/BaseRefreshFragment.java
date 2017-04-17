package cn.com.libbasic.ui;

import java.util.ArrayList;

import android.graphics.Rect;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import cn.com.libbasic.R;
import cn.com.libbasic.net.ResStatus;
import cn.com.libbasic.net.TaskData;
import cn.com.libbasic.ui.refresh.PullToRefreshLayout;
import cn.com.libbasic.ui.refresh.PullToRefreshLayout.OnRefreshListener;
import cn.com.libbasic.util.LibStatus;
import cn.com.libbasic.util.LibStatus.RefreshType;
import cn.com.libbasic.util.LogUtil;
import cn.com.libbasic.util.SystemInfoUtil;

/**
 * 刷新加载
 *
 */
public  class BaseRefreshFragment extends BaseTitleFragment implements OnRefreshListener {

    public static final String TAG = "BaseRefreshFragment";
    private static final long serialVersionUID = 1L;
    private int mListHeight = -1;//EmptyView,ErrorView应该占用的高度
    private int minListEmptyHeight = 0;

    //子类MSG id以MSG_REFRESH_BEGIN开始增加，本类以MSG_REFRESH_BEGIN递减
    public static final int MSG_REFRESH_BEGIN = 20;
    public static final int MSG_GET_DATA = MSG_REQUEST_DATA + 1;


    //子类TASK id以TASK_REFRESH_BEGIN开始增加，本类以TASK_REFRESH_BEGIN递减
    public static final int TASK_REFRESH_BEGIN = 20;


    protected int RAGE_LENGTH = 20;
    protected int mCurrentPage = 0;// 当前页面
    protected int mPageNo = 0;// 要请求的页面
    protected int mUpdtePage = 0; // 指定更新某一页
    protected int mPageSize = RAGE_LENGTH;// 每页显示多少条数据
    protected int mTotalPage;// 数据总页数
    protected boolean mHasMore = true;// 是否还有数据

    protected int mRefreshType = RefreshType.REFRESH;// 当前页面加载类型,每次刷新时在进行设置

    protected View mHeaderView;
    protected PullToRefreshLayout mPtrl;
    protected ListView mListView;
    protected BaseRefreshAdapter mAdapter;
    @SuppressWarnings("rawtypes")
    protected ArrayList mList = new ArrayList();

    public BaseRefreshFragment() {
        super();
    }

    public void initRefreshView() {
        mPtrl = (PullToRefreshLayout) mViewGroup.findViewById(R.id.refresh_view);
        mListView = (ListView) mViewGroup.findViewById(R.id.listview);
        mPtrl.setOnRefreshListener(this);
        mPtrl.mCanPull = true;
        mListView.setFooterDividersEnabled(false);
        minListEmptyHeight = (int)getResources().getDimension(R.dimen.list_empty_min_height);
    }

    @Override
    public void handleMessageSecond(Message msg) {
        switch (msg.what) {
            case MSG_GET_DATA:
                if (mList == null || mList.size() == 0) {
                    mPageNo = 0;
                    requestData(RefreshType.REFRESH);
                    pull();
                    removeFooterViews();
                    if(mEmptyView!=null){
                        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
                        calculateHeight();
                        layoutParams.height= mListHeight;
                        mEmptyView.setLayoutParams(layoutParams);
                        mListView.addFooterView(mEmptyView);
                    }
                }
                break;
        }
        super.handleMessageSecond(msg);
    }

    /**
     * 计算EmptyView,ErrorView应该占用的高度
     */
    private void calculateHeight(){
        if(mListHeight == -1){
            Rect rectL = new Rect();
            Rect rectH = new Rect();
            Rect rectE = new Rect();
            mListView.getLocalVisibleRect(rectL);
            mEmptyView.getLocalVisibleRect(rectE);
            if (mHeaderView != null) {
                mHeaderView.getLocalVisibleRect(rectH);
            }
            mListHeight = rectL.height() - rectH.height();
            if (mListHeight < rectE.height()) {
                mListHeight = rectE.height();
            }
            if(mListHeight<minListEmptyHeight){
                mListHeight = minListEmptyHeight;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPtrl != null) {
            mPtrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getData() {
        mBaseHandler.sendEmptyMessageDelayed(MSG_GET_DATA, DELAY_TIME);
    }

    public void pull() {

    }

    public void requestData(int refreshType){
        mRefreshType = refreshType;
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (SystemInfoUtil.isNetworkConnected(mContext)) {
            mPageNo = 0;
            requestData(RefreshType.REFRESH);
            pull();
        } else {
            mPtrl.refreshFinish(PullToRefreshLayout.FAIL);
            showToast(R.string.network_error);
        }

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (SystemInfoUtil.isNetworkConnected(mContext)) {
            requestData(RefreshType.LOAD_MORE);
        } else {
            if (!SystemInfoUtil.isNetworkConnected(mContext)) {
                showToast(R.string.network_error);
                mPtrl.loadmoreFinish(PullToRefreshLayout.FAIL);
            } else {
                mPtrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }

    public void onRefreshFinish(TaskData taskData) {
        if (taskData.refreshType == RefreshType.LOAD_MORE) {
            if (taskData.retStatus == ResStatus.Success) {
                mPtrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            } else {
                mPtrl.loadmoreFinish(PullToRefreshLayout.FAIL);
            }
        } else {
            if (taskData.retStatus == ResStatus.Success) {
                mPtrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            } else {
                mPtrl.refreshFinish(PullToRefreshLayout.FAIL);
            }
        }
    }

    /**
     * 填充数据
     *
     * @param list
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setData(ArrayList list, TaskData taskData) {
        LogUtil.d(TAG, "---setData---mCurrentPage=" + mCurrentPage + ";mTotalPage=" + mTotalPage + ";status=" + taskData.retStatus + ";mRefreshType=" + mRefreshType + ";taskData.refreshType=" + taskData.refreshType);
        if (taskData.refreshType != mRefreshType) {
            return;
        }
        if (taskData.retStatus == ResStatus.Success) {
            if (list != null && list.size() > 0) {
                switch (taskData.refreshType) {
                    case RefreshType.REFRESH:
                        mList.clear();
                        mList.addAll(list);
                        mCurrentPage = mPageNo = 1;
                        mHasMore = true;
                        break;
                    case RefreshType.LOAD_MORE:
                        mList.addAll(list);
                        mPageNo = mCurrentPage++;
                        mHasMore = true;
                        break;
                    case RefreshType.REFRESH_PAGE:
                        if (mList != null && mList.size() > 0 && mUpdtePage >= 1) {
                            mList.addAll((mUpdtePage - 1) * mPageSize, list);
                        }
                        mUpdtePage = 0;
                        break;
                    case RefreshType.REFRES_HALL:
                        mList.clear();
                        mList.addAll(list);
                        break;
                }
            }
        }
        View view = getEmptyErrorView(taskData.retStatus != ResStatus.Success);
        if (mList == null || mList.size() <= 0) {
            removeFooterViews();
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
            calculateHeight();
            layoutParams.height= mListHeight;
            if (view != null) {
                view.setLayoutParams(layoutParams);
                mListView.addFooterView(view);
            }
            if (mListNullView != null) {
                if (taskData.retStatus != ResStatus.Success) {
                    mListNullView.setTip(R.string.network_error);
                } else {
                    mListNullView.setTip(R.string.no_data);
                }
            }
        } else {
            removeFooterViews();
        }
        mAdapter.list = mList;
        LogUtil.d(TAG, "size=" + mList.size());

        if (taskData.retStatus != ResStatus.Success && mList != null && mList.size() > 0 && (list == null || list.size() <= 0)) {
            showToast(R.string.network_error);
        }
        if (taskData.retStatus == ResStatus.Success && mList != null && mList.size() > 0 && (list == null || list.size() <= 0)) {
            showToast(R.string.no_more_data);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void removeFooterViews(){
        if(mErrorView!=null){
            mListView.removeFooterView(mErrorView);
        }
        if(mEmptyView!=null){
            mListView.removeFooterView(mEmptyView);
        }
    }
}
