package com.kekeinfo.core.business.monitor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.model.QWaterLineType;
import com.kekeinfo.core.business.monitor.model.WaterLineType;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("waterlinetypeDao")
public class WaterLineTypeDaoImpl extends KekeinfoEntityDaoImpl<Long, WaterLineType> implements WaterLineTypeDao {
	public WaterLineTypeDaoImpl() {
		super();
	}

	@Override
	public List<WaterLineType> getByMid(Long mid) {
		QWaterLineType qContact = QWaterLineType.waterLineType;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.baseType).fetch()
		.where(qContact.monitor.id.eq(mid));
		List<WaterLineType> cs =query.list(qContact);
		return cs;
	}

	@Override
	public List<WaterLineType> getByTid(Long tid) {
		QWaterLineType qContact = QWaterLineType.waterLineType;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.where(qContact.baseType.id.eq(tid));
		List<WaterLineType> cs =query.list(qContact);
		return cs;
	}
}