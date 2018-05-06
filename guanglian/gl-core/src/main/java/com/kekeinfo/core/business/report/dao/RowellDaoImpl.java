package com.kekeinfo.core.business.report.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.report.model.Rowell;


@Repository("rowellDataDao")
public class RowellDaoImpl extends KekeinfoEntityDaoImpl<Long, Rowell> implements RowellDao {
	
}
