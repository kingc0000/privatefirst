package com.kekeinfo.core.business.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.system.dao.SystemConfigurationDao;
import com.kekeinfo.core.business.system.model.SystemConfiguration;
import com.kekeinfo.core.business.system.model.SystemConfiguration_;
import com.kekeinfo.core.constants.Constants;
import com.kekeinfo.core.modules.utils.DreportConfig;

@Service("systemConfigurationService")
public class SystemConfigurationServiceImpl extends
		KekeinfoEntityServiceImpl<Long, SystemConfiguration> implements
		SystemConfigurationService {

	
	@Autowired
	public SystemConfigurationServiceImpl(
			SystemConfigurationDao systemConfigurationDao) {
			super(systemConfigurationDao);
	}
	
	public SystemConfiguration getByKey(String key) throws ServiceException {
		return super.getByField(SystemConfiguration_.key, key);
	}

	/**
	 * 获取报告审核、审定人配置的信息
	 */
	@Override
	public DreportConfig getDreportConfiguration() throws ServiceException {
		SystemConfiguration configuration = getByKey(Constants.DREPORT_CONFIG);
		DreportConfig dreportConfig = null;
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				dreportConfig = mapper.readValue(value, DreportConfig.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return dreportConfig;
	}


}
