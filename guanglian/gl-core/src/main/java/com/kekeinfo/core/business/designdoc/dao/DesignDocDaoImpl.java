package com.kekeinfo.core.business.designdoc.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.designdoc.model.Designdoc;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;



@Repository("designDocDao")
public class DesignDocDaoImpl extends KekeinfoEntityDaoImpl<Long, Designdoc> implements DesignDocDao {

	public DesignDocDaoImpl() {
		super();
	}
	
	

	
}
