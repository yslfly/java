package com.uid.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	static private Properties properties = new Properties();
 
	/**
	 * 取得properties文件
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * */
	static public Properties getPp(String fileName) throws Exception {	
		InputStream is = PropertiesUtils.class
				.getResourceAsStream("/com/uid/config/"+fileName);
		properties.load(is);
		return properties;
	}
	
	static public String getValueByKey(String fileName,String key){
		Properties pp = null;
		try {
			 pp = getPp(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (String) pp.get(key);		
	}
	
}
