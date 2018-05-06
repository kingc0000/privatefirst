package com.kekeinfo.core.business.gateway.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.gateway.model.Gateway;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;

@Repository("gatewayDao")
public class GatewayDaoImpl extends KekeinfoEntityDaoImpl<Long, Gateway> implements GatewayDao {

	public GatewayDaoImpl() {
		super();
	}
	
}
