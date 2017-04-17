package cn.com.libbasic.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import cn.com.libbasic.BasicApplication;
import cn.com.libbasic.LibConfigs;

public class NetUtil {
	
	public static final int TYPE_WAP = 1;
	public static final int TYPE_NET = 2;
	public static final int TYPE_UNKNOWN = 3;

	public static final String NET = "net";
	public final static String http = "http://";
	public final static String https = "https://";
	public static final String PROXY_IP = "10.0.0.172";

	public final static int DEFAULT_PROXY_PORT = 80;

	public final static int HTTP_OK_CODE = 202;
	
	
	public static boolean isHttps(final String s) {
		if (s == null) {
			return false;
		}
		return s.startsWith(https);
	}
	
	public static HttpClient getNewHttpClient(HttpParams params) {
		try {
			SSLSocketFactory sf = null;
			if (LibConfigs.DEBUG == true) {
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				sf = new SSLSocketFactoryEx(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			}

			// HttpParams params = new BasicHttpParams();
			// HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			// HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient(params);
		}
	}
	
	public static String getContent(HttpResponse response, String charSet) throws IOException {
		if (response == null)
			return null;
		boolean isGzip = false;
		final InputStream is = response.getEntity().getContent();
		StringBuffer sb = new StringBuffer();

		Header[] headers = response.getHeaders("Content-Encoding");
		if (headers != null && headers.length > 0) {
			final int size = headers.length;
			for (int i = 0; i < size; ++i) {
				final Header header = headers[i];
				if (header.getValue().toLowerCase().indexOf("gzip") > -1) {
					isGzip = true;
					break;
				}
			}
		}

		if (isGzip) {
			GZIPInputStream gis = new GZIPInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(gis, charSet));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			gis.close();
			is.close();
			return sb.toString();
		} else {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, charSet));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			return sb.toString();
		}
	}
	
	public static String buildParamListInHttpRequestUrlEncode(List<NameValuePair> params) {
		if (params == null || params.size() <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < params.size(); index++) {
			try {
				sb.append(URLEncoder.encode(params.get(index).getName(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sb.append("=");
			try {
				String v = params.get(index).getValue();
				if (null != v) {
					sb.append(URLEncoder.encode(v, "utf-8"));
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (index < params.size() - 1) {
				sb.append("&");
			}
		}
		return sb.toString();
	}

	public static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		// Android use X509 cert
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	public static boolean isWap() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) BasicApplication.mCon.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		if (info != null && info.getExtraInfo() != null) {
			return info.getExtraInfo().endsWith("wap");
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param context
	 * @param httpParams
	 */
	public static void fillProxy(final Context context, final HttpParams httpParams) {
		if (NetUtil.isWap()) {
			try {
				Uri uri = Uri.parse("content://telephony/carriers/preferapn");
				Cursor mCursor = null;
				try {
					mCursor = context.getContentResolver().query(uri, null, null, null, null);
					if (mCursor != null) {
						boolean b = mCursor.moveToNext();
						if (b) {
							String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
							{
								HttpHost proxy = new HttpHost(proxyStr, 80);
								httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
							}
						}
					}
				} finally {
					if (mCursor != null && !mCursor.isClosed()) {
						mCursor.close();
					}
				}
			} catch (Exception e) {
			}
		}
	}

}
