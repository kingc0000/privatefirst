package com.kekeinfo.core.business.last.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.last.model.GuardLast;

public interface GuardLastDao extends KekeinfoEntityDao<Long, GuardLast> {
	List<GuardLast> getByUserID(long userid);
}
