package com.kekeinfo.web.admin.controller.dreport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.kekeinfo.core.business.auditrule.service.AuditRuleService;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.content.model.Content;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.dreport.model.AuditRule;
import com.kekeinfo.core.business.dreport.model.Dattach;
import com.kekeinfo.core.business.dreport.model.Dopinion;
import com.kekeinfo.core.business.dreport.model.Dreport;
import com.kekeinfo.core.business.dreport.service.DreportService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.CommonEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.GroupModel;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class DreportController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DreportController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	private DreportService dreportService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private AuditRuleService auditRuleService;
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/dreport/list.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		model.addAttribute("dreport",new Dreport());
		setMenu(model,request);
		return "water-dreport";
	}
	
	/**
	 * 加载审批列表
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/dreport/listApprove.html", method=RequestMethod.GET)
	public String listApprove(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		model.addAttribute("dreport",new Dreport());
		setMenu(model,request);
		return "water-dreport-approve";
	}
	
	/**
	 * 获取设计报告
	 * 编辑人只能查看自己的报告，部门管理员可以查看所有人的报告
	 * @param aoData
	 * @param request
	 * @param response
	 * @return
	 * @throws ServiceException 
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/dreport/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getDreports(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		//指定根据什么条件进行模糊查询
		List<String> attributes = new ArrayList<String>();
		attributes.add("title"); //名称
		attributes.add("note");
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateModified", "desc");
		List<Object[]> where = new ArrayList<Object[]>();
		Map<String, String> fetches = new HashMap<String, String>();
		//fetches.put("user", "LEFT");
		if (!(request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN"))) {
			boolean isSelectAll = false;
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			if(departmentNodes!=null && departmentNodes.size()>0){
				//全选状态
				for(DepartmentNode pnode:departmentNodes){
					if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll()==true){ //拥有所有部门权限，因此直接返回地下水部门下的所有项目
						isSelectAll=true;
						break;
					}
					//拥有地下水部门权限，因此直接返回地下水部门下的所有项目
					if(pnode.getDepartmentid().longValue()==waterDept.getId().longValue() && pnode.getType().equalsIgnoreCase("department") && pnode.isAll()==true){
						isSelectAll=true;
						break;
					}
				}
			}
			if (!isSelectAll) {
				where.add(new String[]{"user.id", user.getId().toString(), "0"});
			}
		}
		
		Entitites<Dreport> list = dreportService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
				Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, fetches, true);
		
		DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
		dt.setsEcho(dataTableParam.getsEcho()+1);
		dt.setiTotalDisplayRecords(list.getTotalCount());
        dt.setiTotalRecords(list.getTotalCount());
        if(list.getEntites()!=null && list.getEntites().size()>0) {
        	dt.setAaData(getDatas(list.getEntites(), request));
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


	/**
	 * 获取待审批报告列表
	 * @param aoData
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/dreport/getApproves.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getApproveDreports(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		Long uid = user.getId();
		Integer[] types = {1, 2}; 
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		List<Dreport> list = dreportService.getApproveDreports(uid, types);
		
		DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
		dt.setsEcho(dataTableParam.getsEcho()+1);
		dt.setiTotalDisplayRecords(list.size());
        dt.setiTotalRecords(list.size());
        if(list!=null && list.size()>0) {
			dt.setAaData(getDatas(list, request));
		}
        //设定用户待办审批数量
        request.getSession().setAttribute(Constants.USER_REPORT_DEAL, list.size());
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(dt);
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 处理Dreport集合，转为json格式
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getDatas(List<Dreport> list, HttpServletRequest request) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for(Dreport report : list) {
			Map<String, Object> entry = new HashMap<String, Object>();
			entry.put("id", report.getId());
			entry.put("title", report.getTitle());
			entry.put("rank", report.getRank());
			entry.put("note", report.getNote());
			entry.put("status", report.getStatus());
			entry.put("userId", report.getUser().getId());
			entry.put("firstName", report.getUser().getFirstName());
			entry.put("modifiedBy", report.getAuditSection().getModifiedBy());
			entry.put("dateCreated", DateFormatUtils.format(report.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
			entry.put("dateModified", DateFormatUtils.format(report.getAuditSection().getDateModified(), "yyyy-MM-dd HH:mm:ss"));
			
			//处理附件
			JSONArray attachArray = new JSONArray();
			Set<Dattach> attaches = report.getAttaches();
			if (CollectionUtils.isNotEmpty(attaches)) {
				for (Dattach attach : attaches) {
					JSONObject data = new JSONObject();
					data.put("id", attach.getId());
					data.put("note", attach.getFileNote());
					data.put("attachName", attach.getFileName());
					data.put("type", attach.getFileType());
					/*StringBuffer filepath = new StringBuffer("");
					filepath.append(request.getContextPath()).append(ImageFilePathUtils.buildFilePathByConentType(attach.getFileName(), FileContentType.DREPORT));
					data.put("filepath", filepath);*/
					attachArray.add(data);
				}
			}
			
			//处理审核意见
			JSONArray array = new JSONArray();
			if (!report.getOpinions().isEmpty()) {
				for(Dopinion opinion : report.getOpinions()) {
					JSONObject data = new JSONObject();
					if (opinion.getDealer()!=null) {
						data.put("dealer", opinion.getDealer().getFirstName());
						data.put("dateModified", DateFormatUtils.format(opinion.getAuditSection().getDateModified(), "yyyy-MM-dd HH:mm:ss"));
						data.put("note", StringUtils.trimToEmpty(opinion.getNote()));
					} else {
						data.put("dealer", "");
						data.put("dateModified", "");
						data.put("note", "");
					}
					data.put("id", opinion.getId());
					data.put("auditType", opinion.getAuditType());
					data.put("dateCreated", DateFormatUtils.format(opinion.getAuditSection().getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
					data.put("result", opinion.getResult());
					
					String users = "";
					for (User u : opinion.getUsers()) {
						users+=u.getFirstName()+"&nbsp;&nbsp;";
					}
					data.put("users", users);
					array.add(data);
				}
			}
			entry.put("attachments", attachArray.toJSONString()); //附件集合
			entry.put("opinions", array.toJSONString()); //审核意见集合
			
			result.add(entry);
		}
		return result;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/dreport/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("dreport") Dreport dreport, @RequestParam(value="attachments", required=false) List<CommonsMultipartFile> attaches,
			@RequestParam(value="attachNotes", required=false) List<String> notes,
			Model model, HttpServletRequest request, HttpServletResponse response)  {
		String delDigitalIds = request.getParameter("delDigitalIds"); //删除的附件ID集合，采用#作为分隔符，例如"attachId1#attachId2#"
		AjaxResponse resp = new AjaxResponse();
		try {
			dreport.getAuditSection().setModifiedBy(request.getRemoteUser());
			if (dreport.getId()==null) {
				User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
				dreport.setUser(user);
			}
			
			//处理新增加的附件
			Set<Dattach> attachSet = new HashSet<Dattach>();
			if(attaches!=null && !attaches.isEmpty()) {
				try {
					for (int i = 0; i < attaches.size(); i++) {
						MultipartFile attach = attaches.get(i);
						if(attach.getSize()<=0) continue; //如果附件大小<=0，则不作处理
						Dattach o = new Dattach();
						o.setDigital(attach.getInputStream());
						String originalName = attach.getOriginalFilename();
						String name = RadomSixNumber.getImageName(originalName);
						o.setFileName(name);
						
						if (notes!=null&&notes.size()>i) {
							if (StringUtils.isNotBlank(notes.get(i))) {
								o.setFileNote(notes.get(i));
							} else {
								o.setFileNote(name);
							}
						} else {
							o.setFileNote(name);
						}
						
						if (originalName.lastIndexOf(".")>0) { //文件后缀名获取文件类型
	            			String suffix = originalName.substring(originalName.lastIndexOf(".")+1);
	            			o.setFileType(suffix);
	            		}
						o.setDreport(dreport);
						attachSet.add(o);
					}
					dreport.setAttaches(attachSet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			dreportService.save(dreport, delDigitalIds);
		} catch (ServiceException e ) {
			LOGGER.error("Error while save dreport ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dreport/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.valueOf(listId[i]);
				try {
					dreportService.deleteById(id);
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
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dreport/templates.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody String getTemplates(HttpServletRequest request) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		//加载报告模板
		List<Content> contents = contentService.listByType(Constants.CTN_REPORT_TYPE);
		for (Content content : contents) {
			Map<String,String> data = new HashMap<String, String>();
			data.put("name", content.getDescription().getName());
			data.put("description", content.getDescription().getDescription());
			resp.addDataEntry(data);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	/**
	 * 送审提交，根据指定的报告状态和选择的审批负责人，创建审批意见空记录，
	 * 对于审核用户提交的，则需要更新原审批意见信息
	 * @param rid 报告id
	 * @param uids 负责审批用户ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/dreport/sendAudit.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody String sendAudit(@RequestParam(required=true, value="rid") String rid, @RequestParam(required=true, value="uids") String uids, HttpServletRequest request) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		String note = StringUtils.stripToEmpty(request.getParameter("note"));
		Dreport report = dreportService.getById(Long.valueOf(rid));
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		if (report!=null) {
			//根据报告获取项目等级和状态
			int status = report.getStatus(); //编制中0、待审核1、待审定2、修订（审核）3、修订（审定）4，结束5，其中待修正为审核或审定过程中退回后的状态
			Dopinion opinion = new Dopinion();
			opinion.setDreport(report);
			
			String[] uidArray = uids.split(",");
			for (String uid : uidArray) {
				User auditUser = new User();
				auditUser.setId(Long.valueOf(uid));
				opinion.getUsers().add(auditUser);
			}
			if (status == 0 || status == 3) { //提交至审核环节
				opinion.setAuditType(1); 
				report.setStatus(1); 
			} else if (status == 1) {//提交至审定环节，同时需要更新审核意见信息
				opinion.setAuditType(2);
				report.setStatus(2); 
				
				//需要更新审核意见信息
				Set<Dopinion> set = report.getOpinions();
				for (Dopinion dopinion : set) {
					if (dopinion.getDealer()==null) { //找到还没有实际处理人的意见记录
						dopinion.setDealer(user);
						dopinion.setNote(note);
						dopinion.setResult(1); //通过
						dopinion.getAuditSection().setDateModified(new Date());
						dopinion.getAuditSection().setModifiedBy(user.getFirstName());
						break;
					}
				}
			} else if (status == 4) {//提交至审定环节
				opinion.setAuditType(2);
				report.setStatus(2); 
			} else {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("报告状态不正确，不允许提交送审！");
				return resp.toJSONString();
			}
			AuditSection as = new AuditSection();
			as.setDateCreated(new Date());
			as.setDateModified(as.getDateCreated());
			as.setModifiedBy(user.getFirstName());
			opinion.setAuditSection(as);
			report.getOpinions().add(opinion);
			dreportService.saveOrUpdate(report);
			
			//设定用户待办审批数量
			updateCount(request, user.getId());
		} else {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("设计报告不存在，请联系管理员");
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	/**
	 * 获取对应审批节点的审核人员集合
	 * @param rid
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/dreport/groups.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody GroupModel<CommonEntity> getGroups(@RequestParam("rid") String rid, HttpServletRequest request, HttpServletResponse response)  {
		GroupModel<CommonEntity> gm = new GroupModel<CommonEntity>();
		gm.setMemo("审核员选择");
		try{
			if (StringUtils.isNotBlank(rid)) {
				Long id = Long.valueOf(rid);
				Dreport report = dreportService.getById(id);
				//根据报告获取项目等级和状态
				int status = report.getStatus(); //编制中0、待审核1、待审定2、修订（审核）3、修订（审定）4，结束5，其中待修正为审核或审定过程中退回后的状态
				String rank = report.getRank();
				String statusMsg = "";
				Entitites<AuditRule> rules = null;
				if (status == 0 || status ==3) { //提交至审核环节
					rules = auditRuleService.getListByAttributes(new String[]{"rank", "auditType"}, new Object[]{rank, "1"}, null);
					statusMsg = "编制/修订";
				} else if (status == 1 || status ==4) {//提交至审定环节
					rules = auditRuleService.getListByAttributes(new String[]{"rank", "auditType"}, new Object[]{rank, "2"}, null);
					statusMsg = "审核/修订";
				} else {
					gm.setStatus(GroupModel.RESPONSE_STATUS_FAIURE);
					gm.setMessage("报告正在审定或结束，请核实报告状态");
					return gm;
				}
				if (rules.getTotalCount()>0) {
					AuditRule rule = rules.getEntites().get(0);
					Set<User> users = rule.getUsers();
					if (users.size()>0) {
						List<CommonEntity> froms = new ArrayList<CommonEntity>();
						for (User user : users) {
							CommonEntity c = new CommonEntity(user.getId(), user.getFirstName(), user.getAdminName(), "", null);
							froms.add(c);
						}
						gm.setFroms(froms);
						return gm;
					} else {
						gm.setStatus(GroupModel.RESPONSE_STATUS_FAIURE);
						gm.setMessage("送审规则（'项目等级="+rank+"',报告状态='"+statusMsg+"'）未配置审核人员，请联系管理员");
						return gm;
					}
				} else {
					gm.setStatus(GroupModel.RESPONSE_STATUS_FAIURE);
					gm.setMessage("未配置送审规则（'rank="+rank+"',status='"+statusMsg+"'），请联系管理员");
					return gm;
				}
			} else {
				LOGGER.error("调用getGroups方法，rid不允许为空");
				gm.setStatus(GroupModel.RESPONSE_STATUS_FAIURE);
				gm.setMessage("设计报告不存在");
				return gm;
			}
		}catch (Exception e){
			e.printStackTrace();
			gm.setStatus(GroupModel.RESPONSE_STATUS_FAIURE);
			gm.setMessage("系统错误，请联系管理员");
			return gm;
		}
	}
	
	/**
	 * 审核人员审核不通过，审定人员审核不通过
	 * @param rid
	 * @param uids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/dreport/sendReject.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody String sendReject(@RequestParam(required=true, value="rid") String rid, HttpServletRequest request) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		String note = StringUtils.stripToEmpty(request.getParameter("note"));
		Dreport report = dreportService.getById(Long.valueOf(rid));
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		if (report!=null) {
			//根据报告获取项目等级和状态
			int status = report.getStatus(); //编制中0、待审核1、待审定2、修订（审核）3、修订（审定）4，结束5，其中待修正为审核或审定过程中退回后的状态
			if (status == 1) { //修订（审核）
				report.setStatus(3); 
			} else if (status == 2) {//提交至修订（审定）
				report.setStatus(4); 
			} else {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("报告状态不正确，不允许提交送审！");
				return resp.toJSONString();
			}
			//需要更新审核意见信息
			Set<Dopinion> set = report.getOpinions();
			for (Dopinion dopinion : set) {
				if (dopinion.getResult()==-1) { //待处理的opinion
					dopinion.setDealer(user);
					dopinion.setNote(note);
					dopinion.setResult(0); //不通过
					dopinion.getAuditSection().setDateModified(new Date());
					dopinion.getAuditSection().setModifiedBy(user.getFirstName());
					break;
				}
			}
			
			dreportService.saveOrUpdate(report);
			//设定用户待办审批数量
			updateCount(request, user.getId());
		} else {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("设计报告不存在，请联系管理员");
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	/**
	 * 审定通过，流程结束
	 * @param rid
	 * @param uids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/dreport/sendComplete.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody String sendComplete(@RequestParam(required=true, value="rid") String rid, HttpServletRequest request) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		String note = StringUtils.stripToEmpty(request.getParameter("note"));
		Dreport report = dreportService.getById(Long.valueOf(rid));
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		if (report!=null) {
			//根据报告获取项目等级和状态
			int status = report.getStatus(); //编制中0、待审核1、待审定2、修订（审核）3、修订（审定）4，结束5，其中待修正为审核或审定过程中退回后的状态
			if (status == 2) { //修订（审核）
				report.setStatus(5); 
			} else {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("报告状态不正确，不允许提交送审！");
				return resp.toJSONString();
			}
			//需要更新审核意见信息
			Set<Dopinion> set = report.getOpinions();
			for (Dopinion dopinion : set) {
				if (dopinion.getResult()==-1) { //待处理的opinion
					dopinion.setDealer(user);
					dopinion.setNote(note);
					dopinion.setResult(1); //通过
					dopinion.getAuditSection().setDateModified(new Date());
					dopinion.getAuditSection().setModifiedBy(user.getFirstName());
					break;
				}
			}
			
			dreportService.saveOrUpdate(report);
			
			//设定用户待办审批数量
			updateCount(request, user.getId());
		} else {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("设计报告不存在，请联系管理员");
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	/**
	 * 更新当前用户报告审批待办事项
	 * @param request
	 * @param userId
	 */
	private void updateCount(HttpServletRequest request, Long userId) {
		Integer[] types = {1, 2}; 
		int count = dreportService.countApproveDreports(userId, types);
		request.getSession().setAttribute(Constants.USER_REPORT_DEAL, count);
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("report", "report");
		model.addAttribute("activeMenus",activeMenus);
	}
}
