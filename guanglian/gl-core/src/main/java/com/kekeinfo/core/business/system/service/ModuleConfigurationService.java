package com.kekeinfo.core.business.system.service;

import java.util.List;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.system.model.IntegrationModule;

public interface ModuleConfigurationService extends
		KekeinfoEntityService<Long, IntegrationModule> {

	List<IntegrationModule> getIntegrationModules(String module);

	IntegrationModule getByCode(String moduleCode);
	
	void createOrUpdateModule(String json) throws ServiceException;
	


}
