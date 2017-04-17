package cn.com.libbasic;

import java.io.File;
import java.lang.ref.WeakReference;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import cn.com.libbase.base.BaseApplication;
import cn.com.libbasic.util.FilePathManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;

public abstract class BasicApplication extends BaseApplication {

    public static Context mCon;

    public static ImageLoader imageLoader;// 第三方下载
    public static DisplayImageOptions options;

    private static WeakReference<BasicApplication> mInstance = null;

    public static BasicApplication getInstance() {
        return ((null != mInstance) ? mInstance.get() : null);
    }

    public BasicApplication() {
        super();
        mInstance = new WeakReference<BasicApplication>(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCon = this.getApplicationContext();
        MobclickAgent.setDebugMode(true);

        // 创建默认的ImageLoader配置参数
        File cacheDir = new File(FilePathManager.getCanClearImg());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mCon).diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100).diskCache(new UnlimitedDiskCache(cacheDir)).diskCacheFileNameGenerator(new Md5FileNameGenerator()).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

}
