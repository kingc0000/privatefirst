package com.kekeinfo.core.business.auditrule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.auditrule.dao.AuditRuleDao;
import com.kekeinfo.core.business.dreport.model.AuditRule;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;

@Service("auditRuleService")
public class AuditRuleServiceImpl extends KekeinfoEntityServiceImpl<Long, AuditRule> implements
		AuditRuleService {
	
	private AuditRuleDao auditRuleDao;
	
	@Autowired
	public AuditRuleServiceImpl(AuditRuleDao auditRuleDao) {
		super(auditRuleDao);
		this.auditRuleDao = auditRuleDao;
	}

	@Override
	public int updateAuditRuleConfig(Long rid, List<String> add) {
		//先清空采用该送审规则组的用户
		StringBuilder remove = new StringBuilder("DELETE FROM AUDIT_USER ").append(" WHERE AUDIT_RULE_ID=").append(rid).append(" ");
		int removeSize = auditRuleDao.excuteBySql(remove.toString());
		if(removeSize==-1){
			return -1;
		}
		
		//更新采用该送审规则组的用户
		if (add!=null && add.size()>0) {
			StringBuilder insert = new StringBuilder("INSERT INTO AUDIT_USER(AUDIT_RULE_ID, USER_ID) VALUES ");
			for (String pid : add) {
				insert.append("(").append(rid).append(",").append(pid).append("),");
			}
			String insertSql = insert.substring(0, insert.length()-1) + ";";
			
			int insertSize = auditRuleDao.excuteBySql(insertSql);
			return insertSize;
		} else {
			return 0;
		}
	}
	
}
