package com.kekeinfo.core.business.designdoc.service;

import com.kekeinfo.core.business.designdoc.model.Designdoc;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;

/**
 * 
 * Interface defining methods responsible for CMSContentService.
 * ContentServive will be be entry point for CMS and take care of following functionalities.
 * <li>Adding,removing Content images for given merchant store</li>
 * <li>Get,Save,Update Content data for given merchant store</li>
 *  
 * @author Yong chen
 *
 */
public interface DesignDocService extends KekeinfoEntityService<Long, Designdoc>
{

    

	void saveOrUpdate( Designdoc ddoc) throws ServiceException;
	
	void delelteByDic(long did) throws ServiceException;

}
