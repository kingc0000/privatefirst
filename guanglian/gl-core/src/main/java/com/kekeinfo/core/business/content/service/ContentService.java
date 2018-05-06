package com.kekeinfo.core.business.content.service;

import java.io.InputStream;
import java.util.List;

import com.kekeinfo.core.business.content.model.Content;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.model.OutputContentFile;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityService;


/**
 * 
 * Interface defining methods responsible for CMSContentService.
 * ContentServive will be be entry point for CMS and take care of following functionalities.
 * <li>Adding,removing Content images for given merchant store</li>
 * <li>Get,Save,Update Content data for given merchant store</li>
 *  
 * @author Yong chen
 *
 */
public interface ContentService extends KekeinfoEntityService<Long, Content>
    
{

	public List<Content> listByType( String contentType)
	        throws ServiceException;

    /**
     * Method responsible for storing content file for given Store.Files for given merchant store will be stored in
     * Infinispan.
     * 
     * @param merchantStoreCode merchant store whose content images are being saved.
     * @param contentFile content image being stored
     * @throws ServiceException
     */
    void addContentFile(InputContentFile contentFile )
        throws ServiceException;

   
    /**
     * Method responsible for storing list of content image for given Store.Images for given merchant store will be stored in
     * Infinispan.
     * 
     * @param merchantStoreCode  merchant store whose content images are being saved.
     * @param contentImagesList list of content images being stored.
     * @throws ServiceException
     */
    void addContentFiles(List<InputContentFile> contentFilesList) throws ServiceException;
    
    
    /**
     * Method to remove given content image.Images are stored in underlying system based on there name.
     * Name will be used to search given image for removal
     * @param imageContentType
     * @param imageName
     * @param merchantStoreCode merchant store code
     * @throws ServiceException
     */
    public void removeFile(FileContentType fileContentType, String fileName) throws ServiceException;
    
    
    /**
     * Method to remove all images for a given merchant.It will take merchant store as an input and will
     * remove all images associated with given merchant store.
     * 
     * @param merchantStoreCode
     * @throws ServiceException
     */
    public void removeFiles() throws ServiceException;
    
    /**
     * Method responsible for fetching particular content image for a given merchant store. Requested image will be
     * search in Infinispan tree cache and OutputContentImage will be sent, in case no image is found null will
     * returned.
     * 
     * @param merchantStoreCode
     * @param imageName
     * @return {@link OutputContentImage}
     * @throws ServiceException
     */
    public OutputContentFile getContentFile(FileContentType fileContentType, String fileName )
        throws ServiceException;
    
    public InputStream getContentFileInputstream(FileContentType fileContentType, String fileName )
            throws ServiceException;
    
    
    /**
     * Method to get list of all images associated with a given merchant store.In case of no image method will return an empty list.
     * @param merchantStoreCode
     * @param imageContentType
     * @return list of {@link OutputContentImage}
     * @throws ServiceException
     */
    public List<OutputContentFile> getContentFiles(FileContentType fileContentType )
                    throws ServiceException;

	
    List<String> getContentFilesNames(
			FileContentType fileContentType) throws ServiceException;

    /**
     * Add the store logo
     * @param merchantStoreCode
     * @param cmsContentImage
     * @throws ServiceException
     */
	void addLogo(InputContentFile cmsContentImage)
			throws ServiceException;

	/**
	 * Adds a property (option) image
	 * @param merchantStoreId
	 * @param cmsContentImage
	 * @throws ServiceException
	 */
	void addOptionImage(InputContentFile cmsContentImage)
			throws ServiceException;

	

}
