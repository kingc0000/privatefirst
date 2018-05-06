package com.kekeinfo.web.utils;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class RadomSixNumber {
	//59452密码获取校验码   59450注册获取校验码
	public static String REGIST_TEMP = "62362";
	public static String CHANGE_PW_TEMP = "63038";
//	private static String SMS_URL_PDT = "app.cloopen.com";
//	private static String SMS_PORT = "8883";
//	private static String SMS_ACCOUNT = "8a48b55150b86ee80150c17acac21769";
//	private static String SMS_ACCOUNT_TOKEN = "793aa20d7e2346da979f4fd77077e2bd";
//	private static String SMS_APPID_PDT = "8a48b55151f5dfae0151f616b2790080";
//	
//	private static String SMS_URL_TEST = "sandboxapp.cloopen.com";
//	private static String SMS_APPID_TEST = "aaf98f8950b866cf0150c17effc50765";
//	private static String REGIST_TEMP_TEST = "1"; //测试模板
	
	
	public static String getRadomNumber(){
		int[] array = {0,1,2,3,4,5,6,7,8,9};
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
		    int index = rand.nextInt(i);
		    int tmp = array[index];
		    array[index] = array[i - 1];
		    array[i - 1] = tmp;
		}
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < 6; i++)
			result.append(array[i]);
		return result.toString();
	}
	
	/**
	 * 向指定用户手机号码发送信息
	 * @param phone 指定接收校验码用户手机号
	 * @param phoneTemplate 校验码模板
	 * @param radomNumber
	 * @param messagetime
	 * @param request
	 * @return
	 */
	public static boolean sendMessage(String phone, String phoneTemplate, String radomNumber,String messagetime,HttpServletRequest request){
		return sendMessage(phone, phoneTemplate, radomNumber, messagetime, "", request);
	}
	
	/**
	 * 
	 * 向指定用户手机号码发送信息
	 * @param phone 指定接收校验码用户手机号
	 * @param phoneTemplate 校验码模板
	 * @param radomNumber
	 * @param messagetime
	 * @param flag
	 * @param request
	 * @return
	 */
	public static boolean sendMessage(String phone, String phoneTemplate, String radomNumber,String messagetime,String flag,HttpServletRequest request){
//		HashMap<String, Object> result = null;

		
//		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
//		
//		restAPI.init(SMS_URL_PDT, SMS_PORT);
//		restAPI.setAccount(SMS_ACCOUNT, SMS_ACCOUNT_TOKEN);
//		restAPI.setAppId(SMS_APPID_PDT);
//		
//		result = restAPI.sendTemplateSMS(phone, phoneTemplate ,new String[]{radomNumber,messagetime});
//		
//		if("000000".equals(result.get("statusCode"))){
//			HttpSession session = request.getSession();
//			session.setAttribute("phonecode"+flag, radomNumber);
//			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
//			String createTime = (String) data.get("dateCreated");
//			session.setAttribute("phonecodetime"+flag, createTime);
//
//			return true;
//		}
		return false;
		/**
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
		}else{
			
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
		}*/
	}
	
	public static String getImageName(String oName){
		//生成随机数加时间
		int index = oName.lastIndexOf(".");
		String suffix = "";
		if (index>0) {
			suffix = oName.substring(index);
		}
		Long times = new Date().getTime();
		String piName=RadomSixNumber.getRadomNumber()+times.toString()+suffix;
		return piName;
	}

	/**
	 * 生成随机图片名称，如果没有图片后缀名，则采用指定的图片后缀名
	 * @param oName
	 * @param defaultSuffix 指定图片后缀名
	 * @return
	 */
	public static String getImageName(String oName, String defaultSuffix){
		//生成随机数加时间
		int index = oName.lastIndexOf(".");
		if (index>0) {
			defaultSuffix = oName.substring(index+1);
		}
		Long times = new Date().getTime();
		String piName=RadomSixNumber.getRadomNumber()+times.toString()+"."+defaultSuffix;
		return piName;
	}
	/**
	 * 根据图片类型返回指定的图片后缀名
	 * @param imageType
	 * @return
	 */
	public static String getImageSuffix(String imageType) {
		if (StringUtils.isNotBlank(imageType)) {
			String lowertype= imageType.toLowerCase();
			if (lowertype.indexOf("png")>0) {
				return "png";
			} else if(lowertype.indexOf("jpg")>0||lowertype.indexOf("jpeg")>0) {
				return "jpg";
			} else if(lowertype.indexOf("gif")>0) {
				return "gif";
			}
		}
		return "";
	}
	public static String getRadomNo(){
		Long times = new Date().getTime();
		String piName=RadomSixNumber.getRadomNumber()+times.toString();
		return piName;
	}
}
