package com.kekeinfo.core.business.report.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.report.model.Riwell;


@Repository("riwellDataDao")
public class RiwellDaoImpl extends KekeinfoEntityDaoImpl<Long, Riwell> implements RiwellDao {
	
}
