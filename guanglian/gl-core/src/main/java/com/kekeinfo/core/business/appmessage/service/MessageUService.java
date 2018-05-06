package com.kekeinfo.core.business.appmessage.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.appmessage.model.AMessage;
import com.kekeinfo.core.business.appmessage.model.MessageU;

public interface MessageUService extends KekeinfoEntityService<Long, MessageU> {
	int read(String uid) throws ServiceException;
	public void sendconform(AMessage am,MessageU um) throws ServiceException;
	public MessageU getByMid(Long mid) throws ServiceException;
}
