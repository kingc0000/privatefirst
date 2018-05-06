package com.kekeinfo.core.business.auditrule.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.dreport.model.AuditRule;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;


@Repository("auditRuleDataDao")
public class AuditRuleDaoImpl extends KekeinfoEntityDaoImpl<Long, AuditRule> implements AuditRuleDao {
	
}
