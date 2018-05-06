package com.kekeinfo.core.business.monitoreqip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitoreqip.model.MpointEquip;
import com.kekeinfo.core.business.monitoreqip.dao.MpointEquipDao;

@Service("mpointequipService")
public class MpointEquipServiceImpl extends KekeinfoEntityServiceImpl<Long, MpointEquip> implements MpointEquipService {
	private MpointEquipDao mpointequipDao;

	@Autowired
	public MpointEquipServiceImpl(MpointEquipDao mpointequipDao) {
		super(mpointequipDao);
		this.mpointequipDao = mpointequipDao;
	}

	@Override
	public List<MpointEquip> getByMeid(long meid) throws ServiceException {
		// TODO Auto-generated method stub
		return mpointequipDao.getByMeid(meid);
	}

	@Override
	public List<MpointEquip> getByMtype(String mtype) throws ServiceException {
		// TODO Auto-generated method stub
		return mpointequipDao.getByMtype(mtype);
	}
}
