package com.kekeinfo.core.business.csite.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.constructionsite.model.CsiteCriteria;
import com.kekeinfo.core.business.constructionsite.model.QConstructionSite;
import com.kekeinfo.core.business.department.model.QDepartment;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;



@Repository("csiteDao")
public class CSiteDaoImpl extends KekeinfoEntityDaoImpl<Long, ConstructionSite> implements CSiteDao {

	public CSiteDaoImpl() {
		super();
	}
	
	

	@Override
	public ConstructionSite getByCid(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.project).fetch()
		.leftJoin(qContact.pbase).fetch()
		.leftJoin(qContact.images).fetch()
		.where(qContact.id.eq(cid));
		List<ConstructionSite> cs = query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}



	@Override
	public ConstructionSite getByUserName(String username) throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.where(qContact.project.name.eq(username));
		
		return query.uniqueResult(qContact);
	}



	@Override
	public Entitites<ConstructionSite> getByCriteria(CsiteCriteria criteria) throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qConstructionSite = QConstructionSite.constructionSite;
		QDepartment qDepartment = QDepartment.department;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qConstructionSite)
		.leftJoin(qConstructionSite.project.zone).fetch()
		.leftJoin(qConstructionSite.project.department,qDepartment).fetch();
		
		//多字段模糊匹配
		BooleanBuilder pBuilder = new BooleanBuilder();
		
		if(!StringUtils.isEmpty(criteria.getCode())) {
			pBuilder.andAnyOf(
					qConstructionSite.project.name.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString()),
					qConstructionSite.project.address.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString()),
					qConstructionSite.project.phone.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString()),
					qConstructionSite.project.projectOwner.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString()),
					qConstructionSite.project.memo.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString()),
					qConstructionSite.project.city.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString()),
					qConstructionSite.project.zone.name.like(new StringBuilder().append("%").append(criteria.getCode()).append("%").toString())
					);
		}
		
		if(criteria.getDepartmentID()!=-1){
			
			pBuilder.and(qDepartment.id.eq(criteria.getDepartmentID()));
		}
		
		if(criteria.getMaxCount()>0) {
			query.limit(criteria.getMaxCount());
			query.offset(criteria.getStartIndex());
		}
		if(pBuilder!=null) {
			query.where(pBuilder);
		}
		Entitites<ConstructionSite> elist =new Entitites<ConstructionSite>();
		elist.setTotalCount((int) query.count());
		elist.setEntites(query.list(qConstructionSite));
		return elist;
	}



	@Override
	public ConstructionSite getById(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.project).fetch()
		.where(qContact.id.eq(cid));
		List<ConstructionSite> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}



	@Override
	public ConstructionSite getByIdWithDepartment(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.project).fetch()
		.leftJoin(qContact.pbase).fetch()
		.where(qContact.id.eq(cid));
		List<ConstructionSite> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	/**
	 * 查询指定部门下所有项目的统计信息
	 * @param dept 如果为空，则查询所有部门下的项目
	 * @param csite
	 * @param offset
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Entitites<ConstructionSite> getByCsite(ConstructionSite csite,long offset) throws ServiceException {
		// TODO Auto-generated method stub
		Entitites<ConstructionSite> entites = new Entitites<>();
		QConstructionSite qSite = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qSite)
		.leftJoin(qSite.pbase).fetch()
		.leftJoin(qSite.project).fetch();
		/**
		.leftJoin(qSite.pwells).fetch()
		.leftJoin(qSite.owells).fetch()
		.leftJoin(qSite.iwells).fetch()
		.leftJoin(qSite.dewells).fetch()
		.leftJoin(qSite.ewells).fetch()
		.leftJoin(qSite.pbase).fetch()
		.leftJoin(qSite.project).fetch();*/
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		//不在需要部门ID
		/**
		if (dept!=null) {
			pBuilder.and(qSite.project.department.id.eq(dept.getId()));
		}*/
		if(StringUtils.isNotBlank(csite.getPbase().getRank())){
			pBuilder.and(qSite.pbase.rank.eq(csite.getPbase().getRank()));
		}
		
		if(StringUtils.isNotBlank(csite.getProject().getName())){
			pBuilder.and(qSite.project.name.like("%"+csite.getProject().getName()+"%"));
		}
		
		if(csite.getProject().getZone()!=null && csite.getProject().getZone().getId()!=null){
			pBuilder.and(qSite.project.zone.id.eq(csite.getProject().getZone().getId()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().geteRank())){
			pBuilder.and(qSite.pbase.eRank.eq(csite.getPbase().geteRank()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getPitDepth())){
			pBuilder.and(qSite.pbase.pitDepth.eq(csite.getPbase().getPitDepth()));
		}
		
		if(StringUtils.isNotBlank(csite.getProject().getFeatures())){
			pBuilder.and(qSite.project.features.eq(csite.getProject().getFeatures()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getSurroundFeatures())){
			pBuilder.and(qSite.pbase.surroundFeatures.eq(csite.getPbase().getSurroundFeatures()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getSurroundStyle())){
			pBuilder.and(qSite.pbase.surroundStyle.eq(csite.getPbase().getSurroundStyle()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getPattern())){
			pBuilder.and(qSite.pbase.pattern.eq(csite.getPbase().getPattern()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getType())){
			pBuilder.and(qSite.pbase.type.eq(csite.getPbase().getType()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getPrange())){
			pBuilder.and(qSite.pbase.prange.eq(csite.getPbase().getPrange()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getPmdOwner())){
			pBuilder.and(qSite.pbase.pmdOwner.like(csite.getPbase().getPmdOwner()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getUnit())){
			pBuilder.and(qSite.pbase.unit.like(csite.getPbase().getUnit()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getPreUnit())){
			pBuilder.and(qSite.pbase.preUnit.endsWithIgnoreCase(csite.getPbase().getPreUnit()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getDesign())){
			pBuilder.and(qSite.pbase.design.like(csite.getPbase().getDesign()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getContor())){
			pBuilder.and(qSite.pbase.contor.like(csite.getPbase().getContor()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getTechOwner())){
			pBuilder.and(qSite.pbase.techOwner.like(csite.getPbase().getTechOwner()));
		}
		
		if(StringUtils.isNotBlank(csite.getProject().getProjectOwner())){
			pBuilder.and(qSite.project.projectOwner.like(csite.getProject().getProjectOwner()));
		}
		
		if(StringUtils.isNotBlank(csite.getPbase().getLayer())){
			String [] ls = csite.getPbase().getLayer().split(",");
			Predicate[] ps = new Predicate[ls.length];
			for(int i=0;i<ls.length;i++){
				Predicate p = qSite.pbase.layer.like("%"+ls[i]+"%");
				ps[i]=p;
			}
			
			pBuilder.andAnyOf(ps);
		}
		if(StringUtils.isNotBlank(csite.getPbase().getConfined())){
			String [] ls = csite.getPbase().getConfined().split(",");
			Predicate[] ps = new Predicate[ls.length];
			for(int i=0;i<ls.length;i++){
				Predicate p = qSite.pbase.confined.like("%"+ls[i]+"%");
				ps[i]=p;
			}
			
			pBuilder.andAnyOf(ps);
		}
		if(StringUtils.isNotBlank(csite.getPbase().getPrecipitation())){
			String [] ls = csite.getPbase().getPrecipitation().split(",");
			Predicate[] ps = new Predicate[ls.length];
			for(int i=0;i<ls.length;i++){
				Predicate p = qSite.pbase.precipitation.like("%"+ls[i]+"%");
				ps[i]=p;
			}
			
			pBuilder.andAnyOf(ps);
		}
		
		query.limit(10);
		query.offset(offset);
		query.where(pBuilder);
		entites.setEntites( query.distinct().list(qSite));
		entites.setTotalCount(new Long(query.count()).intValue());
		return entites;
	}



	@Override
	public List<ConstructionSite> getByIds(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact);
		if(ids!=null){
			BooleanBuilder pBuilder = new BooleanBuilder();
			pBuilder.and(qContact.id.in(ids));
			query.where(pBuilder);
		}
		return query.distinct().list(qContact);
	}



	@Override
	public ConstructionSite getByCidWithWell(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.pwells).fetch()
		.leftJoin(qContact.owells).fetch()
		.leftJoin(qContact.iwells).fetch()
		.leftJoin(qContact.dewells).fetch()
		.leftJoin(qContact.project.department).fetch()
		.leftJoin(qContact.project.zone).fetch()
		.where(qContact.id.eq(cid));
		List<ConstructionSite> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}



	@Override
	public ConstructionSite getByCidWithALLWell(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.pwells).fetch()
		.leftJoin(qContact.pwells).fetch()
		.leftJoin(qContact.owells).fetch()
		.leftJoin(qContact.iwells).fetch()
		.leftJoin(qContact.dewells).fetch()
		.leftJoin(qContact.ewells).fetch()
		.leftJoin(qContact.project).fetch()
		.where(qContact.id.eq(cid));
		List<ConstructionSite> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	/**
	 * 查询项目下所有测点信息，包括测点的扩展信息
	 * @param cid
	 * @return
	 * @throws ServiceException
	 */
	public ConstructionSite getByCidForwells(long cid) throws ServiceException {
		StringBuilder qs = new StringBuilder();
		qs.append("select c from ConstructionSite as c ");
		qs.append("left join fetch c.pwells pw ");
		qs.append("left join fetch pw.pointInfo pinfo ");
		qs.append("left join fetch c.owells ow ");
		qs.append("left join fetch ow.pointInfo oinfo ");
		qs.append("left join fetch c.iwells iw ");
		qs.append("left join fetch iw.pointInfo iinfo ");
		qs.append("left join fetch c.dewells dew ");
		qs.append("left join fetch dew.pointInfo deinfo ");
		qs.append("left join fetch c.ewells ew ");
		qs.append("left join fetch ew.pointInfo einfo ");
		
		qs.append("where c.id=:id ");

    	String hql = qs.toString();
		Query q = super.getEntityManager().createQuery(hql);
    	q.setParameter("id", cid);
    	
    	return (ConstructionSite) q.getSingleResult();
	}

	@Override
	public ConstructionSite getByIdWithImg(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.images).fetch()
		.leftJoin(qContact.project).fetch()
		.where(qContact.id.eq(cid));
		List<ConstructionSite> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}



	@Override
	public List<ConstructionSite> withZone() throws ServiceException {
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.project).fetch()
		.where(qContact.status.gt(-1));
		List<ConstructionSite> cs =query.list(qContact);
		return cs;
	}



	@Override
	public List<ConstructionSite> withDepartment() throws ServiceException {
		// TODO Auto-generated method stub
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.project).fetch()
		.leftJoin(qContact.pbase).fetch();
		List<ConstructionSite> cs =query.list(qContact);
		return cs;
	}



	@Override
	public List<ConstructionSite> shengtong(Date date) throws ServiceException {
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.project).fetch();
		
		BooleanBuilder pBuilder = new BooleanBuilder();
		pBuilder.and(qContact.shengTong.eq(true));
		
		if(date!=null){
			pBuilder.and(qContact.project.auditSection.dateModified.goe(date));
		}
		query.where(pBuilder);
		return query.list(qContact);

	}



	@Override
	public List<ConstructionSite> getByPid(Long pid) throws ServiceException {
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.where(qContact.project.id.eq(pid));
		List<ConstructionSite> cs =query.list(qContact);
		return cs;
	}



	@Override
	public ConstructionSite withProjectGroup(long cid) throws ServiceException {
		QConstructionSite qContact = QConstructionSite.constructionSite;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.project).fetch()
		.leftJoin(qContact.project.wUsers).fetch()
		.where(qContact.id.eq(cid));
		List<ConstructionSite> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}



	@Override
	public ConstructionSite getBypId(long cid) throws ServiceException {
		// TODO Auto-generated method stub
				QConstructionSite qContact = QConstructionSite.constructionSite;
				JPQLQuery query = new JPAQuery (getEntityManager());
				query.from(qContact)
				.leftJoin(qContact.project).fetch()
				.where(qContact.id.eq(cid));
				List<ConstructionSite> cs =query.list(qContact);
				if(cs!=null && cs.size()>0){
					return cs.get(0);
				}
				return null;
	}
}
