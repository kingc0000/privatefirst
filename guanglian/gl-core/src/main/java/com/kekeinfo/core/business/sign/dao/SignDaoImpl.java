package com.kekeinfo.core.business.sign.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.sign.model.QSign;
import com.kekeinfo.core.business.sign.model.Sign;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("signDao")
public class SignDaoImpl extends KekeinfoEntityDaoImpl<Long, Sign> implements SignDao {
	public SignDaoImpl() {
		super();
	}

	@Override
	public Sign getBySId(Long sid) {
		QSign qContact = QSign.sign;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.images).fetch()
		.leftJoin(qContact.gjob).fetch()
		.where(qContact.id.eq(sid));
		List<Sign> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}

	@Override
	public List<Sign> getByJIdToday(Long jid) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date to =calendar.getTime();

		QSign qContact = QSign.sign;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.gjob)
		.where(qContact.gjob.id.eq(jid).and(qContact.auditSection.dateCreated.between(zero, to)));
		List<Sign> cs =query.list(qContact);
		return cs;
	}

	@Override
	public List<Sign> getByJId(Long jid) {

		QSign qContact = QSign.sign;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.gjob)
		.where(qContact.gjob.id.eq(jid));
		List<Sign> cs =query.list(qContact);
		return cs;
	}

	@Override
	public List<Sign> getByUidToday(Long jid) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date to =calendar.getTime();

		QSign qContact = QSign.sign;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.where(qContact.user.id.eq(jid).and(qContact.shouleBe.between(zero, to)));
		List<Sign> cs =query.list(qContact);
		return cs;
	}

	@Override
	public List<Sign> getByUidTodayWithGuard(Long jid) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date to =calendar.getTime();

		QSign qContact = QSign.sign;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.guard).fetch()
		.where(qContact.user.id.eq(jid).and(qContact.auditSection.dateCreated.between(zero, to)));
		List<Sign> cs =query.list(qContact);
		return cs;
	}

	@Override
	public Sign withImg(Long jid) {
		QSign qContact = QSign.sign;
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qContact)
		.leftJoin(qContact.images).fetch()
		.where(qContact.id.eq(jid));
		List<Sign> cs =query.list(qContact);
		if(cs!=null && cs.size()>0){
			return cs.get(0);
		}
		return null;
	}
}