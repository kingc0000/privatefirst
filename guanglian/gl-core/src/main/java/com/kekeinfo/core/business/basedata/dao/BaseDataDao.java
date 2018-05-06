package com.kekeinfo.core.business.basedata.dao;

import java.util.List;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;


public interface BaseDataDao extends KekeinfoEntityDao<Long,BasedataType> {	

	List<BasedataType> listByType(String storeType);
}
