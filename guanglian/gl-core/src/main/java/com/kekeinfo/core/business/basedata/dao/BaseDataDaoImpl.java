package com.kekeinfo.core.business.basedata.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.QBasedataType;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;


@Repository("baseDataDao")
public class BaseDataDaoImpl extends KekeinfoEntityDaoImpl<Long, BasedataType> implements BaseDataDao {

	@Override
	public List<BasedataType> listByType(String storeType) {
		QBasedataType qBaseData = QBasedataType.basedataType;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qBaseData)	
			.where(qBaseData.type.eq(storeType));
		return query.list(qBaseData);
	}
}
