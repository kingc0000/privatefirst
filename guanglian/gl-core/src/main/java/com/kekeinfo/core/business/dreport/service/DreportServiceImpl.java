package com.kekeinfo.core.business.dreport.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.dreport.dao.DreportDao;
import com.kekeinfo.core.business.dreport.model.Dattach;
import com.kekeinfo.core.business.dreport.model.Dreport;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;

@Service("dreportService")
public class DreportServiceImpl extends KekeinfoEntityServiceImpl<Long, Dreport> implements
		DreportService {
	
	private DreportDao dreportDao;
	@Autowired
	private ContentService contentService;
	
	@Autowired
	public DreportServiceImpl(DreportDao dreportDao) {
		super(dreportDao);
		this.dreportDao = dreportDao;
	}

	@Override
	public List<Dreport> getApproveDreports(Long uid, Integer[] types) {
		// TODO Auto-generated method stub
		return dreportDao.getApproveDreports(uid, types);
	}
	
	/**
	 * 获取待办事项数量
	 * @param uid
	 * @param types
	 * @return
	 */
	public int countApproveDreports(Long uid, Integer[] types) {
		return dreportDao.countApproveDreports(uid, types);
	}

	@Override
	public void save(Dreport report, String dels) throws ServiceException {
		Dreport newReport = report;
		List<String> delFilenames = new ArrayList<String>();
		if (report.getId() != null) { //更新操作
			//获取数据库对象
			newReport = getById(report.getId());
			//处理需要删除的附件,判断是否存在需要删除的附件ID，如果存在，则移除该附件
			if (!StringUtils.isBlank(dels)) {
				String[] delArray = dels.split("#");
				if (delArray!=null && delArray.length>0) {
					Set<Dattach> attachSet = newReport.getAttaches();
					if (attachSet!=null && attachSet.size()>0) {
						Iterator<Dattach> iter = attachSet.iterator();
						while (iter.hasNext()) {
							Dattach attach = (Dattach) iter.next();
							for (int i = 0; i < delArray.length; i++) {
								if(attach.getId().toString().equals(delArray[i])) {
									delFilenames.add(attach.getFileName());
									iter.remove();//在指针遍历过程中处理，移除指针，而不能是集合中移除对象
								}
							}
						}
					}
				}
			}
			newReport.setAuditSection(report.getAuditSection());
			newReport.setNote(report.getNote());
			newReport.setRank(report.getRank());
			newReport.setTitle(report.getTitle());
			newReport.setUser(report.getUser());
			newReport.getAttaches().addAll(report.getAttaches()); //新增加的附件加入原有未删除附件集合中
		}
		//保存新增加的附件信息
		Set<Dattach> attaches = report.getAttaches();
		if(attaches!=null && attaches.size()>0) {
			for(Dattach attach : attaches) {
				if(attach.getDigital()!=null && attach.getId()==null) {//新增附件
			        InputStream inputStream = attach.getDigital();
			        InputContentFile cmsContentImage = new InputContentFile();
			        cmsContentImage.setFileName( attach.getFileName());
			        cmsContentImage.setFile( inputStream );
			        cmsContentImage.setFileContentType(FileContentType.DREPORT);
			        contentService.addContentFile(cmsContentImage);
				}
			}
		}
		
		super.saveOrUpdate(newReport);
		//删除不要的附件信息
		if (delFilenames.size()>0) {
			for (int i = 0; i < delFilenames.size(); i++) {
				contentService.removeFile(FileContentType.DREPORT, delFilenames.get(i));
			}
		}
	}
	
	@Override
	public void deleteById(Object id) {
		Dreport report = super.getById((Long) id);
		try {
			super.delete(report);
			//删除附件
			Set<Dattach> attaches = report.getAttaches();
			for (Dattach dattach : attaches) {
				contentService.removeFile(FileContentType.DREPORT, dattach.getFileName());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int countApproveDreports(String uname, Integer[] types) {
		// TODO Auto-generated method stub
		return this.dreportDao.countApproveDreports(uname, types);
	}
}
