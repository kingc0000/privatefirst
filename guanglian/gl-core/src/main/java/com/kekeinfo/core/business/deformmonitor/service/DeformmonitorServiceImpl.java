package com.kekeinfo.core.business.deformmonitor.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.data.service.EMonitorDataService;
import com.kekeinfo.core.business.data.service.HeMonitorDataService;
import com.kekeinfo.core.business.deformmonitor.dao.DeformmonitorDao;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitordata.model.HemonitorData;

@Service("deformmonitorService")
public class DeformmonitorServiceImpl extends KekeinfoEntityServiceImpl<Long, Deformmonitor> implements DeformmonitorService {
	
	private DeformmonitorDao deformmonitorDao;
	
	@Autowired EMonitorDataService eMonitorDataService;
	@Autowired HeMonitorDataService heMonitorDataService;
	@Autowired ContentService contentService;
	@Autowired CSiteService cSiteService;
	@Autowired 
	public DeformmonitorServiceImpl(DeformmonitorDao deformmonitorDao) {
		super(deformmonitorDao);
		this.deformmonitorDao = deformmonitorDao;
	}

	@Override
	public Deformmonitor getByIdWithCSite(Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		return deformmonitorDao.getByIdWithCSite(pid);
	}
	
	public Deformmonitor getByIdWithPointLink(Long pid) throws ServiceException {
		return deformmonitorDao.getByIdWithPointLink(pid);
	}

	@Override
	@Transactional
	public void saveStatus(Deformmonitor dData) throws ServiceException {
		Map<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateCreated", "desc");
		Entitites<HemonitorData> dwellDataList = heMonitorDataService.getPageListByAttributes(Arrays.asList("emonitor.id"), Arrays.asList(dData.getId()), 1, 0,  orderby);
		//最新的观测点数据
		if (dwellDataList.getTotalCount()>0) {
			HemonitorData dwellData = dwellDataList.getEntites().get(0);
			dData.setlData(dData.getrData());
			dData.setrData(dwellData.getData());
			dData.setLastDate(dwellData.getAuditSection().getDateCreated());
			int laststatus=dData.getDataStatus();
			dData.updateDataStatusByData(dData.getrData());
			if(laststatus==0){
				//++
				if(dData.getDataStatus()>0){
					ConstructionSite scite = dData.getcSite();
					scite.setRunstatus(scite.getRunstatus()+1);
					cSiteService.update(scite);
				}
			}else{
				//--
				if(dData.getDataStatus()==0){
					ConstructionSite scite = dData.getcSite();
					int runstatus=scite.getRunstatus();
					if(runstatus>0){
						scite.setRunstatus(scite.getRunstatus()-1);
						cSiteService.update(scite);
					}
				}
			}
			super.saveOrUpdate(dData);
		}
		
	}

	@Override
	public List<Deformmonitor> getWarningData(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.deformmonitorDao.getWarningData(ids);
	}
	
	@Override
    public void saveOrUpdate(Deformmonitor entity) throws ServiceException
    {
        // save or update (persist and attach entities
        if ( entity.getId() != null) {
        	Deformmonitor db = deformmonitorDao.getById(entity.getId());
        	if (db.getPointLink()!=null) {
        		entity.setPointLink(db.getPointLink());
			}
        	deformmonitorDao.update( entity );
        } else {
        	deformmonitorDao.save( entity );
        }
    }

	@Override
	public List<Deformmonitor> getBycid(Long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.deformmonitorDao.getBycid(cid);
	}

	@Transactional
	@Override
	public void saveQcode(Deformmonitor iwell) throws ServiceException {
		// TODO Auto-generated method stub
				InputContentFile cmsContentFile = new InputContentFile();
				cmsContentFile.setFileName(iwell.getqCode());
				cmsContentFile.setFileContentType( FileContentType.QCODE );
				cmsContentFile.setFile(iwell.getDigital() );
				contentService.addContentFile(cmsContentFile);
				this.update(iwell);
		
	}

	@Override
	public List<Deformmonitor> getByIds(List<Long> ids) throws ServiceException {
		return this.deformmonitorDao.getByIds(ids);
	}
	
	@Override
	public void deleteById(Object id) throws ServiceException {
		Deformmonitor well = deformmonitorDao.getById((Long) id);
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
}
