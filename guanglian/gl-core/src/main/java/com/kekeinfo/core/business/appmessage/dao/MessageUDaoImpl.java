package com.kekeinfo.core.business.appmessage.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.appmessage.model.MessageU;
import com.kekeinfo.core.business.appmessage.model.QMessageU;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("messageuDao")
public class MessageUDaoImpl extends KekeinfoEntityDaoImpl<Long, MessageU> implements MessageUDao {
	public MessageUDaoImpl() {
		super();
	}

	@Override
	public int read(String uid) {
		QMessageU qBaseData = QMessageU.messageU;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qBaseData).where(qBaseData.user.adminName.endsWithIgnoreCase(uid).and(qBaseData.statu.eq(0)));
		List<MessageU> as = query.list(qBaseData);
		if(as!=null && as.size()>0) return as.size();
		return 0;
	}

	@Override
	public MessageU getByMid(Long mid) {
		QMessageU qBaseData = QMessageU.messageU;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qBaseData)
		.leftJoin(qBaseData.gjob).fetch()
		.where(
				qBaseData.id.eq(mid));
		List<MessageU> as = query.list(qBaseData);
		if(as!=null && as.size()>0) return as.get(0);
		return null;
	}
}