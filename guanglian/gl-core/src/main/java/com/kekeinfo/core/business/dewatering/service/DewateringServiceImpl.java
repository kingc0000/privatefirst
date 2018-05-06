package com.kekeinfo.core.business.dewatering.service;

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
import com.kekeinfo.core.business.data.service.DeWellDataService;
import com.kekeinfo.core.business.data.service.HdeWellDataService;
import com.kekeinfo.core.business.dewatering.dao.DewateringDao;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.image.service.WellImageService;
import com.kekeinfo.core.business.monitordata.model.HdewellData;

@Service("dewateringService")
public class DewateringServiceImpl extends KekeinfoEntityServiceImpl<Long, Dewatering> implements DewateringService {
	
	private DewateringDao pumpwellDao;
	
	@Autowired DeWellDataService pWellDataService;
	
	@Autowired HdeWellDataService hpWellDataService;
	
	@Autowired WellImageService imageService;
	
	@Autowired ContentService contentService;
	@Autowired CSiteService cSiteService;
	
	@Autowired
	public DewateringServiceImpl(DewateringDao pumpwellDao) {
		super(pumpwellDao);
		this.pumpwellDao = pumpwellDao;
	}

	@Override
	public Dewatering getByIdWithCSite(Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		return pumpwellDao.getByIdWithCSite(pid);
	}
	
	public Dewatering getByIdWithPointLink(Long pid) throws ServiceException {
		return pumpwellDao.getByIdWithPointLink(pid);
	}

	@Override
	@Transactional
	public void saveStatus(Dewatering pwell) throws ServiceException {
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "desc");
		Entitites<HdewellData> pwellDataList = hpWellDataService.getPageListByAttributes(Arrays.asList("deWell.id"), Arrays.asList(pwell.getId()), 1, 0,  orderby);
		if (pwellDataList.getTotalCount()>0) {
			HdewellData pwellData = pwellDataList.getEntites().get(0);
			pwell.setrFlow(pwellData.getFlow());
			pwell.setrWater(pwellData.getWater());
			pwell.setLastAccu(pwellData.getLastAccu());
			pwell.setThisAccu(pwellData.getThisAccu());
			pwell.setLastDate(pwellData.getAuditSection().getDateCreated());
			int laststatus =pwell.getDataStatus();
			pwell.updateDataStatusByData(pwell.getrFlow(), pwell.getrWater()); //更新状态
			if(laststatus==0){
				//++
				if(pwell.getDataStatus()>0){
					ConstructionSite scite = pwell.getcSite();
					scite.setRunstatus(scite.getRunstatus()+1);
					cSiteService.update(scite);
				}
			}else{
				//--
				if(pwell.getDataStatus()==0){
					ConstructionSite scite = pwell.getcSite();
					int runstatus=scite.getRunstatus();
					if(runstatus>0){
						scite.setRunstatus(scite.getRunstatus()-1);
						cSiteService.update(scite);
					}
				}
			}
			super.saveOrUpdate(pwell);
		} 
	}

	@Override
	public List<Dewatering> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.pumpwellDao.getWarningData(ids);
	}

	@Override
	public List<Dewatering> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException {
		// TODO Auto-generated method stub
		return pumpwellDao.getListByPidAndPowerStatus(pid, status);
	}

	@Transactional
	@Override
	public void saveOrUpdate(Dewatering pwell, String delids) throws ServiceException {
		// TODO Auto-generated method stub
		List<Wellimages> newimgs=pwell.getPointInfo().getImages();
		//先新增imags
		if(newimgs!=null && newimgs.size()>0){
			for(Wellimages img:newimgs){
				imageService.save(img);
				InputContentFile cmsContentFile = new InputContentFile();
				cmsContentFile.setFileName(img.getName());
				cmsContentFile.setFileContentType( FileContentType.DEWELL_DIGITAL );
				cmsContentFile.setFile(img.getDigital() );
				contentService.addContentFile(cmsContentFile);
				if(img.getJdigital()!=null){
					InputContentFile cmsContentFilej = new InputContentFile();
					cmsContentFilej.setFileName(img.getJpeg());
					cmsContentFilej.setFileContentType( FileContentType.DEWELL_DIGITAL );
					cmsContentFilej.setFile(img.getJdigital() );
					contentService.addContentFile(cmsContentFilej);
				}
			}
		}
		if(pwell.getId()==null){
			super.save(pwell);
		}else{
			Dewatering db = this.getById(pwell.getId());
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
						pwell.getPointInfo().getImages().add(img);
					}
				}
			//没有删除列表要把之前的图片保存起来
			}else{
				for(Wellimages img:oimgs){
					pwell.getPointInfo().getImages().add(img);
				}
			}
			//处理pointlink
			if (db.getPointLink()!=null) {
				pwell.setPointLink(db.getPointLink());
			}
			super.update(pwell);
		}
		
		//添加新的文件
		if(StringUtils.isNotBlank(delids)){
			String[] ids = delids.split(",");
			for(String s:ids){
				//对应的数据记录也要删除
				Wellimages img = imageService.getById(Long.parseLong(s));
				imageService.delete(img);
				contentService.removeFile(FileContentType.DEWELL_DIGITAL, s);
			}
		}
	}
	
	@Transactional
	@Override
	public void remove(Dewatering pwell) throws ServiceException {
		// TODO Auto-generated method stub
		List<Wellimages> imgs=pwell.getPointInfo().getImages();
		for(Wellimages s:imgs){
			//对应的数据记录也要删除
			imageService.delete(s);
			contentService.removeFile(FileContentType.DEWELL_DIGITAL, s.getName());
		}
		if(pwell.getDataStatus()>0){
			ConstructionSite csite = pwell.getcSite();
			int runstauts = csite.getRunstatus();
			if(runstauts>0){
				csite.setRunstatus(runstauts-1);
				cSiteService.update(csite);
			}
		}
		super.delete(pwell);
	}
	
	@Override
	public void deleteById(Object id) throws ServiceException {
		Dewatering well = pumpwellDao.getById((Long) id);
		List<Wellimages> imgs= well.getPointInfo().getImages();
		for(Wellimages s:imgs){
			//对应的数据记录也要删除
			imageService.delete(s);
			contentService.removeFile(FileContentType.PWELL_DIGITAL, s.getName());
		}
		if(well.getDataStatus()>0){
			ConstructionSite csite = well.getcSite();
			int runstauts = csite.getRunstatus();
			if(runstauts>0){
				csite.setRunstatus(runstauts-1);
				cSiteService.update(csite);
			}
		}
		super.delete(well);
	}

	@Override
	public List<Dewatering> getByCid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.pumpwellDao.getByCid(cid);
	}

	@Transactional
	@Override
	public void saveQcode(Dewatering pwell) throws ServiceException {
		
			InputContentFile cmsContentFile = new InputContentFile();
			cmsContentFile.setFileName(pwell.getqCode());
			cmsContentFile.setFileContentType( FileContentType.QCODE );
			cmsContentFile.setFile(pwell.getDigital() );
			contentService.addContentFile(cmsContentFile);
			this.update(pwell);
	}

	@Override
	public List<Dewatering> getByIds(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.pumpwellDao.getByIds(ids);
	}
}
