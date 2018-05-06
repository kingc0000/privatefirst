package com.kekeinfo.core.business.system.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.system.model.IntegrationModule;

public interface ModuleConfigurationDao extends KekeinfoEntityDao<Long, IntegrationModule> {

	List<IntegrationModule> getModulesConfiguration(String module);

	IntegrationModule getByCode(String moduleCode);

}
