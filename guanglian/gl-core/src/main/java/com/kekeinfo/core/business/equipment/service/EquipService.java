package com.kekeinfo.core.business.equipment.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;

import java.util.List;

import com.kekeinfo.core.business.equipment.model.Equip;

public interface EquipService extends KekeinfoEntityService<Long, Equip> {
	public Equip getByIdWithImg(long cid) throws ServiceException;
	public void saveOrUpdate(Equip equip,String delids) throws ServiceException;
	List<Object[]> getPinYin(String ids) throws ServiceException;
}
