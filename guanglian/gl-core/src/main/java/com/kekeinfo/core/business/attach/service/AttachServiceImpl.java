package com.kekeinfo.core.business.attach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.attach.dao.AttachDao;

@Service("attachService")
public class AttachServiceImpl extends KekeinfoEntityServiceImpl<Long, Attach> implements AttachService {
	private AttachDao attachDao;

	@Autowired
	public AttachServiceImpl(AttachDao attachDao) {
		super(attachDao);
		this.attachDao = attachDao;
	}

	@Override
	public Attach getByName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		return this.attachDao.getByName(name);
	}
}
