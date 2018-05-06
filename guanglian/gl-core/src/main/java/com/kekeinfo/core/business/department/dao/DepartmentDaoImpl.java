package com.kekeinfo.core.business.department.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.QDepartment;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.project.model.QProject;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;



@Repository("departmentDao")
public class DepartmentDaoImpl extends KekeinfoEntityDaoImpl<Long, Department> implements DepartmentDao {

	public DepartmentDaoImpl() {
		super();
	}
	
	
	@Override
	public Department getMerchantStore(String code)   throws ServiceException {
	

		//TODO add fetch
		QDepartment qDepartment = QDepartment.department;

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDepartment)	
			.where(qDepartment.name.eq(code));
		
		return query.uniqueResult(qDepartment);
	}



	@Override
	public Department getMerchantStore(Long merchantStoreId) {
		// TODO Auto-generated method stub
		QDepartment qMerchantStore = QDepartment.department;
		QProject qConstructionSite = QProject.project;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qMerchantStore)	
		.leftJoin(qMerchantStore.cSites,qConstructionSite).fetch()
		.leftJoin(qConstructionSite.zone).fetch()
			.where(qMerchantStore.id.eq(merchantStoreId));
		
		return query.uniqueResult(qMerchantStore);
	}


	@Override
	public List<Object[]> getPinYin() throws ServiceException {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select DEPARTMENT_ID as id,DEPARTMENT_NAME as name,IFNULL(ELT(INTERVAL(CONV(HEX(left(CONVERT(DEPARTMENT_NAME USING gbk),1)),16,10), ");
		qs.append("0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),");
		qs.append("'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P',");
		qs.append(" 'Q','R','S','T','W','X','Y','Z'),LEFT(DEPARTMENT_NAME,1)) as pinyin  from DEPARTMENT order by pinyin,name ");
		
		Query q  =super.getEntityManager().createNativeQuery(qs.toString());
    	
    	@SuppressWarnings("unchecked")
		List<Object[]> counts =  q.getResultList();

    	
    	return counts;
	}


	@Override
	public List<Department> listWithCite() throws ServiceException {
		// TODO Auto-generated method stub
		QDepartment qProject = QDepartment.department;
		QProject qConstructionSite=QProject.project;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProject)	
			.leftJoin(qProject.cSites,qConstructionSite).fetch()
			.leftJoin(qConstructionSite.zone).fetch()
			.leftJoin(qConstructionSite.camera).fetch();
		
		return query.distinct().list(qProject);
	}


	@Override
	public List<Department> listByCite(List<Long> cids) throws ServiceException {
		// TODO Auto-generated method stub
		QDepartment qProject = QDepartment.department;
		QProject csite = QProject.project;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qProject)	
			.leftJoin(qProject.cSites,csite).fetch();
		if(cids!=null && cids.size()>0){
			BooleanBuilder pBuilder = new BooleanBuilder();
			pBuilder.and(csite.id.in(cids));
			query.where(pBuilder);
		}
		
		return query.distinct().list(qProject);
	}


	@Override
	public Department getMerchantCode(String code) throws ServiceException {
QDepartment qDepartment = QDepartment.department;

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qDepartment)	
			.where(qDepartment.code.eq(code));
		
		List<Department> cs =query.list(qDepartment);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}
	
}
