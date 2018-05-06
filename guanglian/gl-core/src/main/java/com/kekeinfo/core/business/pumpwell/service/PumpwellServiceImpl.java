package com.kekeinfo.core.business.pumpwell.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.kekeinfo.core.business.data.service.HpWellDataService;
import com.kekeinfo.core.business.data.service.PWellDataService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.image.service.WellImageService;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.monitordata.model.WowellData;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.pointinfo.model.ObserveInfo;
import com.kekeinfo.core.business.pointinfo.model.PumpInfo;
import com.kekeinfo.core.business.pointlink.model.ObserveLink;
import com.kekeinfo.core.business.pumpwell.dao.PumpwellDao;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.constants.Constants;

@Service("pumpwellService")
public class PumpwellServiceImpl extends KekeinfoEntityServiceImpl<Long, Pumpwell> implements PumpwellService {
	
	private PumpwellDao pumpwellDao;
	
	@Autowired ObservewellService observewellService;
	
	@Autowired PWellDataService pWellDataService;
	
	@Autowired HpWellDataService hpWellDataService;
	
	@Autowired WellImageService imageService;
	
	@Autowired ContentService contentService;
	
	@Autowired CSiteService cSiteService;
	
	@Autowired
	public PumpwellServiceImpl(PumpwellDao pumpwellDao) {
		super(pumpwellDao);
		this.pumpwellDao = pumpwellDao;
	}

	@Override
	public Pumpwell getByIdWithCSite(Long pid) throws ServiceException {
		return pumpwellDao.getByIdWithCSite(pid);
	}
	@Override
	public Pumpwell getByIdWithPointLink(Long pid) throws ServiceException {
		return pumpwellDao.getByIdWithPointLink(pid);
	}

	@Transactional
	@Override
	public void saveStatus(Pumpwell pwell) throws ServiceException {
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "desc");
		Entitites<HpwellData> pwellDataList = hpWellDataService.getPageListByAttributes(Arrays.asList("pWell.id"), Arrays.asList(pwell.getId()), 1, 0,  orderby);
		if (pwellDataList.getTotalCount()>0) {
			HpwellData pwellData = pwellDataList.getEntites().get(0);
			pwell.setrFlow(pwellData.getFlow());
			pwell.setrWater(pwellData.getWater());
			pwell.setLastAccu(pwellData.getLastAccu());
			pwell.setThisAccu(pwellData.getThisAccu());
			pwell.setLastDate(pwellData.getAuditSection().getDateCreated());
			int laststatus = pwell.getDataStatus();
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
	public List<Pumpwell> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.pumpwellDao.getWarningData(ids);
	}

	@Override
	public List<Pumpwell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException {
		// TODO Auto-generated method stub
		return pumpwellDao.getListByPidAndPowerStatus(pid, status);
	}

