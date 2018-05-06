package com.kekeinfo.core.business.daily.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.daily.model.Daily;
import com.kekeinfo.core.business.daily.model.QDaily;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;


@Repository("dailyDataDao")
public class DailyDaoImpl extends KekeinfoEntityDaoImpl<Long, Daily> implements DailyDao {

	@Override
	public Daily getByDate(Long pid, Date date1) throws ServiceException {
		// TODO Auto-generated method stub
		QDaily qDaily = QDaily.daily;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDaily)
		.leftJoin(qDaily.wellCon).fetch()
		.where(qDaily.datec.eq(date1).and(qDaily.cSite.id.eq(pid)));
		
		List<Daily> ds = query.list(qDaily);
		if(ds!=null && ds.size()>0){
			return ds.get(0);
		}
		return null;
	}
	
}
