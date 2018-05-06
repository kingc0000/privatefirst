package com.kekeinfo.core.business.daily.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.daily.model.GuardDailyImage;
import com.kekeinfo.core.business.daily.model.QGuardDailyImage;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("guarddailyimageDao")
public class GuardDailyImageDaoImpl extends KekeinfoEntityDaoImpl<Long, GuardDailyImage> implements GuardDailyImageDao {
	public GuardDailyImageDaoImpl() {
		super();
	}

	@Override
	public List<GuardDailyImage> getByGid(Long gid) {
		// TODO Auto-generated method stub
				QGuardDailyImage qDaily = QGuardDailyImage.guardDailyImage;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qDaily)
				.where(qDaily.guardDaily.id.eq(gid));
				
				List<GuardDailyImage> ds = query.list(qDaily);
				return ds;
	}
}