package com.kekeinfo.core.business.monitoreqip.service;

import java.util.List;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.monitoreqip.model.MpointEquip;

public interface MpointEquipService extends KekeinfoEntityService<Long, MpointEquip> {
	List<MpointEquip> getByMeid(long meid) throws ServiceException;
	List<MpointEquip> getByMtype(String mtype) throws ServiceException;
}
