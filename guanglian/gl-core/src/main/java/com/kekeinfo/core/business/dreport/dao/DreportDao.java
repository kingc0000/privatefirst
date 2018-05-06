package com.kekeinfo.core.business.dreport.dao;

import java.util.List;

import com.kekeinfo.core.business.dreport.model.Dreport;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;


public interface DreportDao extends KekeinfoEntityDao<Long,Dreport> {	

	/**
	 * 获取指定用户的报告集合
	 * @param uid 用户id，如果为空，则不做限制
	 * @param types 报告状态数组，如果为空，则不做限制
	 * @return
	 */
	List<Dreport> getApproveDreports(Long uid, Integer[] types);
	
	/**
	 * 获取待办事项数量
	 * @param uid
	 * @param types
	 * @return
	 */
	public int countApproveDreports(Long uid, Integer[] types);
	
	public int countApproveDreports(String uname, Integer[] types);
}
