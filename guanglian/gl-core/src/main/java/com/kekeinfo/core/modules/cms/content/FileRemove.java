/**
 * 
 */
package com.kekeinfo.core.modules.cms.content;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.generic.exception.ServiceException;


/**
 * @author Umesh Awasthi
 *
 */
public interface FileRemove
{
    public void removeFile(FileContentType staticContentType, String fileName) throws ServiceException;
    public void removeFiles() throws ServiceException;

}
