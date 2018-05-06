package com.kekeinfo.core.business.point.service;

import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.appmessage.model.AMessage;
import com.kekeinfo.core.business.appmessage.model.MessageU;
import com.kekeinfo.core.business.appmessage.service.AMessageService;
import com.kekeinfo.core.business.appmessage.service.MessageUService;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitordata.model.WarningData;
import com.kekeinfo.core.business.point.dao.WarningDao;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.user.model.AppUser;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.utils.BaiduPushUtitls;
import com.kekeinfo.core.utils.IosPushUtils;
import com.kekeinfo.core.utils.MessageTypeEnum;

@Service("warningService")
public class WarningServiceImpl extends KekeinfoEntityServiceImpl<Long, WarningData<Basepoint<BasepointLink<?>, BasepointInfo<?>>>> implements WarningService {
	
	private WarningDao warningDao;
	
	@Autowired private IosPushUtils iosPushUtils;
	@Autowired private BaiduPushUtitls baiduPushUtitls;
	@Autowired AMessageService aMessageService;
	@Autowired MessageUService messageUService;
	private static final Logger LOGGER = LoggerFactory.getLogger(WarningServiceImpl.class);
	@Autowired
	public WarningServiceImpl(WarningDao warningDao) {
		super(warningDao);
		this.warningDao = warningDao;
	}

	@SuppressWarnings("rawtypes")
	public Entitites<WarningData> getListByCid(Long cid, PointEnumType type, Integer warningType, String search, Integer limit, Integer offset) throws ServiceException {
		return warningDao.getListByCid(cid, type, warningType, search, limit, offset);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public WarningData findLastWarning(Long pid, PointEnumType type, Integer warningType) throws ServiceException {
		return warningDao.findLastWarning(pid, type, warningType);
	}

	@Override
	public void deleteById(Long id, PointEnumType pointType) throws ServiceException {
		warningDao.deleteById(id, pointType);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	@Override
	public void saveOrUpdate(WarningData wdata,String name,Set<AppUser> ausers,String pname) throws ServiceException{
		if(wdata.getId()==null && ausers!=null){
			LOGGER.debug("写告警开始----------");
			LOGGER.debug(name);
			String message="";
			message=pname+"项目 有新的告警信息，请处理！";
			AMessage am =new AMessage();
			am.setDateCreated(new Date());
			am.setTitle(message);
			String detail="数据采集告警";
			int wtype=wdata.getWarningType();
			am.setMtype(MessageTypeEnum.WWarning);
			if(wtype==1){
				detail="断电告警";
				am.setMtype(MessageTypeEnum.WPower);
			}
			am.setMessage(name+detail);
			aMessageService.save(am);
			
			if(ausers!=null && ausers.size()>0){
				for(AppUser user:ausers){
					String uAgent = user.getuAgent();
					try{
						if(uAgent!=null){
							
							if(uAgent.equalsIgnoreCase("iOS")){
								iosPushUtils.setPushToken(user.getDevice_token());
								iosPushUtils.setTitle(message);
								iosPushUtils.iPush();
							}else if(uAgent.equalsIgnoreCase("android")){
								baiduPushUtitls.setTitle("广联告警信息通知");
								baiduPushUtitls.setChannelID(user.getDevice_token());
								baiduPushUtitls.setDeviceType(3);
								baiduPushUtitls.setMessage(message);
								baiduPushUtitls.pushMessage();
							}
							MessageU mu =new MessageU();
							mu.setMessage(am);
							User uuser =new User();
							uuser.setId(user.getId());
							mu.setUser(uuser);
							messageUService.save(mu);
							System.out.println(user.getId());
						}
					}catch (Exception e){
						e.printStackTrace();
						LOGGER.error(e.getMessage());
					}
				}
			}
		}
		super.saveOrUpdate(wdata);
	}
}
