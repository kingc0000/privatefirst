package com.kekeinfo.core.business.equipment.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.equipment.model.Equip;
import com.kekeinfo.core.business.equipment.model.QEquip;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("equipDao")
public class EquipDaoImpl extends KekeinfoEntityDaoImpl<Long, Equip> implements EquipDao {
	public EquipDaoImpl() {
		super();
	}

	@Override
	public Equip getByIdWithImg(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		QEquip qContact = QEquip.equip;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.attach).fetch()
		.where(qContact.id.eq(cid));
		List<Equip> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public List<Object[]> getPinYin(String ids) throws ServiceException {
		Date date =new Date();
		 SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		// TODO Auto-generated method stub
		StringBuilder qs = new StringBuilder();
		qs.append("select EQUIPMENT_ID as id,NAME as name,IFNULL(ELT(INTERVAL(CONV(HEX(left(CONVERT(NAME USING gbk),1)),16,10), ");
		qs.append("0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),");
		qs.append("'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P',");
		qs.append(" 'Q','R','S','T','W','X','Y','Z'),LEFT(NAME,1)) as pinyin  from EQUIPMENT ");
		//先不管状态SSTATUS=1 AND 
		qs.append(" where DATE_VAILD>='").append(sdf.format(date)).append("'");
		if(ids !=null){
			qs.append(" AND id in (").append(ids).append(" ) ");
		}
		qs.append(" order by pinyin,name ");
		Query q  =super.getEntityManager().createNativeQuery(qs.toString());
    	
    	@SuppressWarnings("unchecked")
		List<Object[]> counts =  q.getResultList();

    	
    	return counts;
	}	
}