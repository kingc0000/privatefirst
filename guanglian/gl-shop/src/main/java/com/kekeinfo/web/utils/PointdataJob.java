package com.kekeinfo.web.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.data.service.PWellDataService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.job.service.GJobService;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.event.SpringContextUtils;

/**
 * 测点数据清理
 * @author sam YongChen
 *
 */
@Component
public class PointdataJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(PointdataJob.class);
	@Autowired PWellDataService pWellDataService;
	@Autowired GJobService gJobService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
//	@Autowired IWellDataService iWellDataService;
//	@Autowired OWellDataService oWellDataService;
//	@Autowired EMonitorDataService eMonitorDataService;
//	@Autowired DeWellDataService deWellDataService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void clearPointdata(){
		//加载刷新时间
		List<BasedataType> rlist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.KEEPTIME);
		Integer keeptime = 7;
		if(rlist!=null){
			keeptime = Integer.valueOf(rlist.get(0).getValue());
		}
		LOGGER.info("开始清理测点当前表过期记录，当前表数据保存周期："+keeptime);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 0-keeptime); //指定删除什么时候的数据
		
		List<String> nativeSqlList = new ArrayList<String>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String deldate = sf.format(calendar.getTime());
		String pwelldataSql = "delete from PWELLDATA where date_created < '" + deldate + "' ";
		String dewelldataSql = "delete from DEWELLDATA where date_created < '" + deldate + "' ";
		String iwelldataSql = "delete from IWELLDATA where date_created < '" + deldate + "' ";
		String owelldataSql = "delete from OWELLDATA where date_created < '" + deldate + "' ";
		String ewelldataSql = "delete from EMONITORDATA where date_created < '" + deldate + "' ";
		nativeSqlList.add(pwelldataSql);
		nativeSqlList.add(dewelldataSql);
		nativeSqlList.add(iwelldataSql);
		nativeSqlList.add(owelldataSql);
		nativeSqlList.add(ewelldataSql);
		
		LOGGER.info("开始清除 "+deldate+" 之前的测点采集数据");
		pWellDataService.excuteByNativeSql(nativeSqlList);
		LOGGER.info("成功清除 "+deldate+" 之前的测点采集数据");
		LOGGER.info("开始清理过期删除的工作安排：");
		try {
			List<GJob> jobs = gJobService.getNoAvaliable(new Date());
			if(jobs!=null && jobs.size()>0){
				for(GJob job:jobs){
					gJobService.delete(job);
				}
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			LOGGER.error("清理过期删除的工作安排失败："+e.getMessage());
		}
		
		LOGGER.info("开始清理用户过期的工作安排：");
		try {
			Map<Long,List<GJob>> ujmaps =  (Map<Long, List<GJob>>) webCacheUtils.getFromCache(Constants.USERGJOBS);
			if(ujmaps!=null && ujmaps.size()>0){
				Map<Long,List<GJob>> newmap =new HashMap<>();
				Date date =new Date();
				Iterator iter = ujmaps.entrySet().iterator();
				while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
					Long uid = (Long)entry.getKey();
					List<GJob> jobs = (List<GJob>)entry.getValue();
					List<GJob> njobs =new ArrayList<>();
					for(GJob job:jobs){
						if(job.getEndDate().after(date)){
							njobs.add(job);
						}
					}
					newmap.put(uid,njobs);
				}
				webCacheUtils.putInCache(Constants.USERGJOBS, newmap);
			}
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
