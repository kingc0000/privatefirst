package com.kekeinfo.core.business.daily.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.daily.model.GuardDailyImage;
import com.kekeinfo.core.business.daily.dao.GuardDailyImageDao;

@Service("guarddailyimageService")
public class GuardDailyImageServiceImpl extends KekeinfoEntityServiceImpl<Long, GuardDailyImage>
		implements GuardDailyImageService {
	private GuardDailyImageDao guarddailyimageDao;

	@Autowired
	public GuardDailyImageServiceImpl(GuardDailyImageDao guarddailyimageDao) {
		super(guarddailyimageDao);
		this.guarddailyimageDao = guarddailyimageDao;
	}

	@Override
	public List<GuardDailyImage> getByGid(Long gid) throws ServiceException {
		// TODO Auto-generated method stub
		return guarddailyimageDao.getByGid(gid);
	}
}
