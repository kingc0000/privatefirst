/**
 * 
 */
package com.kekeinfo.core.modules.cms.content;

import java.util.List;

import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.generic.exception.ServiceException;


/**
 * @author Umesh Awasthi
 *
 */
public interface FilePut
{
    public void addFile(InputContentFile inputStaticContentData) throws ServiceException;
    public void addFiles(List<InputContentFile> inputStaticContentDataList) throws ServiceException;
}
