package com.kekeinfo.core.business.monitoreqip.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.monitoreqip.model.MpointEquip;
import com.kekeinfo.core.business.monitoreqip.model.QMpointEquip;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("mpointequipDao")
public class MpointEquipDaoImpl extends KekeinfoEntityDaoImpl<Long, MpointEquip> implements MpointEquipDao {
	public MpointEquipDaoImpl() {
		super();
	}

	@Override
	public List<MpointEquip> getByMeid(long meid) {
		QMpointEquip qContact = QMpointEquip.mpointEquip;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.mpoint).fetch()
		.leftJoin(qContact.monitorEqip).fetch()
		.where(qContact.monitorEqip.id.eq(meid));
		List<MpointEquip> cs =query.list(qContact);
		
		return cs;
	}

	@Override
	public List<MpointEquip> getByMtype(String mtype) {
		QMpointEquip qContact = QMpointEquip.mpointEquip;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.mpoint).fetch()
		.leftJoin(qContact.monitorEqip).fetch()
		.where(qContact.mpoint.pType.endsWithIgnoreCase(mtype));
		List<MpointEquip> cs =query.list(qContact);
		
		return cs;
	}
}