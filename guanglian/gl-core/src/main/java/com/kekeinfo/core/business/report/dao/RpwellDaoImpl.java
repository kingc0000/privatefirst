package com.kekeinfo.core.business.report.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.report.model.Rpwell;


@Repository("rpwellDao")
public class RpwellDaoImpl extends KekeinfoEntityDaoImpl<Long, Rpwell> implements RpwellDao {
	
}
