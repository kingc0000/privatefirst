package com.kekeinfo.core.business.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.model.MonitorUser;
import com.kekeinfo.core.business.monitor.dao.MonitorUserDao;

@Service("monitoruserService")
public class MonitorUserServiceImpl extends KekeinfoEntityServiceImpl<Long, MonitorUser> implements MonitorUserService {
	private MonitorUserDao monitoruserDao;

	@Autowired
	public MonitorUserServiceImpl(MonitorUserDao monitoruserDao) {
		super(monitoruserDao);
		this.monitoruserDao = monitoruserDao;
	}

	@Override
	public List<Object[]> getPinYin() throws ServiceException {
		// TODO Auto-generated method stub
		return monitoruserDao.getPinYin();
	}
}
