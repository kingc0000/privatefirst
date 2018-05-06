package com.kekeinfo.core.business.invertedwell.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.constructionsite.model.QConstructionSite;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.model.QInvertedwell;
import com.kekeinfo.core.business.pointlink.model.QInvertedLink;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("invertedwellDao")
public class InvertedwellDaoImpl extends KekeinfoEntityDaoImpl<Long, Invertedwell> implements InvertedwellDao {

	public InvertedwellDaoImpl() {
		super();
	}

	@Override
	public Invertedwell getByIdWithCSite(Long pid) throws ServiceException {
		QInvertedwell qiInvertedwell = QInvertedwell.invertedwell;
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qiInvertedwell)
		.leftJoin(qiInvertedwell.cSite,qContact).fetch()
		.leftJoin(qiInvertedwell.pointInfo).fetch()
		.where(qiInvertedwell.id.eq(pid));
		List<Invertedwell> cs =query.list(qiInvertedwell);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}
	
	public Invertedwell getByIdWithPointLink(Long pid) throws ServiceException {
		QInvertedwell qiInvertedwell = QInvertedwell.invertedwell;
		QInvertedLink qLink = QInvertedLink.invertedLink;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qiInvertedwell)
		.leftJoin(qiInvertedwell.pointLink, qLink).fetch()
		.leftJoin(qLink.gateway).fetch()
		.where(qiInvertedwell.id.eq(pid));
		List<Invertedwell> cs =query.list(qiInvertedwell);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;		
	}

	@Override
	public List<Invertedwell> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		QInvertedwell qiInvertedwell = QInvertedwell.invertedwell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qiInvertedwell)
		.leftJoin(qiInvertedwell.cSite).fetch();
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		pBuilder.and(qiInvertedwell.dataStatus.gt(0).or(qiInvertedwell.powerStatus.gt(1)));
		if(ids!=null){
			pBuilder.and(qiInvertedwell.cSite.id.in(ids));
		}
		query.where(pBuilder);
		
		return query.list(qiInvertedwell);
	}
	/**
	 * 根据项目id和抽水井状态，获取抽水井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	public List<Invertedwell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException {
		QInvertedwell qiInvertedwell = QInvertedwell.invertedwell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qiInvertedwell);
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		if(pid!=null){
			pBuilder.and(qiInvertedwell.cSite.id.eq(pid));
		}
		if (status!=null) {
			pBuilder.and(qiInvertedwell.powerStatus.in(status));
		}
		query.where(pBuilder).orderBy(qiInvertedwell.name.asc());
		return query.list(qiInvertedwell);
	}

	@Override
	public List<Invertedwell> getBycid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QInvertedwell qiInvertedwell = QInvertedwell.invertedwell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qiInvertedwell)
		.leftJoin(qiInvertedwell.pointInfo).fetch()
		.where(qiInvertedwell.cSite.id.eq(cid));
		
		
		return query.list(qiInvertedwell);
	}

	@Override
	public List<Invertedwell> getByIds(List<Long> ids) throws ServiceException {
		QInvertedwell qiInvertedwell = QInvertedwell.invertedwell;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qiInvertedwell)
		.where(qiInvertedwell.id.in(ids));
		return query.list(qiInvertedwell);
	}
}
