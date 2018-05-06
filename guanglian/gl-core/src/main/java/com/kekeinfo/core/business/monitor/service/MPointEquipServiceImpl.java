package com.kekeinfo.core.business.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.model.MPoint;
import com.kekeinfo.core.business.monitor.dao.MPointEquipDao;

@Service("mpointService")
public class MPointEquipServiceImpl extends KekeinfoEntityServiceImpl<Long, MPoint> implements MPointEquipService {
	@SuppressWarnings("unused")
	private MPointEquipDao mpointequipDao;

	@Autowired
	public MPointEquipServiceImpl(MPointEquipDao mpointequipDao) {
		super(mpointequipDao);
		this.mpointequipDao = mpointequipDao;
	}
}
