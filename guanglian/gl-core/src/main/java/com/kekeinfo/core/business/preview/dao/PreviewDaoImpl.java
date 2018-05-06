package com.kekeinfo.core.business.preview.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.preview.model.Preview;
import com.kekeinfo.core.business.preview.model.QPreview;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;


@Repository
public class PreviewDaoImpl extends KekeinfoEntityDaoImpl<Long, Preview> implements PreviewDao {

	/**
	 * 获取项目的总评价汇总信息
	 * @param cid
	 * @return 返回list数组， object[] = {项目id，项目名称，评价总分，评分维度1平均值，评分维度2平均值，评分维度3平均值，评分维度4平均值}
	 */
	public List<Object[]> getTotalInfo(Long cid) throws ServiceException {
		Set<Long> set = new HashSet<Long>();
		set.add(cid);
		return getTotalInfo(set, null, null);
	}
	
	/**
	 * 获取项目id数组的评论汇总集合
	 * @param cids
	 * @param limit
	 * @param offset
	 * @return 返回list数组， object[] = {项目id，项目名称，评价总分，评分维度1平均值，评分维度2平均值，评分维度3平均值，评分维度4平均值}
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getTotalInfo(Set<Long> cids, Integer limit, Integer offset) throws ServiceException {
		StringBuilder qs = new StringBuilder();
		qs.append("select p.project.id, p.project.name, count(p), AVG(p.score1), AVG(p.score2), AVG(p.score3), AVG(p.score4) from Preview as p ");
		if (cids!=null) {
			qs.append(" where p.project.id in (:cid) ");
		}
		qs.append(" GROUP BY p.project");

    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);
		if (offset!=null) {
			q.setFirstResult(offset);
		}
		if (limit!=null) {
			q.setMaxResults(limit);
		}
		if (cids!=null) {
			q.setParameter("cid", cids);
		}
    	return q.getResultList();
	}

	@Override
	public Preview getBypid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
				QPreview qoObservewell = QPreview.preview;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qoObservewell)
				.leftJoin(qoObservewell.previewImages).fetch()
				.where(qoObservewell.id.eq(cid));
				
				List<Preview> obs=query.list(qoObservewell);
				if(obs!=null && obs.size()>0){
					return obs.get(0);
				}
				return null;
	}

	@Override
	public Preview withProject(Long cid) throws ServiceException {
		QPreview qoObservewell = QPreview.preview;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qoObservewell)
		.leftJoin(qoObservewell.project).fetch()
		.where(qoObservewell.id.eq(cid));
		
		List<Preview> obs=query.list(qoObservewell);
		if(obs!=null && obs.size()>0){
			return obs.get(0);
		}
		return null;
	}
}
