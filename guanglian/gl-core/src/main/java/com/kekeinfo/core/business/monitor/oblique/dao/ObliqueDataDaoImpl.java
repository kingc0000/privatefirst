package com.kekeinfo.core.business.monitor.oblique.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitor.oblique.model.ObliqueData;
import com.kekeinfo.core.business.monitor.oblique.model.QObliqueData;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("obliquedataDao")
public class ObliqueDataDaoImpl extends KekeinfoEntityDaoImpl<Long, ObliqueData> implements ObliqueDataDao {
	public ObliqueDataDaoImpl() {
		super();
	}

	@Override
	public ObliqueData getByDate(Date date, long sid) {
		QObliqueData qObliqueData = QObliqueData.obliqueData;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qObliqueData)
		.where(qObliqueData.depth.id.eq(sid).and(qObliqueData.curDate.eq(date)));
		List<ObliqueData> cs = query.list(qObliqueData);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public ObliqueData getLast(Date date, long sid) {
		QObliqueData qObliqueData = QObliqueData.obliqueData;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qObliqueData).
        orderBy(qObliqueData.curDate.desc())
        .where(qObliqueData.depth.id.eq(sid).and(qObliqueData.curDate.lt(date)));
		List<ObliqueData> cs = query.list(qObliqueData);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public ObliqueData getNext(Date date, long sid) {
		QObliqueData qObliqueData = QObliqueData.obliqueData;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qObliqueData).
        orderBy(qObliqueData.curDate.asc())
        .where(qObliqueData.depth.id.eq(sid).and(qObliqueData.curDate.gt(date)));
		List<ObliqueData> cs = query.list(qObliqueData);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public ObliqueData getMax(Date date, String mids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObliqueData getByIdWithPoint(long id) {
		QObliqueData qObliqueData = QObliqueData.obliqueData;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qObliqueData).
        leftJoin(qObliqueData.depth)
        .where(qObliqueData.id.eq(id));
		List<ObliqueData> cs = query.list(qObliqueData);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public ObliqueData getEqualsHeightData(BigDecimal initHeight, long sid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ObliqueData> getByDid(List<Long> dids,Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date date1 =c.getTime();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		Date date2 =c.getTime();
		QObliqueData qObliqueData = QObliqueData.obliqueData;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qObliqueData)
		.groupBy(qObliqueData.depth)
        .where(qObliqueData.curDate.between(date1, date2).and(qObliqueData.depth.id.in(dids)))
        .orderBy(qObliqueData.curDate.desc());
		List<ObliqueData> cs = query.list(qObliqueData);
		
		return cs;
	}
}