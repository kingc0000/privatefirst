package com.kekeinfo.core.business.sign.service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.image.service.ImageService;
import com.kekeinfo.core.business.sign.model.Sign;
import com.kekeinfo.core.business.sign.dao.SignDao;

@Service("signService")
public class SignServiceImpl extends KekeinfoEntityServiceImpl<Long, Sign> implements SignService {
	private SignDao signDao;
	@Autowired
	private ContentService contentService;
	@Autowired ImageService imageService;
	@Autowired
	public SignServiceImpl(SignDao signDao) {
		super(signDao);
		this.signDao = signDao;
	}

	@Override
	public Sign getBySId(Long sid) throws ServiceException {
		// TODO Auto-generated method stub
		return signDao.getBySId(sid);
	}

	@Override
	public List<Sign> getByJIdToday(Long jid) throws ServiceException {
		// TODO Auto-generated method stub
		return signDao.getByJIdToday(jid);
	}

	@Override
	@Transactional
	public void saveNew(Sign sign) throws ServiceException {
		// TODO Auto-generated method stub
		List<Images> imgs =sign.getImages();
		if(imgs!=null && imgs.size()>0){
			for(Images image:imgs){
				imageService.save(image);
				InputStream inputStream = image.getDigital();
		        InputContentFile cmsContentImage = new InputContentFile();
		        cmsContentImage.setFileName( image.getName());
		        cmsContentImage.setFile( inputStream );
		        cmsContentImage.setFileContentType(FileContentType.SIGN);
		        contentService.addContentFile(cmsContentImage);
		        //小图
		        if(image.getJdigital()!=null){
					InputContentFile cmsContentFilej = new InputContentFile();
					cmsContentFilej.setFileName(image.getJpeg());
					cmsContentFilej.setFileContentType( FileContentType.SIGN);
					cmsContentFilej.setFile(image.getJdigital() );
					contentService.addContentFile(cmsContentFilej);
				}
			}
		}
		//签到
		sign.setSattus(1);
		sign.getAuditSection().setDateModified(new Date());
		super.update(sign);
	}

	@Override
	public List<Sign> getByJId(Long jid) throws ServiceException {
		// TODO Auto-generated method stub
		return signDao.getByJId(jid);
	}

	@Override
	public List<Sign> getByUidToday(Long jid) throws ServiceException {
		// TODO Auto-generated method stub
		return signDao.getByUidToday(jid);
	}

	@Override
	public List<Sign> getByUidTodayWithGuard(Long jid) throws ServiceException {
		// TODO Auto-generated method stub
		return signDao.getByUidTodayWithGuard(jid);
	}

	@Override
	public Sign withImg(Long jid) throws ServiceException {
		// TODO Auto-generated method stub
		return signDao.withImg(jid);
	}
}
