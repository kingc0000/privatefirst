package com.kekeinfo.core.business.guardeqip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.equipment.model.Equip;
import com.kekeinfo.core.business.equipment.service.EquipService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.guardeqip.model.GuardEqip;
import com.kekeinfo.core.business.guardeqip.dao.GuardEqipDao;

@Service("guardeqipService")
public class GuardEqipServiceImpl extends KekeinfoEntityServiceImpl<Long, GuardEqip> implements GuardEqipService {
	@SuppressWarnings("unused")
	private GuardEqipDao guardeqipDao;
	@Autowired EquipService equipService;

	@Autowired
	public GuardEqipServiceImpl(GuardEqipDao guardeqipDao) {
		super(guardeqipDao);
		this.guardeqipDao = guardeqipDao;
	}

	@Override
	public void saveUpdate(GuardEqip ge) throws ServiceException {
		// TODO Auto-generated method stub
		Equip e=ge.getEquip();
		e.setStatus(2);
		equipService.saveOrUpdate(e);
		super.saveOrUpdate(ge);
	}

	@Override
	public void removeGuard(GuardEqip ge) throws ServiceException {
		// TODO Auto-generated method stub
		Equip e=ge.getEquip();
		e.setStatus(1);
		equipService.delete(e);
	}
}
