package com.kekeinfo.core.business.daily.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.daily.dao.MonitorDailyDao;
import com.kekeinfo.core.business.daily.model.MonitorDaily;
import com.kekeinfo.core.business.daily.model.MonitorDailyImage;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;

@Service("monitordailyService")
public class MonitorDailyServiceImpl extends KekeinfoEntityServiceImpl<Long, MonitorDaily>
		implements MonitorDailyService {
	private MonitorDailyDao monitordailyDao;
	@Autowired
	private ContentService contentService;

	@Autowired
	public MonitorDailyServiceImpl(MonitorDailyDao monitordailyDao) {
		super(monitordailyDao);
		this.monitordailyDao = monitordailyDao;
	}
	
	public void save(MonitorDaily monitorDaily, String dels) throws ServiceException {
		
		List<String> delFilenames = new ArrayList<String>();
		//新增图片
		Set<MonitorDailyImage> images = monitorDaily.getMonitorDailyImages();
		if(images!=null && images.size()>0) {
			for(MonitorDailyImage image : images) {
				if(image.getImage()!=null && (image.getId()==null || image.getId()==0L)) {//新增图片
			        InputStream inputStream = image.getImage();
			        InputContentFile cmsContentImage = new InputContentFile();
			        cmsContentImage.setFileName( image.getDailyImage());
			        cmsContentImage.setFile( inputStream );
			        cmsContentImage.setFileContentType(FileContentType.MONITOR_DAILY);
			        contentService.addContentFile(cmsContentImage);
			        if(image.getJdigital()!=null){
						InputContentFile cmsContentFilej = new InputContentFile();
						cmsContentFilej.setFileName(image.getJpeg());
						cmsContentFilej.setFileContentType( FileContentType.MONITOR_DAILY );
						cmsContentFilej.setFile(image.getJdigital() );
						contentService.addContentFile(cmsContentFilej);
					}
				}
			}
		}
		//获取数据库的最近一条记录
		
		List<String> attributes = new ArrayList<String>();
		attributes.add("monitor.id");
		List<String> fieldValues = new ArrayList<String>();
		fieldValues.add(monitorDaily.getMonitor().getId().toString());
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("datec", "desc");
		Entitites<MonitorDaily> list  = getPageListByAttributes(attributes, fieldValues, 1, null, orderby);
		MonitorDaily dbDaily = null;
		if(list.getTotalCount()>0){
			dbDaily=list.getEntites().get(0);
			if (DateUtils.isSameDay(monitorDaily.getDatec(), dbDaily.getDatec())) {
				monitorDaily.setId(dbDaily.getId());
				//处理之前的图片
				Set<MonitorDailyImage> imageSet = dbDaily.getMonitorDailyImages();
				for(MonitorDailyImage image:imageSet){
					boolean isadd=true;
					if (!StringUtils.isBlank(dels)) {
						String[] delid = dels.split("#");
						if (delid!=null && delid.length>0) {
							for (int i = 0; i < delid.length; i++) {
								if(image.getId().toString().equals(delid[i])) {
									delFilenames.add(image.getDailyImage());
									isadd=false;
								}
							}
						}
					}
					if(isadd){
						monitorDaily.getMonitorDailyImages().add(image);
					}
				}
			}
		}
		
		super.saveOrUpdate(monitorDaily);
		//删除不要的日志图片
		if (delFilenames.size()>0) {
			for (int i = 0; i < delFilenames.size(); i++) {
				contentService.removeFile(FileContentType.MONITOR_DAILY, delFilenames.get(i));
			}
		}
	}
	
	@Override
	public void deleteById(Object id) {
		MonitorDaily daily = super.getById((Long) id);
		try {
			super.delete(daily);
			//删除附件
			Set<MonitorDailyImage> images = daily.getMonitorDailyImages();
			for (MonitorDailyImage image : images) {
				contentService.removeFile(FileContentType.MONITOR_DAILY, image.getDailyImage());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MonitorDaily getBydate(Date date, long mid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.monitordailyDao.getBydate(date, mid);
	}

	@Override
	public MonitorDaily withImg(Long mdid) throws ServiceException {
		// TODO Auto-generated method stub
		return monitordailyDao.withImg(mdid);
	}
}
