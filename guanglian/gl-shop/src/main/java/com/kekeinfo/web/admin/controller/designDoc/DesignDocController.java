package com.kekeinfo.web.admin.controller.designDoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.designdoc.model.Designdoc;
import com.kekeinfo.core.business.designdoc.service.DesignDocService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DownloadUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class DesignDocController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DesignDocController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private DesignDocService designDocService;
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/ddoc/list.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		model.addAttribute("ddoc",new Designdoc());
		setMenu(model,request);
		return "csite-ddoc";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/water/ddoc/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getDailys(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response,Locale locale) {
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		//查询条件
		String ftype =  request.getParameter("ftype");
		List<Object[]> where =null;
		if(StringUtils.isNotBlank(ftype) && !ftype.equalsIgnoreCase("-1")){
			where = new ArrayList<>();
			where.add(new Object[]{"dtype", ftype});
		}
		
		//指定根据什么条件进行模糊查询
		List<String> attributes = new ArrayList<String>();
		attributes.add("tite"); //名称
		attributes.add("content");
		HashMap<String, String> orderby = new HashMap<String, String>();
		Map<String, String> fetches = new HashMap<String, String>();
		fetches.put("user", "left");
		orderby.put("auditSection.dateModified", "desc");
		Entitites<Designdoc> list = designDocService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
				Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, fetches, true);
		
		DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
		dt.setsEcho(dataTableParam.getsEcho()+1);
		dt.setiTotalDisplayRecords(list.getTotalCount());
        dt.setiTotalRecords(list.getTotalCount());
        if(list.getEntites()!=null && list.getEntites().size()>0) {
        	if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")){
        		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
        		
        		for(Designdoc dDoc : list.getEntites()) {
        			Map<String, Object> entry = new HashMap<String, Object>();
    				entry.put("id", dDoc.getId());
    				entry.put("tite", dDoc.getTite());
    				entry.put("content", dDoc.getContent());
    				entry.put("dtype", dDoc.getDtype());
    				entry.put("uname", dDoc.getUser().getFirstName());
    				entry.put("dateCreated", DateFormatUtils.format(dDoc.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
    				entry.put("dateModified", DateFormatUtils.format(dDoc.getAuditSection().getDateModified(), "yyyy-MM-dd HH:mm:ss"));
    				entry.put("fileName", dDoc.getFileName());
    				entry.put("fileType", dDoc.isFileType());
        			if(dDoc.getUser().getId().equals(user.getId())){
        				entry.put("sstatus", 1);
    				}else{
    					entry.put("sstatus", 0);
    				}
        			dt.getAaData().add(entry);
    			}
        	}else{
        		for(Designdoc dDoc : list.getEntites()) {
        			Map<String, Object> entry = new HashMap<String, Object>();
    				entry.put("id", dDoc.getId());
    				entry.put("tite", dDoc.getTite());
    				entry.put("content", dDoc.getContent());
    				entry.put("dtype", dDoc.getDtype());
    				entry.put("uname", dDoc.getUser().getFirstName());
    				entry.put("dateCreated", DateFormatUtils.format(dDoc.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
    				entry.put("dateModified", DateFormatUtils.format(dDoc.getAuditSection().getDateModified(), "yyyy-MM-dd HH:mm:ss"));
    				entry.put("fileName", dDoc.getFileName());
    				entry.put("fileType", dDoc.isFileType());
    				entry.put("sstatus", 1);
    				dt.getAaData().add(entry);
    			}
        	}
		}
        
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(dt);
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/ddoc/save.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String save(@ModelAttribute("dDoc") Designdoc dDoc, @RequestParam(required=true, value="fileupload") CommonsMultipartFile uploadfile, Model model, HttpServletRequest request, HttpServletResponse response)  {
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		AjaxResponse resp = new AjaxResponse();
		try {
			dDoc.getAuditSection().setModifiedBy(request.getRemoteUser());
			dDoc.setUser(user);
			//附件处理
			if(!uploadfile.isEmpty()){
				String originalName = uploadfile.getOriginalFilename();
        		String name = RadomSixNumber.getImageName(originalName);
        		String uploadContentType = uploadfile.getContentType();
        		//文件类型是否可以预览
        		dDoc.setFileType(DownloadUtils.canPreview(uploadContentType));
        		
        		dDoc.setDigital(uploadfile.getInputStream());
        		dDoc.setFileName(name);
			}
			designDocService.saveOrUpdate(dDoc);;
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (ServiceException e ) {
			LOGGER.error("Error while save ddoc ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/ddoc/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.valueOf(listId[i]);
				try {
					designDocService.delelteByDic(id);
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					resplist.add(resp);
				} catch (Exception e) {
					e.printStackTrace();
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resplist.add(resp);
				}
			}
		}
		return resplist;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("document", "document");
		model.addAttribute("activeMenus",activeMenus);
	}
}
