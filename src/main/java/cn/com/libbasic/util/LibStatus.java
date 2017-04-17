package cn.com.libbasic.util;


public class LibStatus {

	/**
	 * 加载显示类型
	 * 
	 */
	public static final class LoadingType {
		public static final int LOAD_EMPTY = 0;// 不显示
		public static final int LOAD_VIEW = 1;// 显示加载view
		public static final int LOAD_DIALOG = 2;// 显示加载dialog
	}

	/**
	 * 刷新类型
	 * 
	 */
	public static final class RefreshType {
		public static final int REFRESH = 0;// 刷新第一页
		public static final int LOAD_MORE = 1;// 加载下一页
		public static final int REFRES_HALL = 2;// 刷新所有页面
		public static final int REFRESH_PAGE = 3;// 刷新某一页
	}

	/**
	 * 网络请求的类型
	 */
	public static final class TaskType {
		public static final int URL = 1;
		public static final int FileUP = 2;
		public static final int FileDown = 3;
		public static final int HttpClient = 4;
		public static final int XingHuo = 5;
	}
	
	/**
	 * 文件上传下载状态
	 */
	public static final class FileStatus{

	    public static final int IDLE = 1;// 空闲，表示线程已生成，但没有执行,目前没有用
	    public static final int START = 2;// 下载,上传中
	    public static final int COMPLETE = 3;// 下载,上传完成
	    public static final int DONE_RESULT = 4;// 上传完成并返回结果
	    public static final int FIAL = 5;// 下载,上传失败

	}

}
