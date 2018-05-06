package com.kekeinfo.web.admin.controller.content;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.controller.ControllerConstants;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.RadomSixNumber;

/**
 * Manage static content type image
 * - Add images
 * - Remove images
 * @author Yong Chen
 *
 */
@Controller
public class ContentImageController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentImageController.class);
	
	@Autowired
	private ContentService contentService;
	
	/**
	 * Entry point for the file browser used from the javascript
	 * content editor
	 * 针对CKeditor控件给我们提供的值，当我们设置editorObj.config.filebrowserImageUploadUrl = "新地址"时CKeditor会自动为我们增加几个个url参数： 
		新地址?CKEditor=Text&CKEditorFuncNum=2&langCode=zh-cn，将几个url参数返回至页面
		CKEditor: CKEditor的name名称，CKEditorFuncNum： 对应调用的功能控件编号
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value={"/admin/content/fileBrowser.html"}, method=RequestMethod.GET)
	public String displayFileBrowser(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//针对ckeditor
		model.addAttribute("CKEditor", request.getParameter("CKEditor"));
		model.addAttribute("CKEditorFuncNum", request.getParameter("CKEditorFuncNum"));
		model.addAttribute("langCode", request.getParameter("langCode"));
		return ControllerConstants.Tiles.ContentImages.fileBrowser;
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/content/image/upload.shtml", method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody void saveProductImage(@RequestParam("upload") MultipartFile file,HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception{
		
		StringBuilder res = new StringBuilder();
		//res.append("<script type=\"text/javascript\">");
		String CKEditorFuncNum = request.getParameter("CKEditorFuncNum");
		res.append("window.parent.CKEDITOR.tools.callFunction(").append(CKEditorFuncNum);
		PrintWriter out = response.getWriter();  
		out.println("<script type=\"text/javascript\">"); 
		String fileName = file.getOriginalFilename();  
		String uploadContentType = file.getContentType(); 
		try{
		
		
			if (!uploadContentType.equals("image/pjpeg")  
			&& !uploadContentType.equals("image/jpeg") 
			&& !uploadContentType.equals("image/png") 
			&& !uploadContentType.equals("image/x-png")
			&& !uploadContentType.equals("image/gif")
			&& !uploadContentType.equals("image/bmp")) {  
				res.append(",'',").append("'文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");
				out.println(res.toString());
				out.println("</script>");
				
		         return;
				//return res.toString();
			} 
			
			if (file.getSize() > 1024 * 1024 * 5) {  
				res.append(",'',").append("'文件大小不得大于5M');");
				//return res.toString();  
				out.println(res.toString());
				out.println("</script>");
		         return;
				}  
			
			final List<InputContentFile> contentImagesList=new ArrayList<InputContentFile>();
			 InputContentFile cmsContentImage = new InputContentFile();
	         cmsContentImage.setFileName( RadomSixNumber.getImageName(fileName));
	         cmsContentImage.setMimeType( file.getContentType() );
	        // cmsContentImage.setFile(file.getInputStream());
	       //压缩
	         cmsContentImage.setFile(ImageUtils.ByteArrayOutputStream(file.getInputStream()));
	         cmsContentImage.setFileContentType(FileContentType.IMAGE);
	         contentImagesList.add( cmsContentImage);    
	         
	         ;
	         contentService.addContentFiles( contentImagesList );
	         //contentService.getContentFilesNames(FileContentType.IMAGE);
	         //contentService.getContentFiles( FileContentType.IMAGE);
	         String path=request.getSession().getServletContext().getContextPath();
	         res.append(",'").append(path).append("/static").append("/DEFAULT").append("/").append(FileContentType.IMAGE.name()).append("/").append(cmsContentImage.getFileName());
	         res.append("','')");
	         //res.append("');").append("window.close();</script>");
	         out.println(res.toString());
	         out.println("</script>");
	         return;
	         //return res.toString();
		}catch (Exception e){
			res.append(",'',").append("'上传文件失败，请重试！');").append("</script>");
			//return res.toString(); 
		}
		
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/content/images/paging.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody List<String> pageImages(HttpServletRequest request, HttpServletResponse response) {
		List<String> inames;
		try {
			inames = contentService.getContentFilesNames( FileContentType.IMAGE);
		} catch (ServiceException e) {			
			return null;
		}
		return inames;
	}
	
	/**
	 * Removes a content image from the CMS
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value="/admin/content/removeImage.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String removeImage(@RequestParam("name") String name,HttpServletRequest request, HttpServletResponse response, Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		try {
			if (StringUtils.isNotBlank(name)) {
				contentService.removeFile(FileContentType.IMAGE, name);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
}
