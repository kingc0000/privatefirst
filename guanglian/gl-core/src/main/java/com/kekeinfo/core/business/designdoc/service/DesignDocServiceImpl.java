package com.kekeinfo.core.business.designdoc.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.designdoc.dao.DesignDocDao;
import com.kekeinfo.core.business.designdoc.model.Designdoc;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;



@Service( "designDocService" )
public class DesignDocServiceImpl extends KekeinfoEntityServiceImpl<Long, Designdoc> implements DesignDocService
{
	@SuppressWarnings("unused")
	private DesignDocDao designDocDao;
	
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	public DesignDocServiceImpl(DesignDocDao designDocDao) {
		super(designDocDao);
		this.designDocDao = designDocDao;
	}

   // private static final Logger LOG = LoggerFactory.getLogger( DesignDocServiceImpl.class );
	@Transactional
	@Override
	public void saveOrUpdate(Designdoc ddoc) throws ServiceException{
		String oldfile = "";
		if(ddoc.getId()!=null){ 
			Designdoc dbDoc = this.getById(ddoc.getId());
			if (StringUtils.isNotEmpty(ddoc.getFileName())) {
				oldfile = dbDoc.getFileName();
			} else {
				ddoc.setFileName(dbDoc.getFileName());
				ddoc.setFileType(dbDoc.isFileType());
			}
			
			super.update(ddoc);
		}else{
			super.save(ddoc);
		}
		//添加新的文件
		if (ddoc.getDigital()!=null) {
			if (StringUtils.isNotEmpty(oldfile)) { //删除旧文件
				contentService.removeFile(FileContentType.PRODUCT_DIGITAL, oldfile);
			}
			InputContentFile cmsContentFile = new InputContentFile();
			cmsContentFile.setFileName(ddoc.getFileName());
			cmsContentFile.setFileContentType( FileContentType.PRODUCT_DIGITAL );
			cmsContentFile.setFile(ddoc.getDigital() );
			contentService.addContentFile(cmsContentFile);
		}
		
	}
	
	@Override
	public void delelteByDic(long did) throws ServiceException {
		// TODO Auto-generated method stub
		Designdoc dbDoc = this.getById(did);
		super.delete(dbDoc);
		if (StringUtils.isNotEmpty(dbDoc.getFileName())) {
			contentService.removeFile(FileContentType.PRODUCT_DIGITAL, dbDoc.getFileName());
		}
	}

}
