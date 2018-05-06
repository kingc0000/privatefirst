package com.kekeinfo.core.business.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.gateway.dao.GatewayDao;
import com.kekeinfo.core.business.gateway.model.Gateway;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;

@Service("gatewayService")
public class GatewayServiceImpl extends KekeinfoEntityServiceImpl<Long, Gateway> implements GatewayService {
	
	@SuppressWarnings("unused")
	private GatewayDao gatewayDao;
	
	@Autowired
	public GatewayServiceImpl(GatewayDao gatewayDao) {
		super(gatewayDao);
		this.gatewayDao = gatewayDao;
	}
}
