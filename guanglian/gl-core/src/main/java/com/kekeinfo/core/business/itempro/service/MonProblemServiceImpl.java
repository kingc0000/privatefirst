package com.kekeinfo.core.business.itempro.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.appmessage.model.AMessage;
import com.kekeinfo.core.business.appmessage.model.MessageU;
import com.kekeinfo.core.business.appmessage.service.AMessageService;
import com.kekeinfo.core.business.appmessage.service.MessageUService;
import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.attach.service.AttachService;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.itempro.dao.MonProblemDao;
import com.kekeinfo.core.business.monproblem.model.MonProblem;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.BaiduPushUtitls;
import com.kekeinfo.core.utils.IosPushUtils;
import com.kekeinfo.core.utils.MessageTypeEnum;

@Service("monproblemService")
public class MonProblemServiceImpl extends KekeinfoEntityServiceImpl<Long, MonProblem> implements MonProblemService{

	private MonProblemDao monProblemDao;
	
	@Autowired
	private ContentService contentService;
	@Autowired private AttachService  attachService;
	@Autowired private IosPushUtils iosPushUtils;
	@Autowired private BaiduPushUtitls baiduPushUtitls;
	@Autowired private MessageUService sessageUService;
	@Autowired AMessageService aMessageService;
	@Autowired UserService userService;
	
	@Autowired
	public MonProblemServiceImpl(MonProblemDao monProblemDao) {
		super(monProblemDao);
		this.monProblemDao = monProblemDao;
	}

	@Override
	@Transactional
	public void saveUpdate(MonProblem gp, String dels) throws ServiceException {
		// TODO Auto-generated method stub
		List<String> delFilenames = new ArrayList<String>();
		//新增图片
		List<Attach> attachs = gp.getAttach();
		if(attachs!=null && attachs.size()>0) {
			for(Attach attach : attachs) {
				if(attach.getDigital()!=null ) {//新增图片
			        InputStream inputStream = attach.getDigital();
			        InputContentFile cmsContentImage = new InputContentFile();
			        cmsContentImage.setFileName( attach.getFileName());
			        cmsContentImage.setFile( inputStream );
			        cmsContentImage.setFileContentType(FileContentType.MONITOR_PRO);
			        contentService.addContentFile(cmsContentImage);
			        attachService.save(attach);
				}
			}
		}
		//记录删除的图片
		if(StringUtils.isNotBlank(dels)){
			if(gp.getId()!=null){
				MonProblem db =this.monProblemDao.withAttach(gp.getId());
				if(db.getAttach()!=null && db.getAttach().size()>0){
					String[] delid = dels.split("#");
					for(Attach gi:db.getAttach()){
						boolean isadd=true;
						for (int i = 0; i < delid.length; i++) {
							if(gi.getId().equals(Long.parseLong(delid[i]))){
								delFilenames.add(gi.getFileName());
								attachService.delete(gi);
								isadd=false;
								break;
							}
						}
						if(isadd){
							gp.getAttach().add(gi);
						}
					}
				}
			}
		}
		
		super.saveOrUpdate(gp);
		//删除不要的日志图片
		if (delFilenames.size()>0) {
			for (int i = 0; i < delFilenames.size(); i++) {
				contentService.removeFile(FileContentType.MONITOR_PRO, delFilenames.get(i));
			}
		}// TODO Auto-generated method stub
		
		//推送信息到项目负责人
				if(gp.getMonitor().getProject()!=null && gp.getMonitor().getProject().getProjectOwnerid()!=null){
					User user = userService.getById(gp.getMonitor().getProject().getProjectOwnerid());
					AMessage am =new AMessage();
					am.setMtype(MessageTypeEnum.GIssuesNotice);
					am.setDateCreated(new Date());
					am.setTitle("项目问题");
					am.setMessage(gp.getMonitor().getProject().getName()+"有新的问题，请查看");
					aMessageService.save(am);
					MessageU um=new MessageU();
					um.setMessage(am);
					um.setUser(user);
					sessageUService.save(um);
					if(StringUtils.isNotBlank(user.getAtype())){
						try{
							if(user.getAtype().equalsIgnoreCase("iOS")){
								iosPushUtils.setPushToken(user.getDevice_token());
								iosPushUtils.setTitle(am.getMessage());
								iosPushUtils.iPush();
							}else if(user.getAtype().equalsIgnoreCase("android")){
								baiduPushUtitls.setTitle("广联项目问题通知");
								baiduPushUtitls.setChannelID(user.getDevice_token());
								baiduPushUtitls.setDeviceType(3);
								baiduPushUtitls.setMessage(am.getMessage());
								baiduPushUtitls.pushMessage();
							}
						}catch (Exception e){
							e.printStackTrace();
						}
					}
					
				}
	}

	@Override
	@Transactional
	public void deletewithattach(Long gid) throws ServiceException {
		// TODO Auto-generated method stub
			MonProblem dg =this.monProblemDao.withAttach(gid);
				List<Attach> gims =dg.getAttach();
				if(gims!=null && gims.size()>0){
					for(Attach gm:gims){
						contentService.removeFile(FileContentType.MONITOR_PRO, gm.getFileName());
						attachService.delete(gm);
					}
				}
				super.delete(dg);
	}

	@Override
	public MonProblem withAttach(Long gid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.monProblemDao.withAttach(gid);
	}


}
