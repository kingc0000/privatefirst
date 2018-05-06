package com.kekeinfo.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;

@Component
public class BaiduPushUtitls {
	private String apiKey="UF2AIPvxyB3OMgat4izp88A4DWUjiuuD";
	private String secretKey="Y178ISCjyd57zZtbPF2zwtPWggeuymex";
	private static final Logger LOGGER = LoggerFactory.getLogger(BaiduPushUtitls.class);
	 //设置设备类型，deviceType => 1 for web, 2 for pc, 
    //3 for android, 4 for ios, 5 for wp.
	private String channelID;
	private int deviceType=3;
	private String message;
	private String title;
	public void pushMessage() throws PushClientException, PushServerException{
		 PushKeyPair pair = new PushKeyPair(apiKey,secretKey);
		// 2. 创建BaiduPushClient，访问SDK接口
	        BaiduPushClient pushClient = new BaiduPushClient(pair,
	                BaiduPushConstants.CHANNEL_REST_URL);
	     // 3. 注册YunLogHandler，获取本次请求的交互信息
	        pushClient.setChannelLogHandler (new YunLogHandler () {
	            
				@Override
				public void onHandle(YunLogEvent arg0) {
					// TODO Auto-generated method stub
					LOGGER.debug(arg0.getMessage());
				}
	        });
	        try {
	            // 4. 设置请求参数，创建请求实例
	                PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().
	                    addChannelId(channelID).
	                    addMsgExpires(new Integer(3600*5)).   //设置消息的有效时间,单位秒,默认3600*5.
	                    addMessageType(1).            //设置消息类型,0表示透传消息,1表示通知,默认为0.
	                    addMessage("{\"title\":\""+this.title+"\",\"description\":\""+this.message+"\"}").
	                    addDeviceType(deviceType);     
	            // 5. 执行Http请求
	                PushMsgToSingleDeviceResponse response = pushClient.
	                    pushMsgToSingleDevice(request);
	            // 6. Http请求返回值解析
	                System.out.println("msgId: " + response.getMsgId()
	                        + ",sendTime: " + response.getSendTime());
	            } catch (PushClientException e) {
	                //ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
	                //'true' 表示抛出, 'false' 表示捕获。
	                if (BaiduPushConstants.ERROROPTTYPE) { 
	                    throw e;
	                } else {
	                    e.printStackTrace();
	                }
	            } catch (PushServerException e) {
	                if (BaiduPushConstants.ERROROPTTYPE) {
	                    throw e;
	                } else {
	                    System.out.println(String.format(
	                            "requestId: %d, errorCode: %d, errorMsg: %s",
	                            e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
	                }
	            }

	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
