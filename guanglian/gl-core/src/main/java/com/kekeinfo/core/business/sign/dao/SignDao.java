package com.kekeinfo.core.business.sign.dao;

import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.sign.model.Sign;

public interface SignDao extends KekeinfoEntityDao<Long, Sign> {
	Sign getBySId(Long sid);
	List<Sign> getByJIdToday(Long jid);
	
	Sign withImg(Long jid);
	
	List<Sign> getByJId(Long jid);
	List<Sign> getByUidToday(Long jid);
	
	List<Sign> getByUidTodayWithGuard(Long jid);
}
