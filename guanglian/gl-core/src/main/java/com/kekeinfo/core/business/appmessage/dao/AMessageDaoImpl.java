package com.kekeinfo.core.business.appmessage.dao;

import org.springframework.stereotype.Repository;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.appmessage.model.AMessage;

@Repository("amessageDao")
public class AMessageDaoImpl extends KekeinfoEntityDaoImpl<Long, AMessage> implements AMessageDao {
	public AMessageDaoImpl() {
		super();
	}
}