package com.kekeinfo.web.admin.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.OutputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.web.constants.Constants;

@Controller
public class FilesController  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FilesController.class);
	

	
	@Autowired
	private ContentService contentService;
	

	/**
	 * 下载附件
	 * @param storeCode
	 * @param imageName
	 * @param extension
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 */
	@RequestMapping("/files/downloads/{fileName}.{extension}")
	public @ResponseBody void downloadProduct(@PathVariable final String fileName, @PathVariable final String extension, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ftype=request.getParameter("ftype");
		FileContentType fileType = FileContentType.PRODUCT_DIGITAL;
		if(ftype!=null && StringUtils.isNotBlank(ftype)){
			fileType = FileContentType.valueOf(ftype);
		}
		
		String fileNameAndExtension = new StringBuilder().append(fileName).append(".").append(extension).toString();
		
		// needs to query the new API
		OutputContentFile file = contentService.getContentFile(fileType, fileNameAndExtension);
		if(file!=null) {
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameAndExtension + "\"");
			byte[] bytes = file.getFile().toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			OutputStream os = response.getOutputStream();
			os.write(bytes);
			 // 这里主要关闭。
			os.close();
//			return null;
			//return file.getFile().toByteArray();
		} else {
			LOGGER.debug("File not found " + fileName + "." + extension);
			response.sendError(404, Constants.FILE_NOT_FOUND);
			//return null;
		}
	}
	
	/**
	 * 下载附件
	 * @param fileType 文件类型
	 * @param fileName
	 * @param extension
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 */
	@RequestMapping("/files/downloads/{fileType}/{fileName}.{extension}")
	public @ResponseBody void downloadFile(@PathVariable final String fileType, @PathVariable final String fileName, @PathVariable final String extension, HttpServletRequest request, HttpServletResponse response) throws Exception {

		FileContentType type = FileContentType.valueOf(fileType);
		
		String fileNameAndExtension = new StringBuilder().append(fileName).append(".").append(extension).toString();
		
		// needs to query the new API
		OutputContentFile file = contentService.getContentFile(type, fileNameAndExtension);
		if(file!=null) {
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameAndExtension + "\"");
			byte[] bytes = file.getFile().toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			OutputStream os = response.getOutputStream();
			os.write(bytes);
			 // 这里主要关闭。
			os.close();
//			return null;
			//return file.getFile().toByteArray();
		} else {
			LOGGER.debug("File not found " + fileName + "." + extension);
			response.sendError(404, Constants.FILE_NOT_FOUND);
			//return null;
		}
	}
	/**
	 * 预览附件，除了图片格式，pdf，txt附件采用预览方式，其他附件均采用下载方式
	 * @param fileName
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value={"/files/previewbyext/{fileName}.{extension}"}, method=RequestMethod.GET)
	public @ResponseBody byte[] previewFileByExt(@PathVariable final String fileName, @PathVariable String extension, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ftype=request.getParameter("ftype");
		return previewFile(fileName, extension, response,ftype);
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/files/preview/{fileName}.{extension}", method=RequestMethod.GET)
	public String list(@PathVariable final String fileName, @PathVariable String extension ,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ftype=request.getParameter("ftype");
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("document", "document");
		model.addAttribute("activeMenus",activeMenus);
		model.addAttribute("aname", fileName+"."+extension);
		model.addAttribute("ftype", ftype);
		//model.addAttribute("context", previewFile(fileName, extension, response));
		//request.setAttribute("context", previewFile(fileName, extension, response));
		if(extension.equalsIgnoreCase("pdf")){
			return "ddoc-prepdf";
		}else if(extension.equalsIgnoreCase("txt")){
			return "ddoc-pretxt";
		}
		return "ddoc-preimg";
	}
	/*
	@RequestMapping(value={"/files/preview/{fileName}"})
	public @ResponseBody byte[] previewFile(@PathVariable final String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String[] array = fileName.split("/."); 
		return previewFile(fileName, "", response);
	}*/
	
	private byte[] previewFile(final String fileName, String extension, HttpServletResponse response,String ftype)
			throws Exception {
		
		FileContentType fileType = FileContentType.PRODUCT_DIGITAL;
		if(ftype!=null && StringUtils.isNotBlank(ftype)){
			fileType = FileContentType.valueOf(ftype);
		}
		String fileNameAndExtension;
		if (StringUtils.isBlank(extension)) {
			fileNameAndExtension = new StringBuilder().append(fileName).toString();
		} else {
			fileNameAndExtension = new StringBuilder().append(fileName).append(".").append(extension).toString();
		}
		
		// needs to query the new API
		OutputContentFile file = contentService.getContentFile(fileType, fileNameAndExtension);
		if(file!=null) {
			//text
			if(extension.equalsIgnoreCase("txt")){
				String code = this.codeString(file.getFile());
				String tmep = new String(file.getFile().toByteArray(),code);
				return tmep.getBytes("UTF-8");
			}
			return file.getFile().toByteArray();
		} else {
			LOGGER.debug("File not found " + fileName + "." + extension);
			response.sendError(404, Constants.FILE_NOT_FOUND);
			return null;
		}
	}
	
	/**
	 * Requires admin with roles admin, superadmin or product
	 * @param storeCode
	 * @param fileName
	 * @param extension
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping("/admin/files/downloads/{fileName}.{extension}")
	public @ResponseBody byte[] downloadAdminProduct(@PathVariable final String fileName, @PathVariable final String extension, HttpServletRequest request, HttpServletResponse response) throws Exception {

		FileContentType fileType = FileContentType.PRODUCT_DIGITAL;
		
		StringBuffer fileNameAndExtension = new StringBuffer().append(fileName);
				if(!StringUtils.isBlank(extension)){
					fileNameAndExtension.append(".").append(extension).toString();
				}
				
		
		// needs to query the new API
		OutputContentFile file = contentService.getContentFile(fileType, fileNameAndExtension.toString());
		if(file!=null) {
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameAndExtension + "\"");
//			byte[] bytes = file.getFile().toByteArray();
			//response.setHeader("Content-Length", String.valueOf(bytes.length));
			OutputStream os = response.getOutputStream();
//			os.write(bytes);
			 // 这里主要关闭。
			os.close();
//			return null;
			return file.getFile().toByteArray();
		} else {
			LOGGER.debug("File not found " + fileName + "." + extension);
			response.sendError(404, Constants.FILE_NOT_FOUND);
			return null;
		}
	}
	
	/**
     * 判断文件的编码格式
     * 
     * @param fileName
     *            :file
     * @return 文件编码格式
     * @throws Exception
     */
    private  String codeString(ByteArrayOutputStream bin) throws Exception {
    	ByteArrayInputStream swapStream = new ByteArrayInputStream(bin.toByteArray());
    	
        int p = (swapStream.read() << 8) + swapStream.read();
        String code = null;
 
        switch (p) {
        case 0xefbb:
            code = "UTF-8";
            break;
        case 0xfffe:
            code = "Unicode";
            break;
        case 0xfeff:
            code = "UTF-16BE";
            break;
        default:
            code = "GBK";
        }
 
        return code;
    }

}
