package com.kekeinfo.web.admin.controller.amessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kekeinfo.core.business.appmessage.model.MessageU;
import com.kekeinfo.core.business.appmessage.service.MessageUService;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.MessageTypeEnum;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.utils.DataTableParameter;


@Controller
public class AMessageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AMessageController.class);
	@Autowired UserService userService;
	@Autowired private MessageUService sessageUService;
	
	
	@RequestMapping(value = "/water/amessage/list.html", method = RequestMethod.GET)
	public String listamessages(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		MessageU amessage = new MessageU();
		model.addAttribute("amessage", amessage);
		
		model.addAttribute("mtypes", MessageTypeEnum.values());
		return "admin-amessage";
	}
	@RequestMapping(value = "/water/amessage/app.html", method = RequestMethod.GET)
	public String app(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<MessageTypeEnum> mes=new ArrayList<>();
		String type=request.getParameter("type");
		if(StringUtils.isNotBlank(type)){
			if(type.equalsIgnoreCase("monitor")){
				mes.add(MessageTypeEnum.EVerifyNotice);
				mes.add(MessageTypeEnum.GIssuesNotice);
				mes.add(MessageTypeEnum.MFeedback);
				model.addAttribute("mtypes", mes);
				return "monitor-appmessage";
			}else if(type.equalsIgnoreCase("guard")){
				mes.add(MessageTypeEnum.GIssuesNotice);
				mes.add(MessageTypeEnum.GJobA);
				mes.add(MessageTypeEnum.GAlarm);
				model.addAttribute("mtypes", mes);
				return "guard-appmessage";
			}
			else if(type.equalsIgnoreCase("water")){
				mes.add(MessageTypeEnum.WCommet);
				mes.add(MessageTypeEnum.WWarning);
				mes.add(MessageTypeEnum.WPower);
				model.addAttribute("mtypes", mes);
				return "water-appmessage";
			}
		}
		
		model.addAttribute("mtypes", MessageTypeEnum.values());
		return "admin-appmessage";
	}

	@RequestMapping(value = "/water/amessage/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getAMessage(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<MessageU> dt = new DataTable<MessageU>();
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		String status =request.getParameter("statu");
		String mtype =request.getParameter("mtype");
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("message.message"); //名称
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("message.dateCreated", "desc");
			List<Object[]> where =new ArrayList<>();
			where.add(new Object[]{"user.id", user.getId()});
			if(StringUtils.isNotBlank(status)){
				where.add(new Object[]{"statu", status});
			}
			if(StringUtils.isNotBlank(mtype)){
				where.add(new Object[]{"message.mtype", MessageTypeEnum.valueOf(mtype)});
			}
			Entitites<MessageU> list = sessageUService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null, true);
			
			if(list!=null && list.getTotalCount()>0){
				for(MessageU am:list.getEntites()){
					am.getMessage().setTypename(am.getMessage().getMtype().getName());
				}
			}
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging amessages", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE); 
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}
	
	@RequestMapping(value = "/water/amessage/applist.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String applist( HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		Map<String, Object> entry_camera = new HashMap<String, Object>();
		int page=0;
		String pages =request.getParameter("page");
		if(StringUtils.isNotBlank(pages)){
			page=Integer.parseInt(pages);
		}
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		try { // 指定根据什么条件进行模糊查询
			
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("message.dateCreated", "desc");
			List<Object[]> where =new ArrayList<>();
			where.add(new Object[]{"user.id", user.getId()});
			String mtype =request.getParameter("mtype");
			if(StringUtils.isNotBlank(mtype)){
				where.add(new Object[]{"message.mtype", MessageTypeEnum.valueOf(mtype)});
			}
			Entitites<MessageU> list = sessageUService.getPageListByAttributesLike(null, null, 5, 
					Long.valueOf(page*5).intValue(), orderby, where, null, true);
			if(list!=null && list.getTotalCount()>0){
				for(MessageU am:list.getEntites()){
					am.getMessage().setTypename(am.getMessage().getMtype().getName());
				}
			}
			entry_camera.put("data",list.getEntites());
			entry_camera.put("total", list.getTotalCount());
		} catch (Exception e) {
			LOGGER.error("Error while paging amessages", e);
			//resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json =mapper.writeValueAsString(entry_camera);
		return json;
	}
/**
	@RequestMapping(value = "/water/amessage/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveAMessage(@ModelAttribute("amessage") AMessage amessage, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			amessageService.saveOrUpdate(amessage);
		} catch (ServiceException e) {
			LOGGER.error("Error while save AMessage", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}**/

	@RequestMapping(value = "/water/amessage/readed.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteAMessage(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					MessageU amessage = sessageUService.getById(id);
					if (amessage != null) {
						amessage.setStatu(1);
						sessageUService.update(amessage);
						//不再发消息，表示已经确认
						if(amessage.getMessage().getMtype().equals(MessageTypeEnum.GAlarm) ||
								amessage.getMessage().getMtype().equals(MessageTypeEnum.GJobA)){
							GJob gjob=amessage.getGjob();
							gjob.setCstatu(1);
						}
						
						/**
						if(amessage.getPids()!=null && StringUtils.isNotBlank(amessage.getPids())){
							User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
							//发确认消息
							String[] pid =amessage.getPids().split(",");
							List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
							Department de = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_MONITOR);
							if(listen!=null && listen.size()>0){
								for(GuardEntity g:listen){
									for(String s:pid){
										if(g.getId().equals(Long.parseLong(s))){
											AMessage am=this.setMu(amessage.getMessage().getMtype(), g.getName(), user.getFirstName());
											MessageU um=new MessageU();
											um.setMessage(am);
											um.setUser(de.getUser());
											sessageUService.sendconform(am, um);
										}
									}
									
								}
							}
						}*/
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						resplist.add(resp);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (e.getMessage().startsWith("Cannot delete or update a parent row")) {
						resp.setStatus(9997);
					} else {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					}
					resplist.add(resp);
				}
			}
			//重新设置用户
			 //重新设置user
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			  user  = userService.getByUserName(user.getAdminName());
			  request.getSession().setAttribute(Constants.ADMIN_USER, user);
			  int unread =sessageUService.read(user.getAdminName());
			  request.getSession().setAttribute(Constants.USER_UN_READ, unread);
		}
		return resplist;
	}
	/**
	private AMessage setMu(MessageTypeEnum mu,String me,String username) throws ServiceException{
		AMessage am =new AMessage();
		switch (mu) {
		case GAlarm:
			am.setMtype(MessageTypeEnum.GConFORM);
			me+="项目工作提醒，"+username+"已确认";
			break;
		case GJobA:
			am.setMtype(MessageTypeEnum.GConANRN);
			me+="项目工作安排，"+username+"已确认";
			break;	
		default:
			break;
		}
		if(am.getMtype()==null){
			return null;
		}
		am.setDateCreated(new Date());
		am.setTitle(me);
		am.setMessage("");
		return am;
	}
*/
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("amessage", "amessage");
		activeMenus.put("amessage-list", "amessage-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("amessage");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
