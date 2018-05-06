package com.kekeinfo.core.business.content.dao;

import java.util.List;

import com.kekeinfo.core.business.content.model.Content;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;


public interface ContentDao extends KekeinfoEntityDao<Long, Content> {

	List<Content> listByType(String contentType) throws ServiceException;
}
