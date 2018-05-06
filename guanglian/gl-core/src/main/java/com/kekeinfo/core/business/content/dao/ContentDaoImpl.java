package com.kekeinfo.core.business.content.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.content.model.Content;
import com.kekeinfo.core.business.content.model.QContent;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository("contentDao")
public class ContentDaoImpl extends KekeinfoEntityDaoImpl<Long, Content> implements ContentDao {

	public ContentDaoImpl() {
		super();
	}

	@Override
	public List<Content> listByType(String contentType) throws ServiceException {

		QContent qContent = QContent.content;

		JPQLQuery query = new JPAQuery(getEntityManager());

		query.from(qContent).where(qContent.contentType.eq(contentType)).orderBy(qContent.sortOrder.asc());

		List<Content> contents = query.list(qContent);

		return contents;
	}

}
