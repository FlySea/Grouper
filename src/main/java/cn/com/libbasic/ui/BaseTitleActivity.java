package cn.com.libbasic.ui;

import android.view.View;

import cn.com.libUI.view.TitleBar;
import cn.com.libUI.view.TitleBar.OnTitleItemActionListener;
import cn.com.libbasic.R;

/**
 * 带标题
 *
 */
public class BaseTitleActivity extends BaseActivity implements OnTitleItemActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected TitleBar mTitleBar;

    protected void initTitle() {
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mTitleBar.setOnTitleItemActionListener(this);
    }

    @Override
    public void onTitleItemAction(View v, String action, String clickId, String content) {
        switch (clickId) {
            case TitleBar.ID_BACK:
                clickBack();
                break;
            case TitleBar.ID_SHARE:
                clickShare();
                break;
            case TitleBar.ID_MESSAGE:
                clickMessage();
                break;
            case TitleBar.ID_SEARCH:
                clickSearch();
                break;
            default:
        }
    }

    /**
     * 返回事件
     */
    protected void clickBack() {
        finish();
    }

    /**
     * 搜索事件
     */
    protected void clickSearch() {

    }

    /**
     * 分享事件
     */
    protected void clickShare() {

    }

    /**
     * 消息事件,需要重写再写
     */
    protected void clickMessage() {

    }

}
