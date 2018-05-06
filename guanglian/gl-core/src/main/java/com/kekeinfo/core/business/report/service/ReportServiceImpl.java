package com.kekeinfo.core.business.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.report.dao.ReportDao;
import com.kekeinfo.core.business.report.model.Report;

@Service("reportService")
public class ReportServiceImpl extends KekeinfoEntityServiceImpl<Long, Report> implements
		ReportService {
	
	private ReportDao dailyDao;
	
	@Autowired
	public ReportServiceImpl(ReportDao dailyDao) {
		super(dailyDao);
		this.dailyDao = dailyDao;
	}
	
	@Override
	@Transactional
	public void saveOrUpdate(Report report) throws ServiceException{
		if(report.getId()==null){
			this.save(report);
		}else{
			this.update(report);
		}
	}

	@Override
	public Report getIdWithWell(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return dailyDao.getIdWithWell(id);
	}
}
