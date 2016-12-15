package com.uid.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.uid.common.LogWriter;
import com.uid.web.back.uidcode20.bean.UidCodeGenNumParam;


public class JsonUtils {
	private static final DateFormat df = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss Z yyyy", Locale.US);

	private static Map<Object, Object> primitiveMap = new HashMap<Object, Object>();

	/** 鍘熷瓙鏁版嵁绫诲瀷 */
	static {
		primitiveMap.put(boolean.class, Boolean.class);
		primitiveMap.put(char.class, Character.class);
		primitiveMap.put(byte.class, Byte.class);
		primitiveMap.put(short.class, Short.class);
		primitiveMap.put(int.class, Integer.class);
		primitiveMap.put(long.class, Long.class);
		primitiveMap.put(float.class, Float.class);
		primitiveMap.put(double.class, Double.class);
	}

	public JsonUtils() {
	}

	/**
	 * 灏唈ava bean杞崲鎴愪负json瀵硅薄
	 * 
	 * @param bean
	 * @return
	 */
	public static JSONObject toJSONObject(Object bean) {
		return toJSONObject(bean, null);
	}

	/**
	 * 灏唌ap杞崲鎴恓son瀵硅薄
	 */
	public static JSONObject toJSONObject(Map<Object, Object> map) {
		return mapToJSONObject(map, null);
	}

