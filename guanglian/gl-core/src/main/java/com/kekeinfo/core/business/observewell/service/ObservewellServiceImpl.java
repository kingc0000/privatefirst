package com.kekeinfo.core.business.observewell.service;

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
import com.kekeinfo.core.business.data.service.HoWellDataService;
import com.kekeinfo.core.business.data.service.OWellDataService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.image.service.WellImageService;
import com.kekeinfo.core.business.monitordata.model.HowellData;
import com.kekeinfo.core.business.monitordata.model.HpwellData;
import com.kekeinfo.core.business.monitordata.model.WpwellData;
import com.kekeinfo.core.business.observewell.dao.ObservewellDao;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.pointinfo.model.ObserveInfo;
import com.kekeinfo.core.business.pointinfo.model.PumpInfo;
import com.kekeinfo.core.business.pointlink.model.PumpLink;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.constants.Constants;

@Service("observewellService")
public class ObservewellServiceImpl extends KekeinfoEntityServiceImpl<Long, Observewell> implements ObservewellService {
	
	private ObservewellDao observewellDao;
	
	private @Autowired PumpwellService pumpwellService;
	@Autowired OWellDataService oWellDataService;
	@Autowired HoWellDataService hoWellDataService;
	@Autowired WellImageService imageService;
	@Autowired ContentService contentService;
	@Autowired CSiteService cSiteService;
	
	@Autowired
	public ObservewellServiceImpl(ObservewellDao observewellDao) {
		super(observewellDao);
		this.observewellDao = observewellDao;
	}

	@Override
	public Observewell getByIdWithCSite(Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		return observewellDao.getByIdWithCSite(pid);
	}
	
	public Observewell getByIdWithPointLink(Long pid) throws ServiceException{
		return observewellDao.getByIdWithPointLink(pid);
	}

