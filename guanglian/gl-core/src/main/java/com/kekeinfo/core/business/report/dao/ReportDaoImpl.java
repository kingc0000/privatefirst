package com.kekeinfo.core.business.report.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.report.model.QReport;
import com.kekeinfo.core.business.report.model.Report;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;


@Repository("reportDataDao")
public class ReportDaoImpl extends KekeinfoEntityDaoImpl<Long, Report> implements ReportDao {

	@Override
	public Report getIdWithWell(Long id) {
		// TODO Auto-generated method stub
		QReport qReport = QReport.report;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qReport)
		.leftJoin(qReport.dewells).fetch()
		.leftJoin(qReport.ewells).fetch()
		.leftJoin(qReport.iwells).fetch()
		.leftJoin(qReport.owells).fetch()
		.leftJoin(qReport.pwells).fetch()
		.where(qReport.id.eq(id));
		List<Report> rs =query.list(qReport);
		if(rs!=null && rs.size()>0){
			return rs.get(0);
		}
		return null;
	}
	
}
