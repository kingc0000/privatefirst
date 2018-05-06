package com.kekeinfo.core.business.preview.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.appmessage.model.AMessage;
import com.kekeinfo.core.business.appmessage.model.MessageU;
import com.kekeinfo.core.business.appmessage.service.AMessageService;
import com.kekeinfo.core.business.appmessage.service.MessageUService;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.preview.dao.PreviewDao;
import com.kekeinfo.core.business.preview.model.Preview;
import com.kekeinfo.core.business.preview.model.PreviewImage;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.utils.MessageTypeEnum;

@Service
public class PreviewServiceImpl extends KekeinfoEntityServiceImpl<Long, Preview> implements
		PreviewService {
	
	private PreviewDao previewDao;
	@Autowired
	private ContentService contentService;
	@Autowired
	private AMessageService aMessageService;
	@Autowired
	private MessageUService messageUService;
	
	@Autowired
	public PreviewServiceImpl(PreviewDao previewDao) {
		super(previewDao);
		this.previewDao = previewDao;
	}
	
	public void save(Preview review, String delImageIds,List<Long> uid,String pname) throws ServiceException {
		Set<PreviewImage> newimgs=review.getPreviewImages();
		
		//先新增imags
		if(newimgs!=null && newimgs.size()>0){
			for(PreviewImage img:newimgs){
				InputContentFile cmsContentFile = new InputContentFile();
				cmsContentFile.setFileName(img.getReviewImage());
				cmsContentFile.setFileContentType(FileContentType.PREVIEW );
				cmsContentFile.setFile(img.getImage());
				contentService.addContentFile(cmsContentFile);
			}
		}
		if(review.getId()==null){
			super.save(review);
		}else{
			Preview db = this.getById(review.getId());
			Set<PreviewImage> oimgs = db.getPreviewImages();
			if(StringUtils.isNotBlank(delImageIds)){
				String [] diss=delImageIds.split(",");
				for(PreviewImage img:oimgs){
					boolean find = false;
					for(String s:diss){
						//需要删除
						if(img.getId().equals(Long.parseLong(s))){
							find=true;
							break;
						}
					}
					//没有在删除列表中
					if(!find){
						review.getPreviewImages().add(img);
					}
				}
			//没有删除列表要把之前的图片保存起来
			}else{
				for(PreviewImage img:oimgs){
					review.getPreviewImages().add(img);
				}
			}
			
			super.update(review);
		}
		
		//删除文件
		if(StringUtils.isNotBlank(delImageIds)){
			String[] ids = delImageIds.split(",");
			for(String s:ids){
				contentService.removeFile(FileContentType.PREVIEW, s);
			}
		}
		if(uid!=null && uid.size()>0){
			//增加message
			AMessage am =new AMessage();
			am.setDateCreated(new Date());
			am.setTitle(pname+"新评论");
			am.setMessage(pname+"新评论"+review.getComment());
			am.setMtype(MessageTypeEnum.WCommet);
			aMessageService.save(am);
			for(Long ui:uid){
				MessageU mu =new MessageU();
				mu.setMessage(am);
				User user =new User();
				user.setId(ui);
				mu.setUser(user);
				mu.setStatu(0);
				messageUService.save(mu);
			}
		}
		
		
	}
	
	@Override
	public void deleteById(Object id) {
		Preview review = super.getById((Long) id);
		try {
			super.delete(review);
			//删除附件
			Set<PreviewImage> images = review.getPreviewImages();
			for (PreviewImage image : images) {
				contentService.removeFile(FileContentType.PREVIEW, image.getReviewImage());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public List<Object[]> getTotalInfo(Long cid) throws ServiceException {
		return previewDao.getTotalInfo(cid);
	}
	
	/**
	 * 获取项目id数组的评论汇总集合
	 * @param cids
	 * @param limit
	 * @param offset
	 * @return
	 * @throws ServiceException
	 */
	public List<Object[]> getTotalInfo(Set<Long> cids, Integer limit, Integer offset) throws ServiceException {
		return previewDao.getTotalInfo(cids, limit, offset);
	}

	@Override
	public Preview getBypid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return previewDao.getBypid(cid);
	}

	@Override
	public Preview withProject(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return previewDao.withProject(cid);
	}
}
