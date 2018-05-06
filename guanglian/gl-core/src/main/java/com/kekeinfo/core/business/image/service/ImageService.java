package com.kekeinfo.core.business.image.service;


import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.image.model.Images;


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
public interface ImageService extends KekeinfoEntityService<Long, Images>
    
{

	Images getByName(String name) throws ServiceException;

}
