package com.kekeinfo.core.business.monitor.substrate.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.attach.service.AttachService;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.monitor.substrate.dao.SubStrateDao;
import com.kekeinfo.core.business.monitor.substrate.model.SubStrate;

@Service("substrateService")
public class SubStrateServiceImpl extends KekeinfoEntityServiceImpl<Long, SubStrate> implements SubStrateService {
	private SubStrateDao substrateDao;

	@Autowired private AttachService  attachService;

	@Autowired
	private ContentService contentService;

	@Autowired
	public SubStrateServiceImpl(SubStrateDao substrateDao) {
		super(substrateDao);
		this.substrateDao = substrateDao;
	}

	@Override
	public void saveUpdate(SubStrate gp, String dels) throws ServiceException {
		List<String> delFilenames = new ArrayList<String>();
		SubStrate db =null;
		if(gp.getId()!=null){
			db =this.substrateDao.withAttach(gp.getId());
		}
		//新增图片
		List<Attach> attachs = gp.getAttach();
		if(attachs!=null && attachs.size()>0) {
			for(Attach attach : attachs) {
				if(attach.getDigital()!=null ) {//新增图片
			        InputStream inputStream = attach.getDigital();
			        InputContentFile cmsContentImage = new InputContentFile();
			        cmsContentImage.setFileName( attach.getFileName());
			        cmsContentImage.setFile( inputStream );
			        cmsContentImage.setFileContentType(FileContentType.MONITOR_SUB);
			        contentService.addContentFile(cmsContentImage);
			        attachService.save(attach);
				}
			}
		}
		//记录删除的图片
		if(StringUtils.isNotBlank(dels)){
			if(db!=null){
				if(db.getAttach()!=null && db.getAttach().size()>0){
					String[] delid = dels.split(",");
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
		}else if(db!=null && db.getAttach()!=null){
			for(Attach gi:db.getAttach()){
				gp.getAttach().add(gi);
			}
		}
		super.saveOrUpdate(gp);
		//删除不要的日志图片
		if (delFilenames.size()>0) {
			for (int i = 0; i < delFilenames.size(); i++) {
				contentService.removeFile(FileContentType.MONITOR_SUB, delFilenames.get(i));
			}
		}// TODO Auto-generated method stub
		
	}

	@Override
	public void deletewithattach(Long gid) throws ServiceException {
		SubStrate dg =this.substrateDao.withAttach(gid);
		List<Attach> gims =dg.getAttach();
		if(gims!=null && gims.size()>0){
			for(Attach gm:gims){
				contentService.removeFile(FileContentType.MONITOR_SUB, gm.getFileName());
				attachService.delete(gm);
			}
		}
		super.delete(dg);
		
	}

	@Override
	public SubStrate withAttach(Long gid) throws ServiceException {
		// TODO Auto-generated method stub
				return this.substrateDao.withAttach(gid);
	}

	

}
