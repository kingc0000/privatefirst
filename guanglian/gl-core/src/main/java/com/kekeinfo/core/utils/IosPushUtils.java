package com.kekeinfo.core.utils;

import org.springframework.stereotype.Component;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

@Component
public class IosPushUtils {
	private String  pushToken;
	private String  title;
	//private String cpath =this.getClass().getResource("/").getPath()+"ios/guanglian.p12"; 
	//private String cpath =this.getClass().getResource("/").getPath().replace("/classes", "")+"ios/guanglian.p12"; 
	//private String cpath =this.getClass().getResource("/").getPath()+"ios/gln.p12"; 
	//private String cpath =this.getClass().getResource("/").getPath().replace("/classes", "")+"ios/glios.p12"; 
	private String cpath =this.getClass().getResource("/").getPath().replace("/WEB-INF/classes", "")+"/resources/aps/glios.p12";
	
	private String  password = "123qwe4R";
	
	public void iPush(){
		try{
			ApnsService service =APNS.newService().withCert(cpath,password).withProductionDestination().build(); 
	    	 //ApnsService service =APNS.newService().withCert(cpath,password).withSandboxDestination().build(); 
	          String payload = APNS.newPayload().alertBody(title).badge(1).sound("default").build();  
	          service.push(pushToken, payload);
		}catch (Exception e){
			e.printStackTrace();
		}
		  
	}
	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
