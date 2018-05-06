package com.kekeinfo.core.business.job.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.job.model.QGJob;
import com.kekeinfo.core.business.user.model.User;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("gjobDao")
public class GJobDaoImpl extends KekeinfoEntityDaoImpl<Long, GJob> implements GJobDao {
	public GJobDaoImpl() {
		super();
	}

	@Override
	public List<GJob> getByGidsAndDate(List<Long> ids, Date date,Date edate) {
		
		QGJob qjob = QGJob.gJob;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qjob)
		.leftJoin(qjob.guard).fetch()
		.leftJoin(qjob.users).fetch()
		.where(qjob.guard.id.in(ids).and(qjob.startDate.between(date, edate)).and(qjob.rstatus.eq(0)));
		
		return query.distinct().list(qjob);
	}

	@Override
	public List<GJob> getToday(Date today, Long uid) {
		User user=new User();
		user.setId(uid);
		QGJob qjob = QGJob.gJob;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qjob)
		.leftJoin(qjob.guard).fetch()
		.where(qjob.users.contains(user).and(qjob.startDate.loe(today)).and(qjob.endDate.goe(today)).and(qjob.rstatus.eq(0)))
		.orderBy(qjob.arriveDate.asc());
		
		return query.distinct().list(qjob);
	}

	@Override
	public List<GJob> getEndDate(Date end) {
		QGJob qjob = QGJob.gJob;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qjob)
		.leftJoin(qjob.users).fetch()
		.leftJoin(qjob.guard).fetch()
		//.where(qjob.endDate.goe(end).and(qjob.rstatus.eq(0)));
		.where(qjob.endDate.goe(end));
		
		return query.distinct().list(qjob);
	}

	@Override
	public List<GJob> getNoAvaliable(Date end) {
		QGJob qjob = QGJob.gJob;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qjob)
		.where(qjob.endDate.lt(end).and(qjob.rstatus.eq(1)))
		.orderBy(qjob.arriveDate.asc());
		
		return query.distinct().list(qjob);
	}

	@Override
	public List<GJob> getEndDate(Date end, Long uid) {
		User user=new User();
		user.setId(uid);
		QGJob qjob = QGJob.gJob;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qjob)
		.leftJoin(qjob.users).fetch()
		.leftJoin(qjob.guard).fetch()
		.where(qjob.endDate.goe(end).and(qjob.users.contains(user)).and(qjob.rstatus.eq(0)));
		
		return query.distinct().list(qjob);
	}
}