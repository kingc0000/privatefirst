package com.kekeinfo.core.business.monitor.oblique.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.oblique.dao.ObliqueDataDao;
import com.kekeinfo.core.business.monitor.oblique.model.ObliqueData;

@Service("obliquedataService")
public class ObliqueDataServiceImpl extends KekeinfoEntityServiceImpl<Long, ObliqueData> implements ObliqueDataService {
	
	private ObliqueDataDao obliquedataDao;


	@Autowired
	public ObliqueDataServiceImpl(ObliqueDataDao obliquedataDao) {
		super(obliquedataDao);
		this.obliquedataDao = obliquedataDao;
	}
	
	@Transactional
	@Override
	public void saveOrUpdate(ObliqueData obliqueData) throws ServiceException {
		// 每个时间点只能有一条数据
		ObliqueData dbdata = this.obliquedataDao.getByDate(obliqueData.getCurDate(),
				obliqueData.getDepth().getId());
		if (dbdata != null) {
			obliqueData.setId(dbdata.getId());
		}
		if (obliqueData.getId() == null) {
			super.save(obliqueData);
		} else {
			super.update(obliqueData);
		}
		ObliqueData next = obliquedataDao.getNext(obliqueData.getCurDate(), obliqueData.getDepth().getId());
		if (next != null) {
			next.setLastTotal(obliqueData.getCurTotal());
			super.update(next);
		} 
	}

	@Transactional
	@Override
	public void deleteAndUpdate(ObliqueData obliqueData) throws ServiceException {
		super.delete(obliqueData);
		ObliqueData tobliqueData = obliquedataDao.getNext(obliqueData.getCurDate(),obliqueData.getDepth().getId());
		ObliqueData bobliqueData = obliquedataDao.getLast(obliqueData.getCurDate(),obliqueData.getDepth().getId());
		//有上级数据
		if(tobliqueData!=null) {
			//上下级数据同时存在
			if(bobliqueData!=null) {
				tobliqueData.setLastTotal(bobliqueData.getCurTotal());
				this.saveOrUpdate(tobliqueData);
			//只有上级数据存在+
			}else {
				tobliqueData.setLastTotal(new BigDecimal(0));
				this.saveOrUpdate(tobliqueData);
			}
		}
	}

	@Override
	public ObliqueData getByDate(Date date, long sid) throws ServiceException {
		return obliquedataDao.getByDate(date, sid);
	}

	@Override
	public ObliqueData getLast(Date date, long sid) throws ServiceException {
		return obliquedataDao.getLast(date, sid);
	}

	@Override
	public ObliqueData getNext(Date date, long sid) throws ServiceException {
		return obliquedataDao.getNext(date, sid);
	}

	@Override
	public ObliqueData getMax(Date date, String mids) throws ServiceException {
		return obliquedataDao.getMax(date, mids);
	}

	@Override
	public ObliqueData getByIdWithPoint(long id) throws ServiceException {
		return obliquedataDao.getByIdWithPoint(id);
	}

	@Override
	public ObliqueData getEqualsHeightData(BigDecimal initHeight, long sid) throws ServiceException {
		return obliquedataDao.getEqualsHeightData(initHeight, sid);
	}

	@Override
	public List<ObliqueData> getByDid(List<Long> dids,Date date) throws ServiceException {
		// TODO Auto-generated method stub
		return obliquedataDao.getByDid(dids,date);
	}
}
