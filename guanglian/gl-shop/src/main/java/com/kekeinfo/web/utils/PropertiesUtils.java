package com.kekeinfo.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class PropertiesUtils {
	private static List<Properties> list_properties = new ArrayList<Properties>();
	private static String[] pro_filename = new String[]{"/jcifs.properties"};
	static {
		try {
			for (int i = 0; i < pro_filename.length; i++) {
				String str = pro_filename[i];
				InputStream inputStream = PropertiesUtils.class.getResourceAsStream(str);
				Properties pro_properties = new Properties();
				//因为字节流是无法读取中文的，所以采取reader把inputStream转换成reader用字符流来读取中文
				BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				pro_properties.load(bf);
				inputStream.close(); // 关闭流
				list_properties.add(pro_properties);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*try {
			InputStream inputStream = PropertiesUtils.class.getResourceAsStream(xml_filename);
			xml_properties.loadFromXML(inputStream);
			inputStream.close(); // 关闭流
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public static String getPropertiesValue(String key) {
		return getPropertiesValue(0, key.toLowerCase());
	}
	public static String getPropertiesValue(int index, String key) {
		String score=list_properties.get(index).getProperty(key.toUpperCase());
		if(score == null || score==""){
		    score = list_properties.get(0).getProperty(key.toLowerCase())	;
		}
		return score;
	}
	
	
	/*public static String getXmlValue(String key) {
		return xml_properties.getProperty(key);
	}*/

}
