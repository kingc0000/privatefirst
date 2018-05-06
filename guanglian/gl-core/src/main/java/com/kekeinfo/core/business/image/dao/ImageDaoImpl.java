package com.kekeinfo.core.business.image.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.image.model.QImages;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("imageDao")
public class ImageDaoImpl extends KekeinfoEntityDaoImpl<Long, Images> implements ImageDao {

	public ImageDaoImpl() {
		super();
	}

	@Override
	public Images getByName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		QImages qImages = QImages.images;

		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qImages).where(qImages.name.endsWithIgnoreCase(name));
		List<Images> is= query.list(qImages);
		if(is!=null && is.size()>0){
			return is.get(0);
		}

		return  null;
		}

}