	public static JSONObject stringToJSONObject(String json) {
		try {
			return new JSONObject(json);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 灏唌ap杞寲涓篔SONObject 瀵硅薄
	 * 
	 * @param map
	 * @param json
	 * @return
	 */
	public static JSONObject mapToJSONObject(Map<Object, Object> map,
			JSONObject json) {
		if (map == null)
			return null;
		if (json == null)
			json = new JSONObject();
		try {
			((Map<Object, Object>) json).putAll(map);
		} catch (Exception e) {
			LogWriter.log("JsonUtil:", e);
		}
		return json;
	}

	/**
	 * 灏哹ean杞寲涓篔SONObject 瀵硅薄 娉ㄦ剰:浠呭鐞�鍘熷绫诲瀷, String, Number, Boolean, Date 杩欏嚑绉嶇被鍨�
	 * 
	 * @param bean
	 *            Object 瑕佽浆鎹㈢殑bean瀵硅薄
	 * @return JSONObject 杩斿洖鐨凧SONObject瀵硅薄
	 */
	public static JSONObject toJSONObject(Object bean, JSONObject json) {
		if (bean == null)
			return null;
		if (json == null)
			json = new JSONObject();
		Class klass = bean.getClass();
		Method[] methods = klass.getMethods();
		Method method;
		String name, key;
		Object obj = null;
		for (int i = 0; i < methods.length; i += 1) {
			try {
				method = methods[i];
				name = method.getName();
				key = "";
				if (name.startsWith("get")) {
					key = name.substring(3);
				} else if (name.startsWith("is")) {
					key = name.substring(2);
				}
				if (key.length() > 0 && !key.equals("Class")
						&& Character.isUpperCase(key.charAt(0))
						&& method.getParameterTypes().length == 0) {
					key = key.substring(0, 1).toLowerCase() + key.substring(1);
					obj = method.invoke(bean, (Object[]) null);
					// 浠呭锟� String, Number, Boolean, Date 杩欏嚑绉嶇被鍨�
					if (obj == null) {
						json.put(key, JSONObject.NULL);
					} else if (Date.class.isAssignableFrom(obj.getClass())) {
						json.put(key, TimeUtil.formatDateTime((Date) obj));
					} else if (obj.getClass().isPrimitive()
							|| obj.getClass().equals(String.class)
							|| obj.getClass().equals(Boolean.class)
							|| Number.class.isAssignableFrom(obj.getClass())) {
						json.put(key, obj);
					}
				}
			} catch (Exception e) {
				/* forget about it */
				LogWriter.log("JsonUtil:", e);
			}
		}
		return json;
	}

	/**
	 * 鐢↗SON涓殑瀵瑰簲鍊煎～鍏卋ean瀵硅薄鐨勫睘鎬�娉ㄦ剰:浠呭鐞�鍘熷绫诲瀷, String, Number, Boolean, Date 杩欏嚑绉嶇被鍨�
	 * 
	 * @param bean
	 *            Object 瑕佸～鍏呯殑bean瀵硅薄
	 * @param json
	 *            JSONObject 鐢ㄤ簬濉厖鐨刯son瀵硅薄
	 * @return int 瀹為檯濉厖鐨勫睘鎬ц鏁�
	 */
	public static int fillBean(Object bean, JSONObject json) {
		if (bean == null || json == null)
			return 0;
		int ret = 0;
		Class klass = bean.getClass();
		Method[] methods = klass.getMethods();
		Method method;
		String name, key;
		Object obj = null;
		Class[] types = null;
		for (int i = 0; i < methods.length; i += 1) {
			try {
				method = methods[i];
				name = method.getName();
				key = "";
				if (name.startsWith("set")) {
					key = name.substring(3);
					types = method.getParameterTypes();
					if (key.length() > 0 && !key.equals("Class")
							&& Character.isUpperCase(key.charAt(0))
							&& types.length == 1) {
						key = key.substring(0, 1).toLowerCase()
								+ key.substring(1);
						obj = null;
						Class clazz = types[0];
						// 浠呭鐞�String, Number, Boolean, Date 杩欏嚑绉嶇被鍨�
						if (json.has(key)
								&& (clazz.isPrimitive()
										|| clazz.equals(String.class)
										|| clazz.equals(Boolean.class)
										|| Number.class.isAssignableFrom(clazz) || Date.class
											.isAssignableFrom(clazz))) {
							// 澶勭悊Date鍙婂叾瀛愮被鎯呭喌
							if (Date.class.isAssignableFrom(clazz)) {
								String tmp = json.optString(key, null);
								if (tmp != null) {
									if (clazz.equals(java.sql.Date.class))
										obj = java.sql.Date.valueOf(tmp);
									else if (clazz.equals(java.sql.Time.class))
										obj = java.sql.Time.valueOf(tmp);
									else if (clazz
											.equals(java.sql.Timestamp.class))
										obj = java.sql.Timestamp.valueOf(tmp);
									else {
										obj = TimeUtil.parseDateTime(json
												.optString(key));
										if (obj == null)
											obj = TimeUtil.parseDate(json
													.optString(key));
									}
								}
							} else {
								obj = json.opt(key);
								if (JSONObject.NULL.equals(obj)) {
									obj = null;
								} else if (!clazz.equals(obj.getClass())) {
									String tmp = json.optString(key, "0");
									if (clazz.isPrimitive()) {
										clazz = (Class) primitiveMap.get(clazz);
									}
									obj = clazz.getConstructor(
											new Class[] { String.class })
											.newInstance(new Object[] { tmp });
								}
							}
							method.invoke(bean, new Object[] { obj });
							ret++;
						}
					}
				}
			} catch (Exception e) {
				/* forget about it */
				LogWriter.log("JsonUtil:", e);
			}
		}
		return ret;
	}

	/**
	 * 浠巎son鑾峰彇java bean
	 * 
	 * @param klass
	 * @param json
	 * @return
	 */
	public static Object getBean(Class klass, JSONObject json) {
		try {
			Object obj = klass.newInstance();
			JsonUtils.fillBean(obj, json);
			return obj;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 灏唈son鍒楄〃杞崲鎴愪负java bean鍒楄〃
	 * 
	 * @param klass
	 * @param jary
	 * @return
	 */
	public static List getBeanList(Class klass, JSONArray jary) {
		List list = new ArrayList();
		JSONObject json = null;
		Object obj = null;
		for (int i = 0; i < jary.length(); i++) {
			json = jary.optJSONObject(i);
			if (json != null) {
				obj = getBean(klass, json);
				if (obj != null)
					list.add(obj);
			}
		}
		return list;
	}

	/**
	 * 灏哹ean鍒楄〃杞崲鎴愪负json鍒楄〃
	 * 
	 * @param beanList
	 * @return
	 */
	public static List toJSONList(List beanList) {
		List list = new ArrayList();
		if (beanList != null) {
			for (int i = 0; i < beanList.size(); i++) {
				Object bean = beanList.get(i);
				list.add(toJSONObject(bean));
			}
		}
		return list;
	}

	/**
	 * 灏嗗垪琛ㄨ浆鎹㈡垚涓簀son鍒楄〃
	 * 
	 * @param beanList
	 * @return
	 */
	public static List mapToJSONList(List beanList) {
		List list = new ArrayList();
		if (beanList != null) {
			for (int i = 0; i < beanList.size(); i++) {
				Map map = (Map) beanList.get(i);
				list.add(toJSONObject(map));
			}
		}
		return list;
	}

	/**
	 * 灏嗗璞¤浆鎹㈡垚map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map Object2StringMap(Object obj) {
		if (obj != null) {
			try {
				Map map = new HashMap();
				Class c = obj.getClass();
				Method[] ms = c.getMethods();
				for (Method m : ms) {
					if (m.getName().startsWith("get")
							&& m.getParameterTypes().length == 0
							&& !m.getName().equals("get")
							&& !m.getName().equals("getClass")) {
						char[] fieldch = m.getName().substring(3).toCharArray();
						fieldch[0] = Character.toLowerCase(fieldch[0]);
						String field = new String(fieldch);
						Object value = m.invoke(obj, null);
						if (value != null) {
							if (value instanceof Date) {
								value = TimeUtil.formatDateTime((Date) value);
							}
						}
						map.put(field, value.toString());
					}
				}
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				LogWriter.log("JsonUtil:", e);
			}
		}
		return null;
	}

	public static String toString(Object object) {
		ObjectMapper mapper = getJacksonObjectMapper();
		return toString(object, mapper);
	}

	private static String toString(Object object, ObjectMapper mapper) {
		String json = "";
		try {
			json = mapper.writeValueAsString(object);

		} catch (IOException e) {
		}
		return json;
	}

	public static <T> T toBean(String json, Class<T> clazz) {
		if (StringUtils.isNotBlank(json)) {
			ObjectMapper mapper = getJacksonObjectMapper();
			try {
				return mapper.readValue(json, clazz);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static ObjectMapper getJacksonObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().setDateFormat(df);
		mapper.getDeserializationConfig().setDateFormat(df);
		return mapper;
	}
	
	static public void  main(String[] args){
		String json ="{\"superCodeGenNum\":\"20\",\"childCodeGenNum\":\"12\"}";
		UidCodeGenNumParam list = (UidCodeGenNumParam) JsonUtils.toBean(json, UidCodeGenNumParam.class);
	    
	}
}
