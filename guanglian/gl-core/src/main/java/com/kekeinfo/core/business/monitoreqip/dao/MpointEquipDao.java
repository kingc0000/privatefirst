package com.kekeinfo.core.business.monitoreqip.dao;


import java.util.List;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.monitoreqip.model.MpointEquip;


public interface MpointEquipDao extends KekeinfoEntityDao<Long, MpointEquip> {
	List<MpointEquip> getByMeid(long meid);
	List<MpointEquip> getByMtype(String mtype);
}

