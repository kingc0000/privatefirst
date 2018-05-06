package com.kekeinfo.web.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.OutputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.constants.Constants;
import com.kekeinfo.core.utils.CoreConfiguration;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.utils.DateUtil;
import com.kekeinfo.web.utils.RadomSixNumber;



@Controller
public class ImagesController {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ImagesController.class);
	

	@Autowired
	private CoreConfiguration configuration;
	
	@Autowired
	private ContentService contentService;
	
	
	
	/**
	 * Logo, content image
	 * @param storeId
	 * @param imageType (LOGO, CONTENT, IMAGE, DAILY, PROPERTY)
	 * @param imageName
	 * @return
	 * @throws IOException
	 * @throws ServiceException 
	 */
	@RequestMapping("/static/{storeCode}/{imageType}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException, ServiceException {

		// example -> /static/mystore/CONTENT/myImage.png
		
		FileContentType imgType = FileContentType.valueOf(imageType);
		
/*		if(FileContentType.LOGO.name().equals(imageType)) {
			imgType = FileContentType.LOGO;
		}
		
		if(FileContentType.IMAGE.name().equals(imageType)) {
			imgType = FileContentType.IMAGE;
		}
		
		if(FileContentType.PROPERTY.name().equals(imageType)) {
			imgType = FileContentType.PROPERTY;
		}
		
		if(FileContentType.DAILY.name().equals(imageType)) { //日志图片
			imgType = FileContentType.DAILY;
		}*/
		
		OutputContentFile image =contentService.getContentFile(imgType, new StringBuilder().append(imageName).append(".").append(extension).toString());
		
		
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}
	

	
	
	/**
	 * 通用的图片上传功能，不做权限控制，配合wysihtml5控件使用
	 * @param baseStore
	 * @param result
	 * @param contentImages
	 * @param model
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/images/upload.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody String uploadImages(@RequestParam(value="pict") List<MultipartFile> files,
			@RequestParam(required=false, value="csrf-token") String token, HttpServletRequest request, Locale locale) throws Exception
	{

		AjaxResponse resp = new AjaxResponse();
		if (files == null || files.isEmpty()) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage("没有文件上传，请重新核实！");
			return resp.toJSONString();
		} 
		String maxHeight = configuration.getProperty("WYSIHTML5_IMAGE_MAX_HEIGHT_SIZE");
		String maxWidth = configuration.getProperty("WYSIHTML5_IMAGE_MAX_WIDTH_SIZE");
		String maxSize = configuration.getProperty("WYSIHTML5_IMAGE_MAX_SIZE");
		String imageFormats = configuration.getProperty("WYSIHTML5_IMAGE_FORMATS");
		String savePath = configuration.getProperty("WYSIHTML5_IMAGE_SAVE_PATH");
		String _savePath = savePath + Constants.SLASH + DateUtil.formatDate(new Date(), "yyyyMMdd");
		if(files!=null && files.size()>0) {
			int i = 0;
			for (Iterator<MultipartFile> iterator = files.iterator(); iterator.hasNext();) {
				MultipartFile file = (MultipartFile) iterator.next();
				String imageName = file.getOriginalFilename();
				InputStream inputStream = file.getInputStream();
				BufferedImage image = ImageIO.read(inputStream);
				
				if(!StringUtils.isBlank(maxHeight)) {
					int maxImageHeight = Integer.parseInt(maxHeight);
					if(image.getHeight()>maxImageHeight) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
						resp.setStatusMessage("图片高度太大，请处理后重新上传，图片高度不允许超过：" + maxImageHeight);
						return resp.toJSONString();
					}
				}
				
				if(!StringUtils.isBlank(maxWidth)) {
					int maxImageWidth = Integer.parseInt(maxWidth);
					if(image.getWidth()>maxImageWidth) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
						resp.setStatusMessage("图片宽度太大，请处理后重新上传，图片宽度不允许超过：" + maxImageWidth);
						return resp.toJSONString();
					}
				}
				
				if(!StringUtils.isBlank(maxSize)) {
					int maxImageSize = Integer.parseInt(maxSize);
					if(file.getSize()>maxImageSize) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
						resp.setStatusMessage("图片过大，请处理后重新上传，图片大小不允许超过：" + maxImageSize/(1024) + "k");
						return resp.toJSONString();
					}
				}
				
				//判断图片格式
				String filename = file.getOriginalFilename();
				filename.lastIndexOf(filename);
				//获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名  
		         String ext = filename.substring(filename.lastIndexOf(".")+1, filename.length());  
		         //对扩展名进行小写转换  
		         ext = ext.toLowerCase(); 
		         if (imageFormats.indexOf(ext) == -1) {
		        	 resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
						resp.setStatusMessage("上传的图片格式不正确，支持的图片格式为：" + imageFormats);
						return resp.toJSONString();
		         }
		         
		         //保存文件
		         String randName = RadomSixNumber.getImageName(imageName);
		         String realPath = request.getSession().getServletContext().getRealPath(_savePath);
		         File saveFile = new File(realPath, randName);
		         //inputstream读取流，不能重复读取，指针不会退，如同导出杯子的水
		         FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
		         resp.addEntry("src_"+i, request.getContextPath() + _savePath + Constants.SLASH + randName); //将图片保存路径返回到客户端
		         i++;
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				
		}
		
			return resp.toJSONString();	
	}


}
