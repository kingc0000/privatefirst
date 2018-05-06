package com.kekeinfo.core.business.monitoreqip.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.equipment.model.Equip;
import com.kekeinfo.core.business.equipment.service.EquipService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.model.MPoint;
import com.kekeinfo.core.business.monitoreqip.dao.MonitorEqipDao;
import com.kekeinfo.core.business.monitoreqip.model.MonitorEqip;
import com.kekeinfo.core.business.monitoreqip.model.MpointEquip;

@Service("monitoreqipService")
public class MonitorEqipServiceImpl extends KekeinfoEntityServiceImpl<Long, MonitorEqip> implements MonitorEqipService {
	private MonitorEqipDao monitoreqipDao;
	@Autowired private MpointEquipService mpointEquipService;
	@Autowired EquipService  equipService;
	@Autowired
	public MonitorEqipServiceImpl(MonitorEqipDao monitoreqipDao) {
		super(monitoreqipDao);
		this.monitoreqipDao = monitoreqipDao;
	}

	@Override
	public List<MonitorEqip> getByMid(long mid) throws ServiceException {
		// TODO Auto-generated method stub
		return monitoreqipDao.getByMid(mid);
	}

	@Transactional
	@Override
	public void saveOrUpdate(MonitorEqip monitorEqip, String mpids[]) throws ServiceException {
		// TODO Auto-generated method stub
		List<MpointEquip> dbmes =null;
		List<String> newmes =new ArrayList<>();
		List<MpointEquip> upmes =new ArrayList<>();
		if(mpids!=null && mpids.length>0){
			if(monitorEqip.getId()!=null){
				dbmes=mpointEquipService.getByMeid(monitorEqip.getId());
				if(dbmes!=null && dbmes.size()>0){
					//新增记录，之前存在不修改
					for(String s:mpids){
						//查找之前的记录
						boolean found=false;
						for(MpointEquip ep:dbmes){
							if(ep.getMpoint().getId().equals(Long.parseLong(s))){
								//从数据库里找到记录，没有修改，不做变化
								found=true;
								break;
							}
						}
						if(!found){
							newmes.add(s);
						}
					}
					//之前存在的现在没有了,状态要修改
					for(MpointEquip ep:dbmes){
						boolean found=false;
						for(String s:mpids){
							if(ep.getMpoint().getId().equals(Long.parseLong(s))){
								found=true;
								break;
							}
						}
						if(!found){
							upmes.add(ep);
						}
					}
				}else{
					//以前的数据为空，全部新增
					for(String s:mpids){
						newmes.add(s);
					}
				}
				super.update(monitorEqip);
			}else{
				//设备新增，对应的关系新增
				for(String s:mpids){
					newmes.add(s);
				}
				super.save(monitorEqip);
			}
		}else{
			if(monitorEqip.getId()!=null){
				dbmes =mpointEquipService.getByMeid(monitorEqip.getId());
				//以前的设备都不用了，数据改变
				if(dbmes!=null){
					for(MpointEquip ep:dbmes){
						upmes.add(ep);
					}
				}
				super.update(monitorEqip);
			}else{
				super.save(monitorEqip);
			}
		}
		//统一新增
		if(newmes.size()>0){
			for(String s:newmes){
				MpointEquip mpe = new MpointEquip();
				mpe.setMonitor(monitorEqip.getMonitor());
				mpe.setMonitorEqip(monitorEqip);
				MPoint mpoint =new MPoint();
				mpoint.setId(Long.parseLong(s));
				mpe.setMpoint(mpoint);
				mpe.setUsed(true);
				mpe.getAuditSection().setDateCreated(new Date());
				mpointEquipService.save(mpe);
			}
		}
		//统一修改
		if(upmes.size()>0){
			for(MpointEquip ep:upmes){
				ep.setUsed(false);
				mpointEquipService.update(ep);
			}
		}
		//改变设备的状态
		Equip equip =equipService.getById(monitorEqip.getEquip().getId());
		equip.setStatus(2);
		equipService.update(equip);
		
	}

	@Override
	@Transactional
	public void remove(MonitorEqip monitorEqip) throws ServiceException {
		// TODO Auto-generated method stub
		Equip equip =monitorEqip.getEquip();
		equip.setStatus(1);
		equipService.update(equip);
		List<MpointEquip> dbmes =mpointEquipService.getByMeid(monitorEqip.getId());
		if(dbmes!=null && dbmes.size()>0){
			for(MpointEquip meq:dbmes){
				mpointEquipService.delete(meq);
			}
		}
		
		super.delete(monitorEqip);
	}
}
