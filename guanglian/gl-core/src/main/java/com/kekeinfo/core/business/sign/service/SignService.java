package com.kekeinfo.core.business.sign.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.sign.model.Sign;

public interface SignService extends KekeinfoEntityService<Long, Sign> {
	Sign getBySId(Long sid) throws ServiceException;
	List<Sign> getByJIdToday(Long jid) throws ServiceException;
	void saveNew(Sign sign) throws ServiceException;
	List<Sign> getByJId(Long jid) throws ServiceException;
	List<Sign> getByUidToday(Long jid) throws ServiceException;
	List<Sign> getByUidTodayWithGuard(Long jid) throws ServiceException;
	Sign withImg(Long jid) throws ServiceException;
}
