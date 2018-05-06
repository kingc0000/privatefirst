package com.kekeinfo.core.business.image.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.image.model.Images;


public interface ImageDao extends KekeinfoEntityDao<Long, Images> {
	public Images getByName(String name) throws ServiceException;
}
