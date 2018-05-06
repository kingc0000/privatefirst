/**
 * 
 */
package com.kekeinfo.core.modules.cms.content;

import java.io.InputStream;
import java.util.List;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.model.OutputContentFile;
import com.kekeinfo.core.business.generic.exception.ServiceException;


/**
 * @author Umesh Awasthi
 *
 */
public class StaticContentFileManagerImpl extends StaticContentFileManager
{

    private FilePut uploadFile;
    private FileGet getFile;
    private FileRemove removeFile;


    
    @Override
    public void addFile( final InputContentFile inputStaticContentData )
        throws ServiceException
    {
    	uploadFile.addFile( inputStaticContentData );
        
    }
    
    /**
     * Implementation for add static data files. This method will called respected add files method of underlying
     * CMSStaticContentManager. For CMS Content files {@link CmsStaticContentFileManagerInfinispanImpl} will take care of adding
     * given content images with Infinispan cache.
     * 
     * @param merchantStoreCode merchant store.
     * @param inputStaticContentDataList Input content images
     * @throws ServiceException
     */
    @Override
    public void addFiles( final List<InputContentFile> inputStaticContentDataList )
        throws ServiceException
    {
    	uploadFile.addFiles( inputStaticContentDataList );
    }
    @Override
    public void removeFile( final FileContentType staticContentType, final String fileName)
        throws ServiceException
    {
    	removeFile.removeFile(staticContentType, fileName);
        
    }


	@Override
	public OutputContentFile getFile(
			FileContentType fileContentType, String contentName)
			throws ServiceException {
		return getFile.getFile(fileContentType, contentName);
	}
	
	@Override
	public InputStream getFileInputstream(
			FileContentType fileContentType, String contentName)
			throws ServiceException {
		return getFile.getFileInputstream(fileContentType, contentName);
	}

	@Override
	public List<String> getFileNames(
			FileContentType fileContentType) throws ServiceException {
		return getFile.getFileNames(fileContentType);
	}

	@Override
	public List<OutputContentFile> getFiles(
			FileContentType fileContentType) throws ServiceException {
		return getFile.getFiles(fileContentType);
	}

	@Override
	public void removeFiles() throws ServiceException {
		removeFile.removeFiles();
	}
    
   

	public void setRemoveFile(FileRemove removeFile) {
		this.removeFile = removeFile;
	}

	public FileRemove getRemoveFile() {
		return removeFile;
	}

	public void setGetFile(FileGet getFile) {
		this.getFile = getFile;
	}

	public FileGet getGetFile() {
		return getFile;
	}

	public void setUploadFile(FilePut uploadFile) {
		this.uploadFile = uploadFile;
	}

	public FilePut getUploadFile() {
		return uploadFile;
	}
  
    
}
