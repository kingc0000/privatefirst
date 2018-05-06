package com.kekeinfo.core.business.zone.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.utils.CacheUtils;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.zone.dao.ZoneDao;
import com.kekeinfo.core.business.zone.model.Zone;
import com.kekeinfo.core.business.zone.model.Zone_;

@Service("zoneService")
public class ZoneServiceImpl extends KekeinfoEntityServiceImpl<Long, Zone> implements
		ZoneService {
	
	private final static String ZONE_CACHE_PREFIX = "ZONES_";

	private ZoneDao zoneDao;
	
	@Autowired
	private CacheUtils cache;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZoneServiceImpl.class);

	@Autowired
	public ZoneServiceImpl(ZoneDao zoneDao) {
		super(zoneDao);
		this.zoneDao = zoneDao;
	}

	@Override
	public Zone getByCode(String code) {
		return getByField(Zone_.code, code);
	}

	
	
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Zone> getZones() throws ServiceException {
		
		Map<String, Zone> zones = null;
		try {

			String cacheKey = ZONE_CACHE_PREFIX ;
			
			zones = (Map<String, Zone>) cache.getFromCache(cacheKey);

		
		
			if(zones==null) {
				zones = new HashMap<String, Zone>();
				List<Zone> zns = zoneDao.list();
			
				//set names
				for(Zone zone : zns) {
					
					zone.setName(zone.getName());
					zones.put(zone.getCode(), zone);
					
				}
				cache.putInCache(zones, cacheKey);
			}

		} catch (Exception e) {
			LOGGER.error("getZones()", e);
		}
		return zones;
		
		
	}

	@Override
	public List<Zone> getAllZone() throws ServiceException {
		// TODO Auto-generated method stub
		return zoneDao.list();
	}

	@Override
	public List<Object[]> getPinYin() throws ServiceException {
		// TODO Auto-generated method stub
		return zoneDao.getPinYin();
	}

}
