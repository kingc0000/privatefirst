package com.kekeinfo.core.business.content.service;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kekeinfo.core.business.content.dao.ContentDao;
import com.kekeinfo.core.business.content.model.Content;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.model.OutputContentFile;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.modules.cms.content.StaticContentFileManager;



@Service( "contentService" )
public class ContentServiceImpl extends KekeinfoEntityServiceImpl<Long, Content> implements ContentService
{

    private static final Logger LOG = LoggerFactory.getLogger( ContentServiceImpl.class );

    private final ContentDao contentDao;
    
    @Autowired
    StaticContentFileManager staticContentFileManager;
    
    @Autowired
    StaticContentFileManager contentFileManager;

    @Autowired
    public ContentServiceImpl( final ContentDao contentDao )
    {
        super( contentDao );

        this.contentDao = contentDao;
    }    
    
    @Override
    public List<Content> listByType( final String contentType)
        throws ServiceException
    {

        return contentDao.listByType( contentType);
    }

    /**
     * Method responsible for adding content file for given merchant store in underlying Infinispan tree
     * cache. It will take  {@link InputContentFile} and will store file for given merchant store according to its type.
     * it can save an image or any type of file (pdf, css, js ...)
     * @param merchantStoreCode Merchant store
     * @param contentFile {@link InputContentFile} being stored
     * @throws ServiceException service exception
     */
    @Override
    public void addContentFile(final InputContentFile contentFile )
        throws ServiceException
    {
        Assert.notNull( contentFile, "InputContentFile image can not be null" );
        Assert.notNull( contentFile.getFileContentType(), "InputContentFile.fileContentType can not be null" );
        
        if(contentFile.getFileContentType().name().equals(FileContentType.IMAGE.name())
        		|| contentFile.getFileContentType().name().equals(FileContentType.STATIC_FILE.name())) {
        	addFile(contentFile);
        } else {
        	addImage(contentFile);
        }
        
       

    }
    
    @Override
    public void addLogo(InputContentFile cmsContentImage )
    throws ServiceException {
    	
    	    Assert.notNull( cmsContentImage, "CMSContent image can not be null" );

		    
		    cmsContentImage.setFileContentType(FileContentType.LOGO);
		    addImage(cmsContentImage);
		   

    }
    
    @Override
    public void addOptionImage( InputContentFile cmsContentImage )
    throws ServiceException {
    	
    	
		    Assert.notNull( cmsContentImage, "CMSContent image can not be null" );
		    cmsContentImage.setFileContentType(FileContentType.PROPERTY);
		    addImage(cmsContentImage);
		   

    }
    
    
    private void addImage(InputContentFile contentImage ) throws ServiceException {
    	
    	try
	    {
	        LOG.info( "Adding content image ");
	        contentFileManager.addFile( contentImage );
	        
	    } catch ( Exception e )
		 {
		        LOG.error( "Error while trying to convert input stream to buffered image", e );
		        throw new ServiceException( e );
		
		 } finally {
			 
			 try {
				if(contentImage.getFile()!=null) {
					contentImage.getFile().close();
				}
			} catch (Exception ignore) {}
			 
		 }

    }
    
    private void addFile(InputContentFile contentImage ) throws ServiceException {
    	
    	try
	    {
	        LOG.info( "Adding content file fo}");
	        staticContentFileManager.addFile(contentImage);
	        
	    } catch ( Exception e )
		 {
		        LOG.error( "Error while trying to convert input stream to buffered image", e );
		        throw new ServiceException( e );
		
		 } finally {
			 
			 try {
				if(contentImage.getFile()!=null) {
					contentImage.getFile().close();
				}
			} catch (Exception ignore) {}
		 }

    }



