package com.kekeinfo.core.business.xreport.dao;

import org.springframework.stereotype.Repository;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.xreport.model.XMreport;

@Repository("xmreportDao")
public class XMreportDaoImpl extends KekeinfoEntityDaoImpl<Long, XMreport> implements XMreportDao {
	public XMreportDaoImpl() {
		super();
	}
}