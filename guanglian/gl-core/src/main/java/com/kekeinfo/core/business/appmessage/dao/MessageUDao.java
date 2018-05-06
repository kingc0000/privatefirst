package com.kekeinfo.core.business.appmessage.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.appmessage.model.MessageU;

public interface MessageUDao extends KekeinfoEntityDao<Long, MessageU> {
	int read(String uid);
	MessageU getByMid(Long mid);
	
}
