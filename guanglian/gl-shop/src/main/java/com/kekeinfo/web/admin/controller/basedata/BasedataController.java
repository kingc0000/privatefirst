package com.kekeinfo.web.admin.controller.basedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.basedata.service.BaseDataService;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.guard.service.GuardService;
import com.kekeinfo.core.business.monitor.service.MonitorService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.service.ResetJob;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.event.BeanEvent;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;

@Controller
public class BasedataController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BasedataController.class);
	
	@Autowired
	private BaseDataService baseDataService;
	@Autowired CSiteService cSiteService;
	@Autowired GuardService guardService;
	@Autowired MonitorService monitorService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
    private ResetJob job;
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value="/water/basedata/list.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		model.addAttribute("basedata",new BasedataType());
		setMenu(model,request);
		List<String> attributes = new ArrayList<String>();
		attributes.add("distinct(TYPE)");
		List<Object[]> bbtype= baseDataService.getBySql(attributes, "BASEDATA_TYPE", "");
		List<String> btype = new ArrayList<String>();
		for (Object type : bbtype) {
			btype.add(type.toString());
		}
		model.addAttribute("btype", btype);
		return "water-basedata";
	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = "/water/basedata/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	DataTable<BasedataType> getBasedatas(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response,Locale locale) {
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		
		//指定根据什么条件进行模糊查询
		List<String> attributes = new ArrayList<String>();
		attributes.add("name"); //名称
		attributes.add("type");
		attributes.add("value");
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateModified", "desc");
		Entitites<BasedataType> list = baseDataService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
				Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby);
		
		DataTable<BasedataType> dt = new DataTable<BasedataType>();
		dt.setsEcho(dataTableParam.getsEcho()+1);
		dt.setiTotalDisplayRecords(list.getTotalCount());
        dt.setiTotalRecords(list.getTotalCount());
        dt.setAaData(list.getEntites());
        return dt;
	}

	@PreAuthorize("hasRole('SUPERADMIN')") 
	@RequestMapping(value="/water/basedata/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("basedata") BasedataType basedata, Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			//banner.getAuditSection().setModifiedBy(request.getRemoteUser());
			boolean notnew =false;
			if(basedata.getType().equalsIgnoreCase(Constants.BD_PROJECT_TYPE) && basedata.getId()!=null){
				notnew=true;
			}
			baseDataService.saveOrUpdate(basedata);//调用saveOrUpdate，会自动更新审计更新时间，对象做了保存前的监听处理
			ApplicationContext context = SpringContextUtils.getApplicationContext();
			context.publishEvent(new BeanEvent(context, BeanEvent.BASEDATA_SRC));
			if(notnew){
				pnodeUtils.reloadAllCsite(request);
				pnodeUtils.reloadAllGuard();
				pnodeUtils.reloadAllMonitor();
			}
			//重新设置定时
			if(basedata.getType().equals(Constants.BD_GUARD_ALARMTIME)){
				String [] strs =basedata.getValue().split(":");
				//0 30 11 * * ?
				StringBuffer sd =new StringBuffer();
				if(strs.length>=3){
					sd.append(strs[2]);
				}else{
					sd.append("0");
				}
				if(strs.length>=2){
					sd.append(" "+strs[1]);
				}else{
					sd.append(" 0");
				}
				if(strs.length>=1){
					sd.append(" "+strs[0]).append(" * * ?");
					job.restJob(sd.toString(), "autoSendTrigger");
				}
				
			}
		} catch (ServiceException e ) {
			LOGGER.error("Error while save basedata ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value="/water/basedata/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.valueOf(listId[i]);
				try {
					baseDataService.remove(id);
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					resplist.add(resp);
				} catch (Exception e) {
					e.printStackTrace();
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resplist.add(resp);
				}
			}
			ApplicationContext context = SpringContextUtils.getApplicationContext();
			context.publishEvent(new BeanEvent(context, BeanEvent.BASEDATA_SRC));
		}
		return resplist;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		model.addAttribute("activeMenus",activeMenus);
	}
}
