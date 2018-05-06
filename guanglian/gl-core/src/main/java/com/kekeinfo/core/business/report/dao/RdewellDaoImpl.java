package com.kekeinfo.core.business.report.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.report.model.Rdewell;


@Repository("rdewellDataDao")
public class RdewellDaoImpl extends KekeinfoEntityDaoImpl<Long, Rdewell> implements RdewellDao {
	
}
