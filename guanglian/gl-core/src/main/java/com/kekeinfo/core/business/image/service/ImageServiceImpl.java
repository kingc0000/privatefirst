package com.kekeinfo.core.business.image.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.image.dao.ImageDao;
import com.kekeinfo.core.business.image.model.Images;



@Service( "imageService" )
public class ImageServiceImpl extends KekeinfoEntityServiceImpl<Long, Images> implements ImageService
{
	private final ImageDao imageDao;
   
    @Autowired
    public ImageServiceImpl( final ImageDao imageDao )
    {
        super( imageDao );

        this.imageDao = imageDao;
    }

	@Override
	public Images getByName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		return this.imageDao.getByName(name);
	}

}
