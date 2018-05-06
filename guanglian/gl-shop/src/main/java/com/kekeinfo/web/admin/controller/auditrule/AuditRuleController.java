package com.kekeinfo.web.admin.controller.auditrule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import com.kekeinfo.core.business.auditrule.service.AuditRuleService;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.dreport.model.AuditRule;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.entity.CommonEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.GroupModel;
import com.kekeinfo.web.utils.DataTableParameter;

import edu.emory.mathcs.backport.java.util.Arrays;

@Controller
public class AuditRuleController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditRuleController.class);
	
	@Autowired
	private AuditRuleService auditRuleService;
	
	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/auditconfig/list.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		model.addAttribute("auditConfig",new AuditRule());
		setMenu(model,request);
		return "water-audit-config";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/water/auditconfig/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	DataTable<Map<String, Object>> getAuditRules(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response,Locale locale) {
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		List<AuditRule> list = auditRuleService.list();
		
		DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
		dt.setsEcho(dataTableParam.getsEcho()+1);
		dt.setiTotalDisplayRecords(list.size());
        dt.setiTotalRecords(list.size());
        for (AuditRule auditRule : list) {
        	Map<String, Object> entry = new HashMap<String, Object>();
			entry.put("id", auditRule.getId());
			entry.put("name", auditRule.getName());
			entry.put("rank", auditRule.getRank());
			entry.put("auditType", auditRule.getAuditType());
			dt.getAaData().add(entry);
		}
        return dt;
	}

	@PreAuthorize("hasRole('ADMIN')") 
	@RequestMapping(value="/water/auditconfig/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("audit") AuditRule audit, Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			//判断项目等级和审核节点是否已经有定义过
			Entitites<AuditRule> ruleList = auditRuleService.getListByAttributes(new String[]{"auditType", "rank"}, new Object[]{audit.getAuditType(), audit.getRank()}, null);
			if (ruleList.getTotalCount()>0) {
				if(!ruleList.getEntites().get(0).getId().equals(audit.getId())){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
					resp.setErrorString("送审配置规则已经定义过，不允许重复定义");
				} else {
					auditRuleService.update(audit);
				}
			} else {
				auditRuleService.saveOrUpdate(audit);//调用saveOrUpdate，会自动更新审计更新时间，对象做了保存前的监听处理
			}
		} catch (ServiceException e ) {
			LOGGER.error("Error while save audit ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/auditconfig/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.valueOf(listId[i]);
				try {
					auditRuleService.deleteById(id);
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
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/auditconfig/groups.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody GroupModel<CommonEntity> getGroups(@RequestParam("rid") String rid, HttpServletRequest request, HttpServletResponse response)  {
		GroupModel<CommonEntity> gm = new GroupModel<CommonEntity>();
		try{
			if (StringUtils.isNotBlank(rid)) {
				Long id = Long.valueOf(rid);
				AuditRule rule = auditRuleService.getById(id);
				if (rule==null) {
					LOGGER.error("送审规则ID="+rid+"不存在");
					gm.setStatus(GroupModel.RESPONSE_STATUS_FAIURE);
					gm.setMessage("送审规则不存在");
					return gm;
				} else {
					
					List<CommonEntity> froms = new ArrayList<CommonEntity>();
					List<CommonEntity> tos = new ArrayList<CommonEntity>();
					gm.setMemo(rule.getName());
					
					//获取所有的用户集合
					List<User> froms_list = userService.list();
					Set<User> tos_list = rule.getUsers();
					
					if (froms_list!=null && froms_list.size()>0) {
						for (User user : froms_list) {
							CommonEntity c = new CommonEntity(user.getId(), user.getFirstName(), user.getAdminName(), "", null);
							froms.add(c);
						}
					}
					if (tos_list!=null && tos_list.size()>0) {
						for (User user : tos_list) {
							CommonEntity c = new CommonEntity(user.getId(), user.getFirstName(), user.getAdminName(), "", null);
							tos.add(c);
						}
					}
					gm.setFroms(froms);
					gm.setTos(tos);
					return gm;
				}
			} else {
				LOGGER.error("调用getGroups方法，rid不允许为空");
				gm.setStatus(GroupModel.RESPONSE_STATUS_FAIURE);
				gm.setMessage("送审规则不存在");
				return gm;
			}
		}catch (Exception e){
			e.printStackTrace();
			gm.setStatus(GroupModel.RESPONSE_STATUS_FAIURE);
			gm.setMessage("系统错误，请联系管理员");
			return gm;
		}
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/auditconfig/setGroup.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String setGroups(HttpServletRequest request, HttpServletResponse response)  {
		
		String rid = request.getParameter("rid"); //送审规则ID
		String sid = request.getParameter("ids"); //加入送审规则的用户ID集合
		
		AjaxResponse ajaxRespone = new AjaxResponse();
		ajaxRespone.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		try{
			if(StringUtils.isNotBlank(rid)){
				List<String> add = null;
				if (StringUtils.isNotBlank(sid)) {
					String[] ids = sid.split(",");
					
					add = new ArrayList<String>(Arrays.asList(ids));
				}
				int updateSize = auditRuleService.updateAuditRuleConfig(Long.valueOf(rid), add);
				if (updateSize>-1) {
					ajaxRespone.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				}
			}
		} catch (Exception e){
			LOGGER.error("while set group "+e.getMessage());
		}
		return ajaxRespone.toJSONString();
	}
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("auditConfig", "auditConfig");
		
		model.addAttribute("activeMenus",activeMenus);
	}
}
