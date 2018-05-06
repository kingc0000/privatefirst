package com.kekeinfo.web.admin.controller.mpoint;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.model.MBaseType;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.business.monitor.model.WaterLineType;
import com.kekeinfo.core.business.monitor.service.MBaseTypeService;
import com.kekeinfo.core.business.monitor.service.WaterLineTypeService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.MonitorEntity;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class MPointController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MPointController.class);
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private GroupService groupService;
	@Autowired private MBaseTypeService mBaseTypeService;
	@Autowired private WaterLineTypeService  waterLineTypeService;

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/warter/mpoint/list.html", method = RequestMethod.GET)
	public String listmonproblems(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		setMenu(model, request);
		String mid = request.getParameter("mid");
		setMenu(model, request);
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
        MonitorEntity me=null;
		if(StringUtils.isNotBlank(mid)){
			List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
			for(MonitorEntity m:listen){
				if(m.getId().equals(Long.parseLong(mid))){
					me = m; 
					model.addAttribute("mentity", m);
					break;
				}
			}
		}
		if(me!=null){
			if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
	        	if(request.isUserInRole("EDIT-PROJECT")){
	        		List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
	        		hasRight=pnodeUtils.hasProjectRight(request, egroups, (BaseEntity)me);
	        	}
			}else{
				hasRight=true;
			}
		}
		if(me!=null){
			Monitor montior=null;
			List<MBaseType> mlist = mBaseTypeService.getByMid(me.getId());
			if(mlist==null ||mlist.size()==0){
				montior =new Monitor();
				montior.setId(me.getId());
				mlist=new ArrayList<>();
				for(MPointEnumType e:MPointEnumType.values()){
					MBaseType mt=new MBaseType();
					mt.setMonitor(montior);
					mt.setMtype(e);
					mBaseTypeService.save(mt);
					mlist.add(mt);
				}
			}
			List<WaterLineType> wlist =waterLineTypeService.getByMid(me.getId());
			if(wlist==null || wlist.size()==0){
				wlist=new ArrayList<>();
				if(montior==null){
					montior =new Monitor();
					montior.setId(me.getId());
				}
				List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_MONITOR_LINE);
				for(BasedataType bt:blist){
					WaterLineType wt =new WaterLineType();
					wt.setMonitor(montior);
					wt.setBaseType(bt);
					waterLineTypeService.save(wt);
					wlist.add(wt);
				}
			}
			/*for(MBaseType m:mlist){
				for(MPointEnumType e:MPointEnumType.values()){
					if(m.getMtype().equals(e)){
						m.setName(e.getName());
						break;
					}
				}
			}*/
			
			model.addAttribute("ms", mlist);
			model.addAttribute("btype", wlist);
		}
	
		request.setAttribute("activeCode", "mpoint"); //指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("mid", mid);
		
		LOGGER.info("警戒值");
		return "admin-mpoint";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/warter/mpoint/savempoint.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getMonProblem(HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException, ServiceException {
		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		String pid =request.getParameter("pid");
		String dailyVar=request.getParameter("dailyVar");
		String totalValue=request.getParameter("totalValue");
		if(StringUtils.isNotBlank(pid) && StringUtils.isNotBlank(totalValue) && StringUtils.isNotBlank(dailyVar)){
			MBaseType mp =mBaseTypeService.getById(Long.parseLong(pid));
			if(mp!=null){
				mp.setDailyVar(new BigDecimal(dailyVar));
				mp.setTotalValue(new BigDecimal(totalValue));
				mBaseTypeService.update(mp);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
		}
		
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/warter/mpoint/savebtype.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getimages(HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException, ServiceException {
		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		String pid =request.getParameter("bid");
		String dailyVar=request.getParameter("dailyVar");
		String totalValue=request.getParameter("totalValue");
		if(StringUtils.isNotBlank(pid) && StringUtils.isNotBlank(totalValue) && StringUtils.isNotBlank(dailyVar)){
			WaterLineType bt = waterLineTypeService.getById(Long.parseLong(pid));
			if(bt!=null){
				bt.setDailyVar(new BigDecimal(dailyVar));
				bt.setTotalValue(new BigDecimal(totalValue));
				waterLineTypeService.update(bt);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}
		}
		return resp.toJSONString();
	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("monitor", "monitor");
		model.addAttribute("activeMenus", activeMenus);
	}
}
