package com.kekeinfo.core.business.zone.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.zone.model.QZone;
import com.kekeinfo.core.business.zone.model.Zone;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("zoneDao")
public class ZoneDaoImpl extends KekeinfoEntityDaoImpl<Long, Zone> implements ZoneDao {
	
	@Override
	public List<Object[]> getPinYin() throws ServiceException {
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select ZONE_ID as id,ZONE_NAME as name,IFNULL(ELT(INTERVAL(CONV(HEX(left(CONVERT(ZONE_NAME USING gbk),1)),16,10), ");
		qs.append("0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),");
		qs.append("'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P',");
		qs.append(" 'Q','R','S','T','W','X','Y','Z'),LEFT(ZONE_NAME,1)) as pinyin  from ZONE order by pinyin,name ");
		
		Query q  =super.getEntityManager().createNativeQuery(qs.toString());
    	
    	@SuppressWarnings("unchecked")
		List<Object[]> counts =  q.getResultList();

    	
    	return counts;
	}
	
	@Override
	public List<Zone> list(){
		QZone qZone = QZone.zone;
		
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qZone);

		List<Zone> zones = query.list(qZone);
		return zones;
	}

}
