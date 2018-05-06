package com.kekeinfo.core.business.image.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.image.model.QWellimages;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("wellImageDao")
public class WellImageDaoImpl extends KekeinfoEntityDaoImpl<Long, Wellimages> implements WellImageDao {

	public WellImageDaoImpl() {
		super();
	}

	@Override
	public Wellimages getByName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		QWellimages qImages = QWellimages.wellimages;

		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qImages).where(qImages.name.endsWithIgnoreCase(name));

		return  query.uniqueResult(qImages);
		}

}
