package com.kekeinfo.core.business.system.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.system.model.SystemConfiguration;

@Repository("systemConfigurationDao")
public class SystemConfigurationDaoImpl extends KekeinfoEntityDaoImpl<Long, SystemConfiguration>
		implements SystemConfigurationDao {



}
