package com.kekeinfo.core.business.report.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.report.model.Rewell;


@Repository("rewellDataDao")
public class RewellDaoImpl extends KekeinfoEntityDaoImpl<Long, Rewell> implements RewellDao {
	
}
