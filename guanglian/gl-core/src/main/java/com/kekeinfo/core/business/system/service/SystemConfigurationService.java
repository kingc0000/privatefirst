package com.kekeinfo.core.business.system.service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.system.model.SystemConfiguration;
import com.kekeinfo.core.modules.utils.DreportConfig;

public interface SystemConfigurationService extends
		KekeinfoEntityService<Long, SystemConfiguration> {
	
	SystemConfiguration getByKey(String key) throws ServiceException;
	
	DreportConfig getDreportConfiguration() throws ServiceException;

}
