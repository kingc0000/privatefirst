package com.kekeinfo.core.business.preview.service;


import java.util.List;
import java.util.Set;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;
import com.kekeinfo.core.business.preview.model.Preview;


public interface PreviewService extends KekeinfoEntityService<Long, Preview> {

	/**
	 * 保存评论信息，同时根据前端传过来的评论图片id删除
	 * @param review
	 * @param dels
	 * @throws ServiceException
	 */
	void save(Preview review, String dels,List<Long> uid,String pname ) throws ServiceException;
	/**
	 * 获取指定项目id的评论汇总
	 * @param cid
	 * @return 返回list数组， object[] = {项目id，项目名称，评价总分，评分维度1平均值，评分维度2平均值，评分维度3平均值，评分维度4平均值}
	 * @throws ServiceException
	 */
	public List<Object[]> getTotalInfo(Long cid) throws ServiceException;
	
	/**
	 * 获取项目id数组的评论汇总集合
	 * @param cids
	 * @param limit
	 * @param offset
	 * @return
	 * @throws ServiceException
	 */
	public List<Object[]> getTotalInfo(Set<Long> cids, Integer limit, Integer offset) throws ServiceException;
	
	public Preview getBypid(Long cid) throws ServiceException;
	
	public Preview withProject(Long cid) throws ServiceException;
}
