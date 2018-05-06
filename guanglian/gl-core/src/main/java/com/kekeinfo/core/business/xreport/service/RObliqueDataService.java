package com.kekeinfo.core.business.xreport.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.xreport.model.RObliqueData;

public interface RObliqueDataService extends KekeinfoEntityService<Long, RObliqueData> {
	List<RObliqueData> getByRid(Long rid) throws ServiceException;
}