	@Transactional
	@Override
	public void saveOrUpdate(Pumpwell pwell, String delids) throws ServiceException {
		// TODO Auto-generated method stub
		List<Wellimages> newimgs=pwell.getPointInfo().getImages();
		//先新增imags
		if(newimgs!=null && newimgs.size()>0){
			for(Wellimages img:newimgs){
				imageService.save(img);
				InputContentFile cmsContentFile = new InputContentFile();
				cmsContentFile.setFileName(img.getName());
				cmsContentFile.setFileContentType( FileContentType.PWELL_DIGITAL );
				cmsContentFile.setFile(img.getDigital() );
				contentService.addContentFile(cmsContentFile);
				if(img.getJdigital()!=null){
					InputContentFile cmsContentFilej = new InputContentFile();
					cmsContentFilej.setFileName(img.getJpeg());
					cmsContentFilej.setFileContentType( FileContentType.PWELL_DIGITAL );
					cmsContentFilej.setFile(img.getJdigital() );
					contentService.addContentFile(cmsContentFilej);
				}
			}
		}
		if(pwell.getId()==null){
			super.save(pwell);
		}else{
			Pumpwell db = this.getById(pwell.getId());
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
				contentService.removeFile(FileContentType.PWELL_DIGITAL, s);
			}
		}
	}
	
	@Transactional
	@Override
	public void remove(Pumpwell pwell) throws ServiceException {
		// TODO Auto-generated method stub
		List<Wellimages> imgs=pwell.getPointInfo().getImages();
		for(Wellimages s:imgs){
			//对应的数据记录也要删除
			imageService.delete(s);
			contentService.removeFile(FileContentType.PWELL_DIGITAL, s.getName());
		}
		if(pwell.getDataStatus()>0){
			ConstructionSite csite =pwell.getcSite();
			int runstauts =csite.getRunstatus();
			if(runstauts>0){
				csite.setRunstatus(runstauts-1);
				cSiteService.update(csite);
			}
		}
		super.delete(pwell);
	}
	
	@Override
	public void deleteById(Object id) throws ServiceException {
		Pumpwell pwell = pumpwellDao.getById((Long) id);
		List<Wellimages> imgs=pwell.getPointInfo().getImages();
		for(Wellimages s:imgs){
			//对应的数据记录也要删除
			imageService.delete(s);
			contentService.removeFile(FileContentType.PWELL_DIGITAL, s.getName());
		}
		if(pwell.getDataStatus()>0){
			ConstructionSite csite =pwell.getcSite();
			int runstauts =csite.getRunstatus();
			if(runstauts>0){
				csite.setRunstatus(runstauts-1);
				cSiteService.update(csite);
			}
		}
		super.delete(pwell);
	}
	
	/**
	 * 获取指定网关的所有抽水井测点，如果网关id为null，则获取所有网关不为空的抽水井测点
	 * @param gateway
	 * @return
	 * @throws ServiceException
	 */
	public List<Pumpwell> getListByGateway(Long gateway) throws ServiceException {
		return pumpwellDao.getListByGateway(gateway);
	}

	@Override
	public List<Pumpwell> getByCid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return pumpwellDao.getByCid(cid);
	}

	/**
	 * 将对应抽水井转成观测井
	 * 复制基本信息，图片，采集配置，采集数据
	 * 删除抽水井基本信息，图片，采集配置，采集数据
	 * @param wid 井ID
	 */
	@Override
	public int switchWell(Long wid) throws ServiceException {
		Pumpwell well = (Pumpwell) pumpwellDao.getById(wid);
		
		if (well!=null) {
			Observewell owell = new Observewell();
			try {
				PropertyUtils.copyProperties(owell, well);
				owell.setWaterMeasurement(well.getWater());
				owell.setId(null);
				
				//测点信息
				PumpInfo pinfo = well.getPointInfo();
				ObserveInfo pointInfo = new ObserveInfo();
				PropertyUtils.copyProperties(pointInfo, pinfo);
				pointInfo.setId(null);
				pointInfo.setPoint(owell);
				owell.setPointInfo(pointInfo);
				
				//图片
				if (!pinfo.getImages().isEmpty()) {
					List<Wellimages> images = new ArrayList<Wellimages>();
					List<Wellimages> oldimages = pinfo.getImages();
					for (Wellimages oldimage : oldimages) {
						Wellimages image = new Wellimages();
						PropertyUtils.copyProperties(image, oldimage);
//						imageService.delete(oldimage); //删除旧照片
						image.setId(null);
						imageService.save(image);
						images.add(image);
					}
					pointInfo.setImages(images);
				} else {
					pointInfo.setImages(null);
				}
				
				//采集配置
				if (well.getPointLink()!=null) {
					ObserveLink link = new ObserveLink();
					//水位
					link.setChannel1(well.getPointLink().getChannel2());
					link.setNode1(well.getPointLink().getNode2());
					link.setGateway(well.getPointLink().getGateway());
					link.setPoint(owell);
					owell.setPointLink(link);
				}
				observewellService.save(owell);
				
				//删除well信息，配置关系
				//处理图片
				if (!pinfo.getImages().isEmpty()) {
					List<Wellimages> oldimages = pinfo.getImages();
					for (Wellimages oldimage : oldimages) {
						InputStream s = contentService.getContentFileInputstream(FileContentType.PWELL_DIGITAL, oldimage.getName());
						InputStream sj = contentService.getContentFileInputstream(FileContentType.PWELL_DIGITAL, oldimage.getJpeg()); //压缩图片
						
						if (s!=null) {
							InputContentFile cmsContentFile = new InputContentFile();
							cmsContentFile.setFileName(oldimage.getName());
							cmsContentFile.setFileContentType( FileContentType.OWELL_DIGITAL);
							cmsContentFile.setFile(s);
							contentService.addContentFile(cmsContentFile); //新增
							contentService.removeFile(FileContentType.PWELL_DIGITAL, oldimage.getName()); //删除
						}
						if(sj!=null){
							InputContentFile cmsContentFilej = new InputContentFile();
							cmsContentFilej.setFileName(oldimage.getJpeg());
							cmsContentFilej.setFileContentType( FileContentType.OWELL_DIGITAL );
							cmsContentFilej.setFile(sj);
							contentService.addContentFile(cmsContentFilej); //新增
							contentService.removeFile(FileContentType.PWELL_DIGITAL, oldimage.getJpeg());//删除
						}
					}
				}
				//处理采集数据
				List<String> attributes = new ArrayList<String>();
				attributes.add("FLOW");
				attributes.add("FLOWTHRESHOLD");
				attributes.add("WATER");
				attributes.add("WATERTHRESHOLD");
				attributes.add("WATERTDOWN");
				attributes.add("TEMPERATURE");
				attributes.add("TEMPERATURETHRESHOLD");
				attributes.add("STATUS");
				attributes.add("LASTACCU");
				attributes.add("THISACCU");
				attributes.add("ACCUPERIOD");
				attributes.add("AUTO");
				attributes.add("DATE_CREATED");
				attributes.add("DATE_MODIFIED");
				attributes.add("UPDT_ID");
				
				List<Object[]> hpwelldataList = pumpwellDao.getBySql(attributes, "HPWELLDATA", "where PWELL_ID="+well.getId());
				
				if (!hpwelldataList.isEmpty()) {
					List<HowellData> howellDataList = new ArrayList<HowellData>();
					for (int i = 0; i < hpwelldataList.size(); i++) {
						if (i>0&&i%Constants.BATCH_SIZE == 0) {
							//批量新增
							pumpwellDao.batchSaveOwellData(howellDataList);
							howellDataList = new ArrayList<HowellData>();
						}
						HowellData howellData = new HowellData();
						Object[] o = hpwelldataList.get(i);
						howellData.setFlow((BigDecimal) o[0]);
						howellData.setFlowThreshold((BigDecimal) o[1]);
						howellData.setWater((BigDecimal) o[2]);
						howellData.setWaterThreshold((BigDecimal) o[3]);
						howellData.setWaterDwon((BigDecimal) o[4]);
						howellData.setTemperature((BigDecimal) o[5]);
						howellData.setTemperatureThreshold((BigDecimal) o[6]);
						howellData.setStatus((Integer) o[7]);
						howellData.setLastAccu((BigDecimal) o[8]);
						howellData.setThisAccu((BigDecimal) o[9]);
						howellData.setAccuPeriod((BigDecimal) o[10]);
						if (o[11]!=null) {
							howellData.setmAuto((Boolean) o[11]);
						} else {
							howellData.setmAuto(false);
						}
						howellData.getAuditSection().setDateCreated((Date) o[12]);
						howellData.getAuditSection().setDateModified((Date) o[13]);
						howellData.getAuditSection().setModifiedBy((String) o[14]);
						
						howellData.setoWell(owell);
						howellDataList.add(howellData);
					}
					if (howellDataList.size()>0) {
						pumpwellDao.batchSaveOwellData(howellDataList);
					}
					
					//删除抽水井的采集数据
					pumpwellDao.excuteBySql("DELETE FROM HPWELLDATA where PWELL_ID="+well.getId());
					pumpwellDao.excuteBySql("DELETE FROM PWELLDATA where PWELL_ID="+well.getId());
				}
				//处理告警数据
				List<String> warnAttributes = new ArrayList<String>();
				warnAttributes.add("FLOW");
				warnAttributes.add("FLOWTHRESHOLD");
				warnAttributes.add("WATER");
				warnAttributes.add("WATERTHRESHOLD");
				warnAttributes.add("WATERTDOWN");
				warnAttributes.add("TEMPERATURE");
				warnAttributes.add("TEMPERATURETHRESHOLD");
				warnAttributes.add("STATUS");
				warnAttributes.add("DATE_CREATED");
				warnAttributes.add("DATE_MODIFIED");
				warnAttributes.add("UPDT_ID");
				warnAttributes.add("WARNING_TYPE");
				warnAttributes.add("CSITE_ID");
				List<Object[]> wdataList = pumpwellDao.getBySql(warnAttributes, "WARNING_PWELLDATA", "where POINT_ID="+well.getId());
				if (!wdataList.isEmpty()) {
					List<WowellData> wowellDataList = new ArrayList<WowellData>();
					for (int i = 0; i < wdataList.size(); i++) {
						if (i>0&&i%Constants.BATCH_SIZE == 0) {
							//批量新增
							pumpwellDao.batchSaveWowellData(wowellDataList);
							wowellDataList = new ArrayList<WowellData>();
						}
						WowellData wowellData = new WowellData();
						Object[] o = wdataList.get(i);
						wowellData.setFlow((BigDecimal) o[0]);
						wowellData.setFlowThreshold((BigDecimal) o[1]);
						wowellData.setWater((BigDecimal) o[2]);
						wowellData.setWaterThreshold((BigDecimal) o[3]);
						wowellData.setWaterDwon((BigDecimal) o[4]);
						wowellData.setTemperature((BigDecimal) o[5]);
						wowellData.setTemperatureThreshold((BigDecimal) o[6]);
						wowellData.setStatus((Integer) o[7]);
						wowellData.getAuditSection().setDateCreated((Date) o[8]);
						wowellData.getAuditSection().setDateModified((Date) o[9]);
						wowellData.getAuditSection().setModifiedBy((String) o[10]);
						wowellData.setWarningType((int) o[11]);
						
						wowellData.setcSite(well.getcSite());
						wowellData.setPoint(owell);
						wowellDataList.add(wowellData);
					}
					if (wowellDataList.size()>0) {
						pumpwellDao.batchSaveWowellData(wowellDataList);
					}
					
					//删除降水井的告警数据
					pumpwellDao.excuteBySql("DELETE FROM WARNING_PWELLDATA where POINT_ID="+well.getId());
				}
				pumpwellDao.delete(well);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
				return -2;
			}
			
		} else {
			return -1;
		}
		return 0;
	}

	@Transactional
	@Override
	public void saveQcode(Pumpwell pwell) throws ServiceException {
		// TODO Auto-generated method stub
		InputContentFile cmsContentFile = new InputContentFile();
		cmsContentFile.setFileName(pwell.getqCode());
		cmsContentFile.setFileContentType( FileContentType.QCODE );
		cmsContentFile.setFile(pwell.getDigital() );
		contentService.addContentFile(cmsContentFile);
		this.update(pwell);
	}

	@Override
	public List<Pumpwell> getByIds(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.pumpwellDao.getByIds(ids);
	}
}
