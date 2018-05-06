package com.kekeinfo.core.business.equipment.dao;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;

import java.util.List;

import com.kekeinfo.core.business.equipment.model.Equip;

public interface EquipDao extends KekeinfoEntityDao<Long, Equip> {
	public Equip getByIdWithImg(long cid) throws ServiceException;
	
	List<Object[]> getPinYin(String ids) throws ServiceException;
}
