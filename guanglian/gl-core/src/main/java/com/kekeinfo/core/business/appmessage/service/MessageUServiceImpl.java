package com.kekeinfo.core.business.appmessage.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.utils.BaiduPushUtitls;
import com.kekeinfo.core.utils.IosPushUtils;
import com.kekeinfo.core.business.appmessage.model.AMessage;
import com.kekeinfo.core.business.appmessage.model.MessageU;
import com.kekeinfo.core.business.appmessage.dao.MessageUDao;

@Service("messageuService")
public class MessageUServiceImpl extends KekeinfoEntityServiceImpl<Long, MessageU> implements MessageUService {
	private MessageUDao messageuDao;
	
	@Autowired AMessageService aMessageService;
	@Autowired private IosPushUtils iosPushUtils;
	@Autowired private BaiduPushUtitls baiduPushUtitls;

	@Autowired
	public MessageUServiceImpl(MessageUDao messageuDao) {
		super(messageuDao);
		this.messageuDao = messageuDao;
	}

	@Override
	public int read(String uid) throws ServiceException {
		// TODO Auto-generated method stub
		return messageuDao.read(uid);
	}

	@Transactional
	@Override
	public void sendconform(AMessage am, MessageU um) throws ServiceException {
		// TODO Auto-generated method stub
		aMessageService.save(am);
		this.save(um);
		User au =um.getUser();
		if(StringUtils.isNotBlank(au.getAtype())){
			try{
				if(au.getAtype().equalsIgnoreCase("iOS")){
					iosPushUtils.setPushToken(au.getDevice_token());
					iosPushUtils.setTitle(am.getMessage());
					iosPushUtils.iPush();
				}else if(au.getAtype().equalsIgnoreCase("android")){
					baiduPushUtitls.setTitle("广联工作安排确认通知");
					baiduPushUtitls.setChannelID(au.getDevice_token());
					baiduPushUtitls.setDeviceType(3);
					baiduPushUtitls.setMessage(am.getMessage());
					baiduPushUtitls.pushMessage();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public MessageU getByMid(Long mid) throws ServiceException {
		// TODO Auto-generated method stub
		return messageuDao.getByMid(mid);
	}
}
