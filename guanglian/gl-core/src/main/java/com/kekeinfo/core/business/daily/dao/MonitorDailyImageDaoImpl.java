package com.kekeinfo.core.business.daily.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.daily.model.MonitorDailyImage;
import com.kekeinfo.core.business.daily.model.QMonitorDailyImage;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("monitordailyimageDao")
public class MonitorDailyImageDaoImpl extends KekeinfoEntityDaoImpl<Long, MonitorDailyImage>
		implements MonitorDailyImageDao {
	public MonitorDailyImageDaoImpl() {
		super();
	}

	@Override
	public List<MonitorDailyImage> getByMid(Long gid) {
		QMonitorDailyImage qDaily = QMonitorDailyImage.monitorDailyImage;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDaily)
		.where(qDaily.monitorDaily.id.eq(gid));
		
		List<MonitorDailyImage> ds = query.list(qDaily);
		return ds;
	}
}