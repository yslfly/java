package com.uid.utils;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.uid.common.LogWriter;

public class HttpClientUtils  {
	
	public final static String RESP_ERROR = "error";
	private static final HttpClient client;
	private static final HttpConnectionManager connectionManager;
	
	static {
			HttpConnectionManagerParams params = loadHttpConfFromFile();
	    	connectionManager = new MultiThreadedHttpConnectionManager();
	        connectionManager.setParams(params);
	        client = new HttpClient(connectionManager);
	}
	 private static HttpConnectionManagerParams loadHttpConfFromFile(){
			HttpConnectionManagerParams params = new HttpConnectionManagerParams();
	        params.setConnectionTimeout(60*1000);
	        params.setSoTimeout(30*1000);
	        params.setStaleCheckingEnabled(true);
	        params.setTcpNoDelay(true);
	        params.setDefaultMaxConnectionsPerHost(100);
	        params.setMaxTotalConnections(1000);
	        params.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			return params;
	    }
     
	public static String httpPost(String url,String params) throws Exception{
    	 PostMethod method = new PostMethod(url);
         method.addRequestHeader("Connection", "Keep-Alive");
         method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
         method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
         method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
         try {
			method.setRequestEntity(new ByteArrayRequestEntity(params.getBytes("utf-8")));
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}           
         try {
             int statusCode = client.executeMethod(method);
             LogWriter.logInfo("HttpClientUtils.httpPost----response status:"+statusCode);
             byte[] resp = method.getResponseBody();
             String respStr = new String(resp, "utf-8");           
             if (statusCode != HttpStatus.SC_OK  ) {
            	 LogWriter.logInfo("HttpClientUtils.httpPost----response error msg:"+respStr);
                 return RESP_ERROR;
             }            
 			 return respStr;
         } catch (SocketTimeoutException e) {
        	 LogWriter.log("HttpClientUtils.httpPost---SocketTimeoutException",e);
             throw e;
         } catch (Exception e) {
        	 LogWriter.log("HttpClientUtils.httpPost---Exception",e);
         	 throw e;
         } finally {
             method.releaseConnection();
             method = null;
         }
     }
     
     public static void main(String[] args) throws Exception {
	}
}
