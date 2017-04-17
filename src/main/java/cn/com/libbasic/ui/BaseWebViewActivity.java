package cn.com.libbasic.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.com.libUI.view.TitleBar;
import cn.com.libbasic.net.TaskData;
import cn.com.libbasic.util.LogUtil;

/**
 * 网页
 *
 */
public class BaseWebViewActivity extends BaseTitleActivity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String TAG = "BaseWebViewActivity";

    public WebSettings webSettings = null;
    public WebView webView = null;
    public String mUrl = null;
    public boolean webViewTitle;// 是否要读取webview中的标题

    public void loadWebView(String baseUrl) {
        setWebViewAttribute();
        mUrl = baseUrl.toString();
        webView.loadUrl(baseUrl.toString());
    }

    // 设置webView相关
    @SuppressLint("SetJavaScriptEnabled")
    protected void setWebViewAttribute() {
        webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setVisibility(View.VISIBLE);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setWebViewClient(loadUrlWebViewClient);
        webView.setWebChromeClient(loadUrlWebViewChromeClient);

    }

    // webview监听
    private WebViewClient loadUrlWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.d(TAG, "---shouldOverrideUrlLoading---url=" + url);
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                return true;
            }
            boolean res = shouldOverrideUrl(view, url);
            if (res) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    public boolean shouldOverrideUrl(WebView view, String url) {
        return false;
    }

    // 滚动条监听
    private WebChromeClient loadUrlWebViewChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return true;
        }

        public void onReceivedTitle(WebView view, String title) {
            if (webViewTitle) {
                mTitleBar.show(true, title, TitleBar.TITLE_STYLE_NONE, false);
            }
        }

        ;

    };

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(TAG, "WebView  onResume");
        webView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e(TAG, "WebView  onPause");
        webView.pauseTimers();
        webView.onPause();
        try {
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isFinishing()) {
            webView.loadUrl("about:blank");
        }
    }

    @Override
    public void onDestroy() {
        LogUtil.e(TAG, "WebView  onDestroy");
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onResultCallBack(TaskData taskData) {

    }
}
