package com.uid.utils;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uid.common.LogWriter;


/**
 * <p>
 
 */
public class ResponseMsgUtil {

	// 常见异常
		public static final int ECODE_EXCEPTION = 1;
		// 参数错误
		public static final int ECODE_ARGUMENT_ERROR = 2;
		// 数据库错误
		public static final int ECODE_DB_ERROR = 3;
		// 对象不存在
		public static final int ECODE_NOT_OBJECT = 4;
		// 没有权限
		public static final int ECODE_NOT_AUTH = 5;
		// AJAX 超时
		public static final int ECODE_AJAX_OVERTIME=6;
		// 未知
		public static final int ECODE_UNKNOWN = 999;
		// 系统错误
		public static final int SYSTEM_ERROR = -1;

		public static final String PROP_SUCCESS = "success";
		public static final String PROP_ERRCODE = "errcode";
		public static final String PROP_ERROR = "error";
		public static final String PROP_ERRORS = "errors";
		public static final String PROP_MSG = "msg";
		public static final String PROP_DATA = "data";

		private HttpServletResponse response;
		private JSONObject json = null;
        
		public void setHttpServletResponse(HttpServletResponse response){
			this.response=response;
		}
		
		
		public HttpServletResponse getResponse() {
			return response;
		}

		public ResponseMsgUtil() {
			json = new JSONObject();
			try {
				json.put(PROP_SUCCESS, false);
				json.put(PROP_MSG, "");
			} catch (JSONException ex) {
			}
		}

		public ResponseMsgUtil(HttpServletResponse response) {
			this();
			this.response = response;
		}

		public ResponseMsgUtil(HttpServletResponse response, boolean success) {
			this();
			this.response = response;
			this.setSuccess(success);
		}

		public ResponseMsgUtil(HttpServletResponse response, boolean success,
				String msg) {
			this();
			this.response = response;
			this.setSuccess(success);
			this.setMsg(msg);
		}

		public ResponseMsgUtil(HttpServletResponse response, boolean success,
				String msg, Object content) {
			this();
			this.response = response;
			this.setSuccess(success);
			this.setMsg(msg);
			this.setData(content);
		}

		public void setResponse(HttpServletResponse response) {
			this.response = response;
		}

		public void setSuccess(boolean bool) {
			try {
				json.put(PROP_SUCCESS, bool);
			} catch (JSONException ex) {
				LogWriter.log("ResponseMsgUtil:",ex);
			}
		}

		public void setErrCode(int code) {
			try {
				json.put(PROP_ERRCODE, code);
			} catch (Exception e) {
				LogWriter.log("ResponseMsgUtil:",e);
			}
		}

		public void setErrors(Object errs) {
			try {
				json.put(PROP_ERRORS, errs);
			} catch (JSONException ex) {
				LogWriter.log("ResponseMsgUtil:",ex);
			}
		}

		public void addError(Object err) {
			try {
				json.append(PROP_ERRORS, err);
			} catch (JSONException ex) {
				JSONArray jary = new JSONArray();
				jary.put(getErrors());
				jary.put(err);
				try {
					json.put(PROP_ERRORS, jary);
				} catch (JSONException e) {
					LogWriter.log("ResponseMsgUtil:",e);
				}
			}
		}

		public boolean isSuccess() {
			return json.optBoolean(PROP_SUCCESS, false);
		}

		public void setMsg(String msg) {
			try {
				json.put(PROP_MSG, msg);
			} catch (JSONException ex) {
				LogWriter.log("ResponseMsgUtil:",ex);
			}
		}

		public void setData(Object content) {
			if (content == null)
				return;
			try {
				json.put(PROP_DATA, content);
			} catch (JSONException ex) {
				LogWriter.log("ResponseMsgUtil:",ex);
			}
		}

		public void setDataByJSONString(String jstr) {
			try {
				if (jstr != null) {
					jstr = jstr.trim();
					if (jstr.charAt(0) == '[')
						json.put(PROP_DATA, new JSONArray(jstr));
					else if (jstr.charAt(0) == '{')
						json.put(PROP_DATA, new JSONObject(jstr));
				}
			} catch (Exception e) {
				LogWriter.log("ResponseMsgUtil:",e);
			}
		}

		@SuppressWarnings("unchecked")
		public void addData(Object content) {
			if (content == null)
				return;
			try {
				json.append(PROP_DATA, content);
			} catch (JSONException ex) {
				Object value = getData();
				try {
					JSONArray jary = null;
					if (value instanceof Collection) {
						jary = new JSONArray((Collection) value);
					} else if (value.getClass().isArray()) {
						jary = new JSONArray(value);
					} else {
						jary = new JSONArray();
						jary.put(value);
					}
					jary.put(content);
					json.put(PROP_DATA, jary);
				} catch (JSONException e) {
					LogWriter.log("ResponseMsgUtil:",e);
				}
			}
		}

		public void addDataByJSONString(String jstr) {
			try {
				if (jstr != null) {
					jstr = jstr.trim();
					if (jstr.charAt(0) == '[')
						addData(new JSONArray(jstr));
					else if (jstr.charAt(0) == '{')
						addData(new JSONObject(jstr));
				}
			} catch (Exception e) {
				LogWriter.log("ResponseMsgUtil:",e);
			}
		}

		public void setProperty(String key, Object value) {
			try {
				json.put(key, value);
			} catch (JSONException ex) {
				LogWriter.log("ResponseMsgUtil:",ex);
			}
		}

		public String getMsg() {
			return json.optString(PROP_MSG, "");
		}

		public Object getData() {
			return json.opt(PROP_DATA);
		}

		public Object getErrors() {
			return json.opt(PROP_ERRORS);
		}

		public int getErrCode() {
			return json.optInt(PROP_ERRCODE);
		}

		public Object getProperty(String key) {
			return json.opt(key);
		}

		public void removeProperty(String key) {
			json.remove(key);
		}

		public String toString() {
			return json.toString();
		}

		public void doResponse() {
			System.out.println(response.toString());
			if (response == null) {
				return;
			}
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("text/html; charset=UTF-8");
			try {
				//空是不显示data
//				Object obj = getData();
//				if (obj == null)
//					json.put(PROP_DATA, new JSONArray());
				String str = toString();
				System.out.println("response string :"+str);
				response.getWriter().print(str);
				//生成新的JSONObject
				json = new JSONObject();
			} catch (Exception ex) {
				LogWriter.log("ResponseMsgUtil:",ex);
			}
		}

		public static void doResponse(String jsonStr,HttpServletResponse response) {
			if (response == null) {
				return;
			}
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("text/plain; charset=UTF-8");
			try {
				response.getWriter().print(jsonStr);
			} catch (Exception ex) {
				LogWriter.log("ResponseMsgUtil:",ex);
			}
			//生成新的JSONObject
		}

		public JSONObject getJson() {
			return json;
		}

		public void setJson(JSONObject json) {
			this.json = json;
		}
}
