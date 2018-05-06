package com.kekeinfo.core.business.system.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.system.model.SystemNotification;

@Repository("systemNotificationDao")
public class SystemNotificationDaoImpl extends KekeinfoEntityDaoImpl<Long, SystemNotification>
		implements SystemNotificationDao {



}
