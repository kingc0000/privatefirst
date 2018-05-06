package com.kekeinfo.core.business.guard.point.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.gbase.service.GbaseServiceImpl;
import com.kekeinfo.core.business.guard.point.dao.DiameterConvertDao;
import com.kekeinfo.core.business.guard.point.model.DiameterConvert;

@Service("diameterconvertService")
public class DiameterConvertServiceImpl extends GbaseServiceImpl<DiameterConvert>
		implements DiameterConvertService {
	
	@SuppressWarnings("unused")
	private DiameterConvertDao diameterconvertDao;
	
	@Autowired
	public DiameterConvertServiceImpl(DiameterConvertDao diameterconvertDao) {
		super(diameterconvertDao);
		this.diameterconvertDao = diameterconvertDao;
	}

}
