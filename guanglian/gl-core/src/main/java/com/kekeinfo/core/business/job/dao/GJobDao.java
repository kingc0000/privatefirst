package com.kekeinfo.core.business.job.dao;

import java.util.Date;
import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.job.model.GJob;

public interface GJobDao extends KekeinfoEntityDao<Long, GJob> {
	List<GJob> getByGidsAndDate(List<Long> ids,Date date,Date edate);
	List<GJob> getToday(Date today,Long uid);
	List<GJob> getEndDate(Date end);
	List<GJob> getEndDate(Date end,Long uid);
	List<GJob> getNoAvaliable(Date end);
}
