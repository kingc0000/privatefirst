package com.kekeinfo.core.business.invertedwell.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.data.service.HiWellDataService;
import com.kekeinfo.core.business.data.service.IWellDataService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.image.service.WellImageService;
import com.kekeinfo.core.business.invertedwell.dao.InvertedwellDao;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.monitordata.model.HiwellData;

@Service("invertedwellService")
public class InvertedwellServiceImpl extends KekeinfoEntityServiceImpl<Long, Invertedwell> implements InvertedwellService {
	
	private InvertedwellDao invertedwellDao;
	
	@Autowired IWellDataService iWellDataService;
	
	@Autowired HiWellDataService hiWellDataService;
	@Autowired WellImageService imageService;
	
	@Autowired ContentService contentService;
	@Autowired CSiteService cSiteService;
	
	@Autowired
	public InvertedwellServiceImpl(InvertedwellDao invertedwellDao) {
		super(invertedwellDao);
		this.invertedwellDao = invertedwellDao;
	}

	@Override
	public Invertedwell getByIdWithCSite(Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		return invertedwellDao.getByIdWithCSite(pid);
	}
	
	public Invertedwell getByIdWithPointLink(Long pid) throws ServiceException{
		return invertedwellDao.getByIdWithPointLink(pid);
	}

	@Override
	@Transactional
	public void saveStatus(Invertedwell iwell) throws ServiceException {
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "desc");
		Entitites<HiwellData> iwellDataList = hiWellDataService.getPageListByAttributes(Arrays.asList("iWell.id"), Arrays.asList(iwell.getId()), 1, 0,  orderby);
		if (iwellDataList.getTotalCount()>0) {
			HiwellData iwellData = iwellDataList.getEntites().get(0);
			iwell.setrFlow(iwellData.getFlow());
			iwell.setrPressure(iwellData.getPressure());
			iwell.setLastAccu(iwellData.getLastAccu());
			iwell.setThisAccu(iwellData.getThisAccu());
			iwell.setLastDate(iwellData.getAuditSection().getDateCreated());
			int laststatus=iwell.getDataStatus();
			iwell.updateDataStatusByData(iwell.getrFlow(), iwell.getrPressure());
			if(laststatus==0){
				//++
				if(iwell.getDataStatus()>0){
					ConstructionSite scite = iwell.getcSite();
					scite.setRunstatus(scite.getRunstatus()+1);
					cSiteService.update(scite);
				}
			}else{
				//--
				if(iwell.getDataStatus()==0){
					ConstructionSite scite = iwell.getcSite();
					int runstatus=scite.getRunstatus();
					if(runstatus>0){
						scite.setRunstatus(scite.getRunstatus()-1);
						cSiteService.update(scite);
					}
				}
			}
			super.saveOrUpdate(iwell);
		}
	}

	@Override
	public List<Invertedwell> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.invertedwellDao.getWarningData(ids);
	}
	
	/**
	 * 根据项目id和抽水井状态，获取抽水井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	public List<Invertedwell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException {
		return invertedwellDao.getListByPidAndPowerStatus(pid, status);
	}

	@Transactional
	@Override
	public void saveOrUpdate(Invertedwell iwell, String delids) throws ServiceException {
		List<Wellimages> newimgs=iwell.getPointInfo().getImages();
		//先新增imags
		if(newimgs!=null && newimgs.size()>0){
			for(Wellimages img:newimgs){
				imageService.save(img);
				InputContentFile cmsContentFile = new InputContentFile();
				cmsContentFile.setFileName(img.getName());
				cmsContentFile.setFileContentType( FileContentType.IWELL_DIGITAL );
				cmsContentFile.setFile(img.getDigital() );
				contentService.addContentFile(cmsContentFile);
				if(img.getJdigital()!=null){
					InputContentFile cmsContentFilej = new InputContentFile();
					cmsContentFilej.setFileName(img.getJpeg());
					cmsContentFilej.setFileContentType( FileContentType.IWELL_DIGITAL );
					cmsContentFilej.setFile(img.getJdigital() );
					contentService.addContentFile(cmsContentFilej);
				}
			}
		}
		if(iwell.getId()==null){
			super.save(iwell);
		}else{
			Invertedwell db = this.getById(iwell.getId());
			List<Wellimages> oimgs = db.getPointInfo().getImages();
			if(StringUtils.isNotBlank(delids)){
				String [] diss=delids.split(",");
				for(Wellimages img:oimgs){
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
						iwell.getPointInfo().getImages().add(img);
					}
				}
			//没有删除列表要把之前的图片保存起来
			}else{
				for(Wellimages img:oimgs){
					iwell.getPointInfo().getImages().add(img);
				}
			}
			//处理pointlink
			if (db.getPointLink()!=null) {
				iwell.setPointLink(db.getPointLink());
			}
			super.update(iwell);
		}
		
		//添加新的文件
		if(StringUtils.isNotBlank(delids)){
			String[] ids = delids.split(",");
			for(String s:ids){
				//对应的数据记录也要删除
				Wellimages img = imageService.getById(Long.parseLong(s));
				imageService.delete(img);
				contentService.removeFile(FileContentType.IWELL_DIGITAL, s);
			}
		}
		
	}

	@Transactional
	@Override
	public void remove(Invertedwell iwell) throws ServiceException {
		List<Wellimages> imgs=iwell.getPointInfo().getImages();
		for(Wellimages s:imgs){
			//对应的数据记录也要删除
			imageService.delete(s);
			contentService.removeFile(FileContentType.IWELL_DIGITAL, s.getName());
		}
		if(iwell.getDataStatus()>0){
			ConstructionSite csite = iwell.getcSite();
			int runstatus = csite.getRunstatus();
			if(runstatus>0){
				csite.setRunstatus(runstatus-1);
				cSiteService.update(csite);
			}
		}
		
		super.delete(iwell);
		
	}
	
	@Override
	public void deleteById(Object id) throws ServiceException {
		Invertedwell well = invertedwellDao.getById((Long) id);
		List<Wellimages> imgs= well.getPointInfo().getImages();
		for(Wellimages s:imgs){
			//对应的数据记录也要删除
			imageService.delete(s);
			contentService.removeFile(FileContentType.PWELL_DIGITAL, s.getName());
		}
		if(well.getDataStatus()>0){
			ConstructionSite csite = well.getcSite();
			int runstatus = csite.getRunstatus();
			if(runstatus>0){
				csite.setRunstatus(runstatus-1);
				cSiteService.update(csite);
			}
		}
		super.delete(well);
	}

	@Override
	public List<Invertedwell> getBycid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return invertedwellDao.getBycid(cid);
	}

	@Transactional
	@Override
	public void saveQcode(Invertedwell iwell) throws ServiceException {
		// TODO Auto-generated method stub
		InputContentFile cmsContentFile = new InputContentFile();
		cmsContentFile.setFileName(iwell.getqCode());
		cmsContentFile.setFileContentType( FileContentType.QCODE );
		cmsContentFile.setFile(iwell.getDigital() );
		contentService.addContentFile(cmsContentFile);
		this.update(iwell);
	}

	@Override
	public List<Invertedwell> getByIds(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.invertedwellDao.getByIds(ids);
	}
}
