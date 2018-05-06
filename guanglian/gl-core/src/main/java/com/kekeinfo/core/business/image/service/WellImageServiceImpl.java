package com.kekeinfo.core.business.image.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.image.dao.WellImageDao;
import com.kekeinfo.core.business.image.model.Wellimages;



@Service( "wellImageService" )
public class WellImageServiceImpl extends KekeinfoEntityServiceImpl<Long, Wellimages> implements WellImageService
{
	private final WellImageDao imageDao;
   
    @Autowired
    public WellImageServiceImpl( final WellImageDao imageDao )
    {
        super( imageDao );

        this.imageDao = imageDao;
    }

	@Override
	public Wellimages getByName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		return this.imageDao.getByName(name);
	}    
   

}
