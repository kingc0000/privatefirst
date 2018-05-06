package com.kekeinfo.core.business.greport.dao;

import org.springframework.stereotype.Repository;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.greport.model.Greport;

@Repository("greportDao")
public class GreportDaoImpl extends KekeinfoEntityDaoImpl<Long, Greport> implements GreportDao {
	public GreportDaoImpl() {
		super();
	}
}