	@Override
	@Transactional
	public void saveStatus(Observewell owell) throws ServiceException {
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "desc");
		Entitites<HowellData> owellDataList = hoWellDataService.getPageListByAttributes(Arrays.asList("oWell.id"), Arrays.asList(owell.getId()), 1, 0,  orderby);
		if (owellDataList.getTotalCount()>0) {
			HowellData owellData = owellDataList.getEntites().get(0);
			
			owell.setrWater(owellData.getWater());
			owell.setrTemperature(owellData.getTemperature());
			owell.setLastDate(owellData.getAuditSection().getDateCreated());
			int laststatus=owell.getDataStatus();
			owell.updateDataStatusByData(owell.getrWater(), owell.getrTemperature());
			if(laststatus==0){
				//++
				if(owell.getDataStatus()>0){
					ConstructionSite scite = owell.getcSite();
					scite.setRunstatus(scite.getRunstatus()+1);
					cSiteService.update(scite);
				}
			}else{
				//--
				if(owell.getDataStatus()==0){
					ConstructionSite scite = owell.getcSite();
					int runstatus=scite.getRunstatus();
					if(runstatus>0){
						scite.setRunstatus(scite.getRunstatus()-1);
						cSiteService.update(scite);
					}
				}
			}
			super.saveOrUpdate(owell);
		}
	}

	@Override
	public List<Observewell> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.observewellDao.getWarningData(ids);
	}

	@Transactional
	@Override
	public void saveOrUpdate(Observewell owell, String delids) throws ServiceException {
		// TODO Auto-generated method stub
				List<Wellimages> newimgs=owell.getPointInfo().getImages();
				//先新增imags
				if(newimgs!=null && newimgs.size()>0){
					for(Wellimages img:newimgs){
						imageService.save(img);
						InputContentFile cmsContentFile = new InputContentFile();
						cmsContentFile.setFileName(img.getName());
						cmsContentFile.setFileContentType( FileContentType.OWELL_DIGITAL );
						cmsContentFile.setFile(img.getDigital() );
						contentService.addContentFile(cmsContentFile);
						if(img.getJdigital()!=null){
							InputContentFile cmsContentFilej = new InputContentFile();
							cmsContentFilej.setFileName(img.getJpeg());
							cmsContentFilej.setFileContentType( FileContentType.OWELL_DIGITAL );
							cmsContentFilej.setFile(img.getJdigital() );
							contentService.addContentFile(cmsContentFilej);
						}
					}
				}
				if(owell.getId()==null){
					super.save(owell);
				}else{
					Observewell db = this.getById(owell.getId());
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
								owell.getPointInfo().getImages().add(img);
							}
						}
					//没有删除列表要把之前的图片保存起来
					}else{
						for(Wellimages img:oimgs){
							owell.getPointInfo().getImages().add(img);
						}
					}
					//处理pointlink
					if (db.getPointLink()!=null) {
						owell.setPointLink(db.getPointLink());
					}
					super.update(owell);
				}
				
				//添加新的文件
				if(StringUtils.isNotBlank(delids)){
					String[] ids = delids.split(",");
					for(String s:ids){
						//对应的数据记录也要删除
						Wellimages img = imageService.getById(Long.parseLong(s));
						imageService.delete(img);
						contentService.removeFile(FileContentType.OWELL_DIGITAL, s);
					}
				}
		
	}

	@Transactional
	@Override
	public void remove(Observewell owell) throws ServiceException {
		// TODO Auto-generated method stub
		List<Wellimages> imgs=owell.getPointInfo().getImages();
		for(Wellimages s:imgs){
			//对应的数据记录也要删除
			imageService.delete(s);
			contentService.removeFile(FileContentType.OWELL_DIGITAL, s.getName());
		}
		if(owell.getDataStatus()>0){
			ConstructionSite csite = owell.getcSite();
			int runstatus = csite.getRunstatus();
			if(runstatus>0){
				csite.setRunstatus(runstatus-1);
				cSiteService.update(csite);
			}
		}
		super.delete(owell);
	}
	
	@Override
	public void deleteById(Object id) throws ServiceException {
		Observewell well = observewellDao.getById((Long) id);
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
	
	/**
	 * 根据项目id和观测井状态，获取观测井集合
	 * @param pid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	public List<Observewell> getListByPidAndPowerStatus(Long pid, Integer[] status) throws ServiceException {
		return observewellDao.getListByPidAndPowerStatus(pid, status); 
	}

	@Override
	public List<Observewell> getBycid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.observewellDao.getBycid(cid);
	}
	
	/**
	 * 将对应观测井转成抽水井
	 * 复制基本信息，图片，采集配置，采集数据
	 * 删除观测井基本信息，图片，采集配置，采集数据
	 * @param wid 井ID
	 */
	@Override
	public int switchWell(Long wid) throws ServiceException {
		Observewell well = (Observewell) observewellDao.getById(wid);
		
		if (well!=null) {
			Pumpwell pwell = new Pumpwell();
			try {
				PropertyUtils.copyProperties(pwell, well);
				pwell.setWater(well.getWaterMeasurement());
				pwell.setId(null);
				
				//测点信息
				ObserveInfo obinfo = well.getPointInfo();
				PumpInfo pointInfo = new PumpInfo();
				PropertyUtils.copyProperties(pointInfo, obinfo);
				pointInfo.setId(null);
				pointInfo.setPoint(pwell);
				pwell.setPointInfo(pointInfo);
				
				//图片
				if (!obinfo.getImages().isEmpty()) {
					List<Wellimages> images = new ArrayList<Wellimages>();
					List<Wellimages> oldimages = obinfo.getImages();
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
					PumpLink link = new PumpLink();
					//水位
					link.setChannel2(well.getPointLink().getChannel1());
					link.setNode2(well.getPointLink().getNode1());
					link.setGateway(well.getPointLink().getGateway());
					link.setPoint(pwell);
					pwell.setPointLink(link);
				}
				pumpwellService.save(pwell);
				
				//删除well信息，配置关系
				//处理图片
				if (!obinfo.getImages().isEmpty()) {
					List<Wellimages> oldimages = obinfo.getImages();
					for (Wellimages oldimage : oldimages) {
						InputStream s = contentService.getContentFileInputstream(FileContentType.OWELL_DIGITAL, oldimage.getName());
						InputStream sj = contentService.getContentFileInputstream(FileContentType.OWELL_DIGITAL, oldimage.getJpeg()); //压缩图片
						
						if (s!=null) {
							InputContentFile cmsContentFile = new InputContentFile();
							cmsContentFile.setFileName(oldimage.getName());
							cmsContentFile.setFileContentType( FileContentType.PWELL_DIGITAL);
							cmsContentFile.setFile(s);
							contentService.addContentFile(cmsContentFile); //新增
							contentService.removeFile(FileContentType.OWELL_DIGITAL, oldimage.getName()); //删除
						}
						if(sj!=null){
							InputContentFile cmsContentFilej = new InputContentFile();
							cmsContentFilej.setFileName(oldimage.getJpeg());
							cmsContentFilej.setFileContentType( FileContentType.PWELL_DIGITAL );
							cmsContentFilej.setFile(sj);
							contentService.addContentFile(cmsContentFilej); //新增
							contentService.removeFile(FileContentType.OWELL_DIGITAL, oldimage.getJpeg());//删除
						}
					}
				}
				//处理采集数据
				List<String> attributes = new ArrayList<String>();
				attributes.add("FLOW");
				attributes.add("FLOWTHRESHOLD");
				attributes.add("WATER");
				attributes.add("WATERTHRESHOLD");
				attributes.add("WATERDOWN");
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
				
				List<Object[]> howelldataList = observewellDao.getBySql(attributes, "HOWELLDATA", "where OWELL_ID="+well.getId());
				
				if (!howelldataList.isEmpty()) {
					List<HpwellData> hpwellDataList = new ArrayList<HpwellData>();
					for (int i = 0; i < howelldataList.size(); i++) {
						if (i>0&&i%Constants.BATCH_SIZE == 0) {
							//批量新增
							observewellDao.batchSavePwellData(hpwellDataList);
							hpwellDataList = new ArrayList<HpwellData>();
						}
						HpwellData hpwellData = new HpwellData();
						Object[] o = howelldataList.get(i);
						hpwellData.setFlow((BigDecimal) o[0]);
						hpwellData.setFlowThreshold((BigDecimal) o[1]);
						hpwellData.setWater((BigDecimal) o[2]);
						hpwellData.setWaterThreshold((BigDecimal) o[3]);
						hpwellData.setWaterDown((BigDecimal) o[4]);
						hpwellData.setTemperature((BigDecimal) o[5]);
						hpwellData.setTemperatureThreshold((BigDecimal) o[6]);
						hpwellData.setStatus((Integer) o[7]);
						hpwellData.setLastAccu((BigDecimal) o[8]);
						hpwellData.setThisAccu((BigDecimal) o[9]);
						hpwellData.setAccuPeriod((BigDecimal) o[10]);
						if (o[11]!=null) {
							hpwellData.setmAuto((Boolean) o[11]);
						} else {
							hpwellData.setmAuto(false);
						}
						hpwellData.getAuditSection().setDateCreated((Date) o[12]);
						hpwellData.getAuditSection().setDateModified((Date) o[13]);
						hpwellData.getAuditSection().setModifiedBy((String) o[14]);
						
						hpwellData.setpWell(pwell);
						hpwellDataList.add(hpwellData);
					}
					if (hpwellDataList.size()>0) {
						observewellDao.batchSavePwellData(hpwellDataList);
					}
					
					//删除观测井的采集数据
					observewellDao.excuteBySql("DELETE FROM HOWELLDATA where OWELL_ID="+well.getId());
					observewellDao.excuteBySql("DELETE FROM OWELLDATA where OWELL_ID="+well.getId());
				}
				
				//处理告警数据
				List<String> warnAttributes = new ArrayList<String>();
				warnAttributes.add("FLOW");
				warnAttributes.add("FLOWTHRESHOLD");
				warnAttributes.add("WATER");
				warnAttributes.add("WATERTHRESHOLD");
				warnAttributes.add("WATERDOWN");
				warnAttributes.add("TEMPERATURE");
				warnAttributes.add("TEMPERATURETHRESHOLD");
				warnAttributes.add("STATUS");
				warnAttributes.add("DATE_CREATED");
				warnAttributes.add("DATE_MODIFIED");
				warnAttributes.add("UPDT_ID");
				warnAttributes.add("WARNING_TYPE");
				warnAttributes.add("CSITE_ID");
				
				List<Object[]> wdataList = observewellDao.getBySql(warnAttributes, "WARNING_OWELLDATA", "where POINT_ID="+well.getId());
				if (!wdataList.isEmpty()) {
					List<WpwellData> wpwellDataList = new ArrayList<WpwellData>();
					for (int i = 0; i < wdataList.size(); i++) {
						if (i>0&&i%Constants.BATCH_SIZE == 0) {
							//批量新增
							observewellDao.batchSaveWpwellData(wpwellDataList);
							wpwellDataList = new ArrayList<WpwellData>();
						}
						WpwellData wpwellData = new WpwellData();
						Object[] o = wdataList.get(i);
						wpwellData.setFlow((BigDecimal) o[0]);
						wpwellData.setFlowThreshold((BigDecimal) o[1]);
						wpwellData.setWater((BigDecimal) o[2]);
						wpwellData.setWaterThreshold((BigDecimal) o[3]);
						wpwellData.setWaterDown((BigDecimal) o[4]);
						wpwellData.setTemperature((BigDecimal) o[5]);
						wpwellData.setTemperatureThreshold((BigDecimal) o[6]);
						wpwellData.setStatus((Integer) o[7]);
						wpwellData.getAuditSection().setDateCreated((Date) o[8]);
						wpwellData.getAuditSection().setDateModified((Date) o[9]);
						wpwellData.getAuditSection().setModifiedBy((String) o[10]);
						wpwellData.setWarningType((int) o[11]);
						
						wpwellData.setcSite(well.getcSite());
						wpwellData.setPoint(pwell);
						wpwellDataList.add(wpwellData);
					}
					if (wpwellDataList.size()>0) {
						observewellDao.batchSaveWpwellData(wpwellDataList);
					}
					
					//删除降水井的告警数据
					observewellDao.excuteBySql("DELETE FROM WARNING_OWELLDATA where POINT_ID="+well.getId());
				}
				
				observewellDao.delete(well);
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
	public void saveQcode(Observewell pwell) throws ServiceException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				InputContentFile cmsContentFile = new InputContentFile();
				cmsContentFile.setFileName(pwell.getqCode());
				cmsContentFile.setFileContentType( FileContentType.QCODE );
				cmsContentFile.setFile(pwell.getDigital() );
				contentService.addContentFile(cmsContentFile);
				this.update(pwell);
	}

	@Override
	public List<Observewell> getByIds(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.observewellDao.getByIds(ids);
	}
}
