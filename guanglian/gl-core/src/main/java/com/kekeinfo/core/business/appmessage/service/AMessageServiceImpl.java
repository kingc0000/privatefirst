package com.kekeinfo.core.business.appmessage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.appmessage.model.AMessage;
import com.kekeinfo.core.business.appmessage.dao.AMessageDao;

@Service("amessageService")
public class AMessageServiceImpl extends KekeinfoEntityServiceImpl<Long, AMessage> implements AMessageService {
	@SuppressWarnings("unused")
	private AMessageDao amessageDao;

	@Autowired
	public AMessageServiceImpl(AMessageDao amessageDao) {
		super(amessageDao);
		this.amessageDao = amessageDao;
	}
}
