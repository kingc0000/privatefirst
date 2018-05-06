package com.kekeinfo.core.business.dreport.service;


import java.util.List;

import com.kekeinfo.core.business.dreport.model.Dreport;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;


public interface DreportService extends KekeinfoEntityService<Long, Dreport> {

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
	
	/**
	 * 保存报告信息，同时根据前端传过来的附件id删除附件图片
	 * @param daily
	 * @param dels
	 * @throws ServiceException
	 */
	void save(Dreport report, String dels) throws ServiceException;

}
