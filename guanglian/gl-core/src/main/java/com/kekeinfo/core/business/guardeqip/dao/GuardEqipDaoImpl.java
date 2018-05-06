package com.kekeinfo.core.business.guardeqip.dao;

import org.springframework.stereotype.Repository;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.guardeqip.model.GuardEqip;

@Repository("guardeqipDao")
public class GuardEqipDaoImpl extends KekeinfoEntityDaoImpl<Long, GuardEqip> implements GuardEqipDao {
	public GuardEqipDaoImpl() {
		super();
	}
}