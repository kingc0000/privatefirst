package com.kekeinfo.core.business.dreport.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.dreport.model.Dreport;
import com.kekeinfo.core.business.dreport.model.QDopinion;
import com.kekeinfo.core.business.dreport.model.QDreport;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.user.model.QUser;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

import edu.emory.mathcs.backport.java.util.Arrays;


@Repository("dreportDataDao")
public class DreportDaoImpl extends KekeinfoEntityDaoImpl<Long, Dreport> implements DreportDao {

	/**
	 * 获取当前用户所能受理的报告
	 */
	@Override
	public List<Dreport> getApproveDreports(Long uid, Integer[] types) {
		QDreport qDreport = QDreport.dreport;
		QUser qUser = QUser.user;
		QDopinion qDopinion = QDopinion.dopinion;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDreport).leftJoin(qDreport.opinions ,qDopinion)
		.leftJoin(qDopinion.users, qUser);
		
		if (uid==null) {
			if (types!=null) {
				query.where(qDreport.status.in(types), qDopinion.result.eq(-1));
			}
		} else {
			if (types!=null) {
				query.where(qUser.id.eq(uid), qDreport.status.in(types), qDopinion.result.eq(-1));
			} else {
				query.where(qUser.id.eq(uid));
			}
		}
		return query.distinct().list(qDreport);
	}
	
	/**
	 * 获取待办事项数量
	 * @param uid
	 * @param types
	 * @return
	 */
	public int countApproveDreports(Long uid, Integer[] types) {
		StringBuilder qs = new StringBuilder();
		qs.append("select count(distinct report) from Dreport as report ");
		qs.append("left join report.opinions opinion ");
		qs.append("left join opinion.users users ");
		qs.append("where opinion.result=-1 ");
		if (uid!=null) {
			qs.append(" and users.id=:userid ");
		}
		if (types != null) {
			qs.append(" and report.status in (:types)");
		}
		
		String hql = qs.toString();
		Query countQ = super.getEntityManager().createQuery(hql);
		
		if (uid!=null) {
			countQ.setParameter("userid", uid);
		}
		if (types != null) {
			countQ.setParameter("types", Arrays.asList(types));
		}
		
		Number count = (Number) countQ.getSingleResult(); //查询出总结果数量
		
		return count.intValue();
	}
	
	/**
	 * 获取待办事项数量
	 * @param uid
	 * @param types
	 * @return
	 */
	public int countApproveDreports(String uname, Integer[] types) {
		StringBuilder qs = new StringBuilder();
		qs.append("select count(distinct report) from Dreport as report ");
		qs.append("left join report.opinions opinion ");
		qs.append("left join opinion.users users ");
		qs.append("where opinion.result=-1 ");
		if (StringUtils.isNotBlank(uname)) {
			qs.append(" and users.adminName=:uname ");
		}
		if (types != null) {
			qs.append(" and report.status in (:types)");
		}
		
		String hql = qs.toString();
		Query countQ = super.getEntityManager().createQuery(hql);
		
		if (StringUtils.isNotBlank(uname)) {
			countQ.setParameter("uname", uname);
		}
		if (types != null) {
			countQ.setParameter("types", Arrays.asList(types));
		}
		
		Number count = (Number) countQ.getSingleResult(); //查询出总结果数量
		
		return count.intValue();
	}
	
}
