package com.kekeinfo.core.business.daily.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.daily.dao.GuardDailyDao;
import com.kekeinfo.core.business.daily.model.GuardDaily;
import com.kekeinfo.core.business.daily.model.GuardDailyImage;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;

@Service("guarddailyService")
public class GuardDailyServiceImpl extends KekeinfoEntityServiceImpl<Long, GuardDaily> implements GuardDailyService {
	private GuardDailyDao guarddailyDao;
	@Autowired
	private ContentService contentService;
	@Autowired GuardDailyImageService guardDailyImageService;
	@Autowired
	public GuardDailyServiceImpl(GuardDailyDao guarddailyDao) {
		super(guarddailyDao);
		this.guarddailyDao = guarddailyDao;
	}

	@Override
	@Transactional
	public void saveUpdate(GuardDaily gd, String dels) throws ServiceException {
		List<String> delFilenames = new ArrayList<String>();
		//新增图片
		Set<GuardDailyImage> images = gd.getGuardDailyImages();
		if(images!=null && images.size()>0) {
			for(GuardDailyImage image : images) {
				if(image.getImage()!=null && (image.getId()==null || image.getId()==0L)) {//新增图片
			        InputStream inputStream = image.getImage();
			        InputContentFile cmsContentImage = new InputContentFile();
			        cmsContentImage.setFileName( image.getDailyImage());
			        cmsContentImage.setFile( inputStream );
			        cmsContentImage.setFileContentType(FileContentType.GUARD_DAILY);
			        contentService.addContentFile(cmsContentImage);
			        if(image.getJdigital()!=null){
						InputContentFile cmsContentFilej = new InputContentFile();
						cmsContentFilej.setFileName(image.getJpeg());
						cmsContentFilej.setFileContentType( FileContentType.GUARD_DAILY );
						cmsContentFilej.setFile(image.getJdigital() );
						contentService.addContentFile(cmsContentFilej);
					}
				}
			}
		}
		//记录删除的图片
		if(StringUtils.isNotBlank(dels)){
			if(gd.getId()!=null){
				List<GuardDailyImage> gims = guardDailyImageService.getByGid(gd.getId());
				if(gims!=null && gims.size()>0){
					String[] delid = dels.split("#");
					for(GuardDailyImage gi:gims){
						boolean isadd=true;
						for (int i = 0; i < delid.length; i++) {
							if(gi.getId().equals(Long.parseLong(delid[i]))){
								delFilenames.add(gi.getDailyImage());
								isadd=false;
								break;
							}
						}
						if(isadd){
							gd.getGuardDailyImages().add(gi);
						}
					}
				}
			}
		}
		
		super.saveOrUpdate(gd);
		//删除不要的日志图片
		if (delFilenames.size()>0) {
			for (int i = 0; i < delFilenames.size(); i++) {
				contentService.removeFile(FileContentType.GUARD_DAILY, delFilenames.get(i));
			}
		}// TODO Auto-generated method stub
		
	}

	@Override
	public GuardDaily wihtimg(Long gid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.guarddailyDao.wihtimg(gid);
	}

	@Transactional
	@Override
	public void deletewithimg(Long gid) throws ServiceException {
		// TODO Auto-generated method stub
		GuardDaily dg =this.wihtimg(gid);
		Set<GuardDailyImage> gims =dg.getGuardDailyImages();
		if(gims!=null && gims.size()>0){
			for(GuardDailyImage gm:gims){
				contentService.removeFile(FileContentType.GUARD_DAILY, gm.getDailyImage());
			}
		}
		super.delete(dg);
	}

	@Override
	public GuardDaily byDate(Date date,Long gid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.guarddailyDao.byDate(date,gid);
	}
}
