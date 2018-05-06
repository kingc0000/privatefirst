package com.kekeinfo.web.utils;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.appmessage.model.AMessage;
import com.kekeinfo.core.business.appmessage.model.MessageU;
import com.kekeinfo.core.business.appmessage.service.AMessageService;
import com.kekeinfo.core.business.appmessage.service.MessageUService;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.sign.model.Sign;
import com.kekeinfo.core.business.sign.service.SignService;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.BaiduPushUtitls;
import com.kekeinfo.core.utils.IosPushUtils;
import com.kekeinfo.core.utils.MessageTypeEnum;
import com.kekeinfo.web.constants.Constants;

@Component
public class AutoSend {
	private static final Logger LOGGER = LoggerFactory.getLogger(AutoSend.class);
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired SignService signService;
	@Autowired private IosPushUtils iosPushUtils;
	@Autowired private BaiduPushUtitls baiduPushUtitls;
	@Autowired private UserService userService;
	@Autowired AMessageService aMessageService;
	@Autowired MessageUService messageUService;
	
	@SuppressWarnings("unchecked")
	public void autoSend(){
		
		try{
			Map<Long,List<GJob>> ujmaps =  (Map<Long, List<GJob>>) webCacheUtils.getFromCache(Constants.USERGJOBS);
			if(ujmaps!=null && ujmaps.size()>0){
				for(Long uid:ujmaps.keySet()){
					List<Sign> sign = signService.getByUidTodayWithGuard(uid);
					if(sign!=null && sign.size()>0){
						//发送推送信息
						User user =userService.getById(uid);
						String aAgent = user.getAtype();
						if(StringUtils.isNotBlank(aAgent)){
							StringBuffer pg =new StringBuffer();
							StringBuffer pids =new StringBuffer();
							for(Sign s:sign){
								if(s.getSattus()==0){
									pg.append(s.getGuard().getName()).append("、");
									pids.append(s.getGuard().getId()).append(",");
								}
							}
							if(pg.length()>0){
								pg.deleteCharAt(pg.lastIndexOf("、"));
								pids.deleteCharAt(pids.lastIndexOf(","));
								AMessage am =new AMessage();
								am.setDateCreated(new Date());
								String message=pg.toString()+"项目 今天有工作安排，请确认！";
								am.setTitle(message);
								am.setMtype(MessageTypeEnum.GAlarm);
								am.setMessage(message);
								aMessageService.save(am);
								MessageU mu =new MessageU();
								mu.setMessage(am);
								mu.setUser(user);
								mu.setPids(pids.toString());
								messageUService.save(mu);
								if(aAgent.equalsIgnoreCase("iOS")){
									iosPushUtils.setPushToken(user.getDevice_token());
									iosPushUtils.setTitle(message);
									iosPushUtils.iPush();
								}else if(aAgent.equalsIgnoreCase("android")){
									baiduPushUtitls.setTitle("广联工作提醒通知");
									baiduPushUtitls.setChannelID(user.getDevice_token());
									baiduPushUtitls.setDeviceType(3);
									baiduPushUtitls.setMessage(message);
									baiduPushUtitls.pushMessage();
								}
							}
						}
					}
				}
			}
		}catch (Exception e){
			LOGGER.error(e.getMessage());
		}
	}
}
