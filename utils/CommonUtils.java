package com.uid.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.uid.common.LogWriter;

public class CommonUtils {
	
	static public String RESULT_JSON="resultJson";
	static public String RESULT_WEB_PAGE="resultWebPage";
	
	/**
	 * json格式的请求返回参数设置
	 * @param jsonMap
	 * @return
	 */
	static public  Map<String ,Object> resultJsonSet(Map<String ,Object> jsonMap){
		Map<String ,Object> result = new HashMap<String ,Object>();
		result.put(RESULT_JSON, jsonMap);
		return result;		
	}
	
	/**
	 * web页面的请求返回结果设置
	 * @param webPage
	 * @return
	 */
	static public Map<String ,Object> resultWebPageSet(String webPage){
		Map<String ,Object> result = new HashMap<String ,Object>();
		result.put(RESULT_WEB_PAGE, webPage);
		return result;		
	}
	
	/**
	 * 以json格式输出请求返回结果
	 * @param response
	 * @param json
	 */
	static public void outJsonString(HttpServletResponse response,String json) {  
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("text/html; charset=UTF-8");
        try { 
        	System.out.println("result json string:"+json);
            PrintWriter out = response.getWriter();  
            out.print(json);
//            out.write(json);  
            out.close();
        } catch (IOException e) {  
        	LogWriter.log("outJsonString IOException", e);
            e.printStackTrace();  
        }    
    }    
	
	/**
	 * 在control类中返回请求结果
	 * @param busResult
	 * @param response
	 * @return
	 */
	static  public Object controlResult(Map<String,Object> busResult,HttpServletResponse resp){
		 if(busResult == null){
			 return null;
		 }
		 if(busResult.containsKey(RESULT_JSON)){
			 CommonUtils.outJsonString(resp, JsonUtils.toString(busResult.get(RESULT_JSON)));
	         return null;  						 
		 }
         return  busResult.get(RESULT_WEB_PAGE);  
	}

}
