package com.kekeinfo.core.business.basedata.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.basedata.dao.BaseDataDao;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.business.monitor.model.WaterLineType;
import com.kekeinfo.core.business.monitor.service.MonitorService;
import com.kekeinfo.core.business.monitor.service.WaterLineTypeService;

@Service("baseDataService")
public class BaseDataServiceImpl extends KekeinfoEntityServiceImpl<Long, BasedataType> implements
		BaseDataService {
	
	private BaseDataDao baseDataDao;
	
	@Autowired MonitorService monitorService;
	@Autowired WaterLineTypeService  waterLineTypeService;
	
	@Autowired
	public BaseDataServiceImpl(BaseDataDao baseDataDao) {
		super(baseDataDao);
		this.baseDataDao = baseDataDao;
	}

	@Override
	public List<BasedataType> listByType(String storeType) {
		// TODO Auto-generated method stub
		return baseDataDao.listByType(storeType);
	}
	
	@Transactional
	@Override
	public void saveOrUpdate(BasedataType bd) throws ServiceException{
		
		if(bd.getId()!=null){
			//需要更改已有项目的值
			if(bd.getType().equalsIgnoreCase("bd_project_type")){
				BasedataType dbdb =this.getById(bd.getId());
				ArrayList<String> delPreviw = new ArrayList<>();
				StringBuffer con1 =new StringBuffer("UPDATE PROJECT SET FEATURES= '").append(bd.getValue()).append("' WHERE FEATURES= '").append(dbdb.getValue()).append("'");
				delPreviw.add(con1.toString());
				this.excuteByNativeSql(delPreviw);
			}
			super.update(bd);
		}else {
			super.save(bd);
			if(bd.getType().equalsIgnoreCase("bd_monitor_line")){
				List<Monitor> ms =monitorService.list();
				for(Monitor m:ms){
					WaterLineType wt =new WaterLineType();
					wt.setBaseType(bd);
					wt.setMonitor(m);
					waterLineTypeService.save(wt);
				}
			}
		}
		
	}

	@Override
	public void remove(Long bid) throws ServiceException {
		// TODO Auto-generated method stub
		BasedataType bd =this.getById(bid);
		if(bd!=null){
			if(bd.getType().equalsIgnoreCase("bd_monitor_line")){
				List<WaterLineType> ms =waterLineTypeService.getByTid(bd.getId());
				for(WaterLineType m:ms){
					waterLineTypeService.delete(m);
				}
			}
			super.delete(bd);
		}
	}
	
	
}
