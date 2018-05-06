package com.kekeinfo.core.business.auditrule.service;


import java.util.List;

import com.kekeinfo.core.business.dreport.model.AuditRule;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;


public interface AuditRuleService extends KekeinfoEntityService<Long, AuditRule> {

	/**
	 * 更新送审规则用户集合信息
	 * @param valueOf
	 * @param add
	 * @return
	 */
	int updateAuditRuleConfig(Long valueOf, List<String> add);

}