    /**
     * Method responsible for adding list of content images for given merchant store in underlying Infinispan tree
     * cache. It will take list of {@link CMSContentImage} and will store them for given merchant store.
     * 
     * @param merchantStoreCode Merchant store
     * @param contentImagesList list of {@link CMSContentImage} being stored
     * @throws ServiceException service exception
     */
	@Override
	public void addContentFiles(List<InputContentFile> contentFilesList) throws ServiceException {
		
        Assert.notEmpty( contentFilesList, "File list can not be empty" );
        LOG.info( "Adding total {} images for given merchant",contentFilesList.size() );
		
        LOG.info( "Adding content images for merchant...." );
        //contentFileManager.addImages( merchantStoreCode, contentImagesList );
        staticContentFileManager.addFiles(contentFilesList);
        
        try {
			for(InputContentFile file : contentFilesList) {
				if(file.getFile()!=null) {
					file.getFile().close();
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
    /**
     * Method to remove given content image.Images are stored in underlying system based on there name.
     * Name will be used to search given image for removal
     * @param contentImage
     * @param merchantStoreCode merchant store
     * @throws ServiceException
     */
	@Override
	public void removeFile(FileContentType fileContentType, String fileName)
			throws ServiceException {
        Assert.notNull( fileContentType, "Content file type can not be null" );
        Assert.notNull( fileName, "Content Image Name can not be null" );
        
        
        //check where to remove the file
        if(fileContentType.name().equals(FileContentType.IMAGE.name())
        		|| fileContentType.name().equals(FileContentType.STATIC_FILE.name())) {
        	staticContentFileManager.removeFile(fileContentType, fileName);
        } else {
        	contentFileManager.removeFile(  fileContentType, fileName );
        }
		
	}

    /**
     * Method to remove all images for a given merchant.It will take merchant store as an input and will
     * remove all images associated with given merchant store.
     * 
     * @param merchantStoreCode
     * @throws ServiceException
     */
	@Override
	public void removeFiles() throws ServiceException {
        
        
        contentFileManager.removeFiles();
        staticContentFileManager.removeFiles();
		
	}

	
    /**
     * Implementation for getContentImage method defined in {@link ContentService} interface. Methods will return
     * Content image with given image name for the Merchant store or will return null if no image with given name found
     * for requested Merchant Store in Infinispan tree cache.
     * 
     * @param store Merchant merchantStoreCode
     * @param imageName name of requested image
     * @return {@link OutputContentImage}
     * @throws ServiceException
     */
	@Override
	public OutputContentFile getContentFile(FileContentType fileContentType, String fileName)
			throws ServiceException {
        Assert.notNull( fileName, "File name can not be null" );
        
        if(fileContentType.name().equals(FileContentType.IMAGE.name())
        		|| fileContentType.name().equals(FileContentType.STATIC_FILE.name())) {
        	return staticContentFileManager.getFile(fileContentType, fileName);
        	
        } else {
        	return contentFileManager.getFile( fileContentType, fileName );
        }
	}
	
	/**
	 * 读取文件输入流信息，可以提供文件复制
	 * @param fileContentType
	 * @param fileName
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public InputStream getContentFileInputstream(FileContentType fileContentType, String fileName)
			throws ServiceException {
        Assert.notNull( fileName, "File name can not be null" );
        
        if(fileContentType.name().equals(FileContentType.IMAGE.name())
        		|| fileContentType.name().equals(FileContentType.STATIC_FILE.name())) {
        	return staticContentFileManager.getFileInputstream(fileContentType, fileName);
        	
        } else {
        	return contentFileManager.getFileInputstream( fileContentType, fileName );
        }
	}

    /**
     * Implementation for getContentImages method defined in {@link ContentService} interface. Methods will return list of all
     * Content image associated with given  Merchant store or will return empty list if no image is associated with
     * given Merchant Store in Infinispan tree cache.
     * 
     * @param merchantStoreId Merchant store
     * @return list of {@link OutputContentImage}
     * @throws ServiceException
     */
	@Override
	public List<OutputContentFile> getContentFiles(FileContentType fileContentType) throws ServiceException {
        return staticContentFileManager.getFiles(fileContentType);
	}

    /**
     * Returns the image names for a given merchant and store
     * @param merchantStoreCode
     * @param imageContentType
     * @return images name list
     * @throws ServiceException
     */
	@Override
	public List<String> getContentFilesNames(FileContentType fileContentType) throws ServiceException {
        if(fileContentType.name().equals(FileContentType.IMAGE.name())
        		|| fileContentType.name().equals(FileContentType.STATIC_FILE.name())) {
        	return staticContentFileManager.getFileNames(fileContentType);
        } else {
        	return contentFileManager.getFileNames(fileContentType);
        }
	}
	

    

}
