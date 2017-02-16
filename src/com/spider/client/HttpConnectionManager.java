package com.spider.client;

import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.Cookie;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * http连接管理类
 * @author hxuhao
 *
 */
@Component
public class HttpConnectionManager {
	
	private static final int TIME_OUT = 10 * 1000;

	private final static Object syncLock = new Object();
	
	private static CloseableHttpClient httpClient = null;
	
	private static void config(HttpRequestBase httpRequestBase){
		 // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(TIME_OUT)
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).build();
        httpRequestBase.setConfig(requestConfig);
	}
	
	// 自定义的cookie策略 忽视cooki检查
	private static CookieSpecProvider easySpecProvider = new CookieSpecProvider() {  
		  
	    public CookieSpec create(HttpContext context) {  
	  
	        return new BrowserCompatSpec() {  
	            public void validate(Cookie cookie, CookieOrigin origin)  
	                    throws MalformedCookieException {  
	                // Oh, I am easy  
	            }  
	        };  
	    }  
	  
	};  


	
	
	// 获取HttpClient
	public static CloseableHttpClient getHttpClient(String url) {
        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
        
        // 双重确认
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
            	  httpClient = createHttpClient(200, 40, 100, hostname, port);
                }
            }
        }
        return httpClient;
	    
	}
	
	// 创建httpClient
    public static CloseableHttpClient createHttpClient(int maxTotal,
            int maxPerRoute, int maxRoute, String hostname, int port) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                registry);
        
        Registry<CookieSpecProvider> reg = RegistryBuilder.<CookieSpecProvider>create()  
                .register(CookieSpecs.BEST_MATCH,  
                    new BestMatchSpecFactory())  
                .register(CookieSpecs.BROWSER_COMPATIBILITY,  
                    new BrowserCompatSpecFactory())  
                .register("mySpec", easySpecProvider)  
                .build();  
          
        RequestConfig requestConfig = RequestConfig.custom()  
                .setCookieSpec("mySpec")  
                .build();  
        
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                    int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        
        CloseableHttpClient httpClient = HttpClients.custom()
        		.setDefaultCookieSpecRegistry(reg)
        		.setDefaultRequestConfig(requestConfig)
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();

        return httpClient;
    }

		
	
	
	
    /**
     * 抓取页面代码
     * @param url 目标页面的url
     * @return 页面代码
     */
    public String getHtml(String url) {
    	
    	HttpGet httpget = new HttpGet(url);
        config(httpget);
        CloseableHttpResponse response = null;
        
        try {
			//Thread.sleep(10);
            response = getHttpClient(url).execute(httpget,HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
            return null;
        } /*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return null;
            
		} */finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    
    
    /**
     * for test
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException{
        HttpConnectionManager httpConnectionManager = new HttpConnectionManager();
        Date start = new Date();
        System.out.println(httpConnectionManager.getHtml("http://www.baike.com/category/Ajax_cate.jsp?catename=PlayStation Portable"));
        Date end = new Date();
        System.out.println((end.getTime() - start.getTime())/1000.0 + " 秒");
    }
}

