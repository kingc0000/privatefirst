package com.kekeinfo.core.business.report.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.report.model.Report;


public interface ReportDao extends KekeinfoEntityDao<Long,Report> {	
	
	Report getIdWithWell(Long id) ;

}
