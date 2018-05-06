package com.kekeinfo.core.business.equipment.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.attach.service.AttachService;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.equipment.dao.EquipDao;
import com.kekeinfo.core.business.equipment.model.Equip;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;

@Service("equipService")
public class EquipServiceImpl extends KekeinfoEntityServiceImpl<Long, Equip> implements EquipService {
	private EquipDao equipDao;
	
	@Autowired private ContentService contentService;
	@Autowired AttachService attachService;

	@Autowired
	public EquipServiceImpl(EquipDao equipDao) {
		super(equipDao);
		this.equipDao = equipDao;
	}

	@Override
	public Equip getByIdWithImg(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.equipDao.getByIdWithImg(cid);
	}
	
	@Override
	@Transactional
	public void saveOrUpdate(Equip equip,String delids) throws ServiceException{
		
			List<Attach> newimgs=equip.getAttach();
			//先新增imags
			if(newimgs!=null && newimgs.size()>0){
				for(Attach img:newimgs){
					InputContentFile cmsContentFile = new InputContentFile();
					cmsContentFile.setFileName(img.getFileName());
					cmsContentFile.setFileContentType( FileContentType.EQUIPMENT );
					cmsContentFile.setFile(img.getDigital() );
					contentService.addContentFile(cmsContentFile);
					attachService.save(img);
				}
			}
			
			
			if(equip.getId()!=null){
				Equip dbe =this.getByIdWithImg(equip.getId());
				List<Attach> at =dbe.getAttach();
				for(Attach a:at){
					equip.getAttach().add(a);
				}
			}
			//删除新的文件
			if(StringUtils.isNotBlank(delids)){
				String[] ids = delids.split(",");
				for(String s:ids){
					//对应的数据记录也要删除
					Attach dbDoc = attachService.getById(Long.parseLong(s));
					attachService.delete(dbDoc);
					contentService.removeFile(FileContentType.EQUIPMENT, s);
					equip.getAttach().remove(dbDoc);
				}
			}
			
			super.saveOrUpdate(equip);
		}

	@Override
	public List<Object[]> getPinYin(String ids) throws ServiceException {
		// TODO Auto-generated method stub
		return this.equipDao.getPinYin(ids);
	}
	}

