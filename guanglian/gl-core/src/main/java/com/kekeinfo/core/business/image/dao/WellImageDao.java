package com.kekeinfo.core.business.image.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.image.model.Wellimages;


public interface WellImageDao extends KekeinfoEntityDao<Long, Wellimages> {
	Wellimages getByName(String name) throws ServiceException;
}
