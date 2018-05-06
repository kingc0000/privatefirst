package com.kekeinfo.test.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class MainTest {

	@Test
	public void testJsonParser() {
		JSONParser parser = new JSONParser();
		String json = "{\"name\":\"sam\","
				+ "\"code\":\"365000\","
				+ "\"address\":\"shanghai\"}";
		String json2 = "[{\"name\":\"name\", \"value\":\"sam\"},"
				+ "{\"name\":\"age\", \"value\":\"36\"},"
				+ "{\"name\":\"address\", \"value\":\"baoshang\"},"
				+ "{\"name\":\"sex\", \"value\":\"M\"}]";
		try {
			//非数组格式
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			String name = (String) jsonObject.get("name");
			System.out.println(name);
			System.out.println(jsonObject.toJSONString());
			//数组格式
			JSONArray jsonArray = (JSONArray) parser.parse(json2);
			for(int i=0;i<jsonArray.size();i++){
	        	JSONObject obj = (JSONObject) jsonArray.get(i);
//	            map.put(obj.get("name").toString(), obj.get("value"));
	            System.out.println(obj.get("name").toString()+"="+obj.get("value"));
	            System.out.println(i + "=" + obj.toJSONString());
	        }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
