package com.kekeinfo.core.modules.cms.content;

import java.io.InputStream;
import java.util.List;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.OutputContentFile;
import com.kekeinfo.core.business.generic.exception.ServiceException;


/**
 * Methods to retrieve the static content from the CMS
 * @author Carl Samson
 *
 */
public interface FileGet
{

	public OutputContentFile getFile(FileContentType fileContentType, String contentName) throws ServiceException;
    public List<String> getFileNames(FileContentType fileContentType) throws ServiceException;
    public List<OutputContentFile> getFiles(FileContentType fileContentType) throws ServiceException;
    public InputStream getFileInputstream( final FileContentType fileContentType, final String contentFileName )
            throws ServiceException;
}
