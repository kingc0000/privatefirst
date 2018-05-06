package com.kekeinfo.web.admin.controller.dataconf;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.deformmonitor.service.DeformmonitorService;
import com.kekeinfo.core.business.dewatering.service.DewateringService;
import com.kekeinfo.core.business.gateway.model.Gateway;
import com.kekeinfo.core.business.gateway.service.GatewayService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.point.service.PointService;
import com.kekeinfo.core.business.pointlink.model.DeformLink;
import com.kekeinfo.core.business.pointlink.model.DewateringLink;
import com.kekeinfo.core.business.pointlink.model.InvertedLink;
import com.kekeinfo.core.business.pointlink.model.ObserveLink;
import com.kekeinfo.core.business.pointlink.model.PumpLink;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.entity.filter.PointInfoFilter;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class DataconfController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataconfController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private PointService pointService;
	@Autowired private GatewayService gatewayService;
	@Autowired private PumpwellService pumpwellService;
	@Autowired private DewateringService dewateringService;
	@Autowired private InvertedwellService invertedwellService;
	@Autowired private ObservewellService observewellService;
	@Autowired private DeformmonitorService deformmonitorService;
	@Autowired private GroupService groupService;
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dataconf/list.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request);
		String csiteID = request.getParameter("cid");
		request.setAttribute("activeFun", "dataconf");  //指定当前操作功能
		if(!StringUtils.isBlank(csiteID)){
			try{
				@SuppressWarnings("unchecked")
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater csite=null;
				for(UnderWater c:cs){
					if(c.getId().equals(Long.parseLong(csiteID))){
						csite=c;
						break;
					}
				}
				if(csite==null){
					csite=pnodeUtils.getByCid(Long.parseLong(csiteID));
				}
				model.addAttribute("csite", csite);
				List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
				boolean hasRight=false;
				if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
		        	if(request.isUserInRole("EDIT-PROJECT")){
		        		hasRight=pnodeUtils.hasProjectRight(request, egroups, csite);
		        		model.addAttribute("hasRight", hasRight);
		        	}
				} else{
					hasRight=true;
				}
				
				//该项目下的网关集合
				Entitites<Gateway> gateways = gatewayService.getListByAttributes(new String[]{"cSite.id"}, new Long[]{Long.valueOf(csiteID)}, null);
				model.addAttribute("gateways", gateways.getEntites());
				model.addAttribute("hasRight", hasRight);
				String ctype = request.getParameter("ctype");
				//手机版
				System.out.println("**********"+csite.getSstatus());
				if(StringUtils.isNotBlank(ctype)){
					model.addAttribute("project", csite);
					return "phone-dataconf";
				}
			}catch (Exception e){
				LOGGER.debug(e.getMessage());
			}
		}else{
			return "csite-wlist";
		}
		
		return "water-dataconf";
	}
	
	/**
	 * @param type 1: 降水井，2：疏干井；3：回灌井；4：观测井；5：环境监测
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/dataconf/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getDataconfs(@RequestParam String aoData, HttpServletRequest request, HttpServletResponse response) {
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		String csiteID = request.getParameter("cid");
		String type = request.getParameter("type"); //测点的类型
		if(!StringUtils.isBlank(csiteID)){
			AjaxResponse resp = new AjaxResponse();
			DataTable dt = new DataTable();
			dt.setsEcho(dataTableParam.getsEcho()+1);
			try {
				//指定项目ID进行查询，获取项目下的所有测点
				List<String> attributes = new ArrayList<String>();
				attributes.add("cSite.id");
				List<String> fieldValues = new ArrayList<String>();
				fieldValues.add(String.valueOf(Long.parseLong(csiteID)));
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				
				PointEnumType pointType = PointEnumType.getType(Integer.valueOf(type));
				List<Basepoint> plist = pointService.getListByCid(Long.valueOf(csiteID), pointType, null);
				dt.setAaData(plist);
				ObjectMapper mapper = new ObjectMapper();
				dt.setiTotalDisplayRecords(dt.getAaData().size());
		        dt.setiTotalRecords(dt.getiTotalDisplayRecords());
		        mapper.getSerializationConfig().addMixInAnnotations(Basepoint.class, PointInfoFilter.class);
		        String json = mapper.writeValueAsString(dt);
		        return json;
			} catch (Exception e) {
				LOGGER.error("Error while paging dataconf", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param type 1: 降水井，2：疏干井；3：回灌井；4：观测井；5：环境监测
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/dataconf/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(
			Model model, HttpServletRequest request, HttpServletResponse response)  {
		
		AjaxResponse resp = new AjaxResponse();
		String cid = request.getParameter("cid");
		String type = request.getParameter("type"); //测点的类型
		String plid = request.getParameter("plid");
		String gateway = request.getParameter("gateway");
		String node1 = request.getParameter("node1"); 
		String node2 = request.getParameter("node2"); 
		String node3 = request.getParameter("node3"); 
		String node4 = request.getParameter("node4"); 
		
		String channel1 = request.getParameter("channel1"); 
		String channel2 = request.getParameter("channel2"); 
		String channel3 = request.getParameter("channel3"); 
		String channel4 = request.getParameter("channel4"); 
		BasepointLink pointlink = null;
		Basepoint point = null;
		switch (type) {
		case "1":
			//降水井
			pointlink = new PumpLink();
			point = pumpwellService.getById(Long.valueOf(cid));
			break;
		case "2":
			//疏干井
			pointlink = new DewateringLink();
			point = dewateringService.getById(Long.valueOf(cid));
			break;
		case "3":
			//回灌井
			pointlink = new InvertedLink();
			point = invertedwellService.getById(Long.valueOf(cid));
			break;
		case "4":
			//观测井
			pointlink = new ObserveLink();
			point = observewellService.getById(Long.valueOf(cid));
			break;
		case "5":
			//环境监测
			pointlink = new DeformLink();
			point = deformmonitorService.getById(Long.valueOf(cid));
			break;
		default:
			break;
		}
		point.setPointLink(pointlink);
		pointlink.setPoint(point);
		
		if (StringUtils.isNotBlank(gateway)) {
			Gateway gw = gatewayService.getById(Long.valueOf(gateway));
			pointlink.setGateway(gw);
		}
		if (StringUtils.isNotBlank(plid)) {
			pointlink.setId(Long.valueOf(plid));
		}
		wrapPointlink(node1, node2, node3, node4, channel1, channel2, channel3, channel4, pointlink);
		try {
//			pointLinkService.saveOrUpdate(pointlink);
			pointService.saveOrUpdate(point);
			pnodeUtils.reloadAllAuto();
			pnodeUtils.reloadAllAutoDeep();
			//重新设置自动选项
			resp.addEntry("plid", String.valueOf(pointlink.getId())); //将pointlink的id传回前台 
		} catch (ServiceException e) {
			LOGGER.error("Error while save dataconf ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(e.getMessage());
			String returnString = resp.toJSONString();
			return returnString;
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("Error while save dataconf ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage("保存失败，请保证【网关+节点+通道】唯一性");
			String returnString = resp.toJSONString();
			return returnString;
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	@SuppressWarnings("rawtypes")
	private void wrapPointlink(String node1, String node2, String node3, String node4, String channel1, String channel2,
			String channel3, String channel4, BasepointLink pointlink) {
		if (StringUtils.isNotBlank(node1)) {
			pointlink.setNode1(Integer.valueOf(node1));
		}
		if (StringUtils.isNotBlank(node2)) {
			pointlink.setNode2(Integer.valueOf(node2));
		}
		if (StringUtils.isNotBlank(node3)) {
			pointlink.setNode3(Integer.valueOf(node3));
		}
		if (StringUtils.isNotBlank(node4)) {
			pointlink.setNode4(Integer.valueOf(node4));
		}

		if (StringUtils.isNotBlank(channel1)) {
			pointlink.setChannel1(Integer.valueOf(channel1));
		}
		if (StringUtils.isNotBlank(channel2)) {
			pointlink.setChannel2(Integer.valueOf(channel2));
		}
		if (StringUtils.isNotBlank(channel3)) {
			pointlink.setChannel3(Integer.valueOf(channel3));
		}
		if (StringUtils.isNotBlank(channel4)) {
			pointlink.setChannel4(Integer.valueOf(channel4));
		}
	}

	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/dataconf/checkValidCode.shtml", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody String checkValidCode(@RequestParam String id, @RequestParam String serialno, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
		AjaxResponse resp = new AjaxResponse();
		resp.setErrorString("");
		if(StringUtils.isBlank(serialno)) {
			return resp.toJSONString();
		}
		try {
			Entitites<Gateway> result = gatewayService.getListByAttributes(new String[]{"serialno"}, new String[]{serialno}, null);
			if (result.getTotalCount()>0) {
				if (StringUtils.isNotBlank(id)) {
					if (result.getEntites().get(0).getSerialno().equalsIgnoreCase(serialno)&&!result.getEntites().get(0).getId().equals(Long.valueOf(id))) {
						resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					}
				} else if (result.getEntites().get(0).getSerialno().equalsIgnoreCase(serialno)) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while getting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
			
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");
		model.addAttribute("activeMenus",activeMenus);
	}
}
