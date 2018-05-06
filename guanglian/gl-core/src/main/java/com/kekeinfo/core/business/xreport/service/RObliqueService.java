package com.kekeinfo.core.business.xreport.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.xreport.model.ROblique;

public interface RObliqueService extends KekeinfoEntityService<Long, ROblique> {
	List<ROblique> getByXid(Long xid) throws ServiceException;
}
