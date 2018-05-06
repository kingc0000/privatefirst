package com.kekeinfo.web.jiangong.controller;

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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
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

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.generic.model.PointEnumType;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.service.PointService;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.entity.PinYinName;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.services.controller.system.ModbusListener;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.QCodeUtils;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;


@Controller
public class JGPWellController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JGPWellController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired CSiteService cSiteService;
	@Autowired PumpwellService pumpwellService;
	@Autowired GroupService groupService;
	@Autowired  TransExcelView transExcelView;
	@Autowired ModbusListener modbusListener;
	@Autowired QCodeUtils qCodeUtils;
	@Autowired PointService pointService;
	@SuppressWarnings("unchecked")
	
	@RequestMapping(value="/jiangong/pwell/list.html", method=RequestMethod.GET)
	public String displayStores(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model,request);
		Pumpwell pwell = new Pumpwell();
		model.addAttribute("oWell", pwell);
		String csiteID = request.getParameter("cid");
		request.setAttribute("activeFun", "pwell");  //指定当前操作功能
		if(!StringUtils.isBlank(csiteID)){
			try{
				//
				List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
				UnderWater csite=null;
				for(UnderWater c:cs){
					if(c.getId().equals(Long.parseLong(csiteID))){
						csite=c;
						break;
					}
				} 
				if(csite==null)csite=pnodeUtils.getByCid(Long.parseLong(csiteID));
				if(!csite.isJiangong()){
					return "water/404";
				}
				model.addAttribute("csite", csite);
				
			}catch (Exception e){
				LOGGER.debug(e.getMessage());
			}
			
		}else{
			return "water/404";
		}
		
		return "jg-pwells";
	}
	
	
	
	
	@RequestMapping(value = "/jiangong/pwell/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getBanner(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		String csiteID = request.getParameter("cid");
		if(!StringUtils.isBlank(csiteID)){
			AjaxResponse resp = new AjaxResponse();
			DataTableParameter dataTableParam = new DataTableParameter(aoData);
			
			DataTable<Pumpwell> dt = new DataTable<Pumpwell>();
			try {
				
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<String> where = new ArrayList<>();
				where.add("cSite.id");
				where.add(csiteID);
				List<String> join =new ArrayList<String>();
				join.add("pointInfo");
				join.add("pointLink");
				Entitites<Pumpwell> list  = pumpwellService.getPageListByAttributesLike(null, null,Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby,where,join);
				dt.setsEcho(dataTableParam.getsEcho()+1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
		        dt.setiTotalRecords(list.getTotalCount());
		        dt.setAaData(list.getEntites());
		        ObjectMapper mapper = new ObjectMapper();
		       // mapper.getSerializationConfig().addMixInAnnotations(Pumpwell.class);
		        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
				String json = mapper.writeValueAsString(dt);
				return json;
			} catch (Exception e) {
				LOGGER.error("Error while paging csite", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		return null;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	
	@RequestMapping(value = "/jiangong/pwell/autoWell.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " text/html; charset=utf-8")
	public @ResponseBody String getAutoWell(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		String cid =request.getParameter("wid");
		String ctype=request.getParameter("wtype");
		ObjectMapper mapper = new ObjectMapper();
		if(StringUtils.isNotBlank(cid) && StringUtils.isNotBlank(ctype)){
			PointEnumType type = PointEnumType.valueOf(ctype);
			Basepoint dbPoint = pointService.getById(Long.parseLong(cid), type);
			if(dbPoint!=null){
				String json = mapper.writeValueAsString(dbPoint.getName());
				return json;
			}
		}else{
			return "-1";
		}
		return "";
	}
	
	@SuppressWarnings("rawtypes")
	
	@RequestMapping(value="/jiangong/pwell/allwells.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin>  getAllWell(HttpServletRequest request, HttpServletResponse response) {
		String cid =request.getParameter("cid");
		if(StringUtils.isNotBlank(cid)){
			try{
				List<PinYin> pinyins =new ArrayList<PinYin>();
				//降水井
				PointEnumType pointType = PointEnumType.getType(Integer.valueOf(1));
				List<Basepoint> plist = pointService.getListNameByCid(Long.parseLong(cid), pointType);
				if(plist!=null && plist.size()>0){
					PinYin pinYin = new PinYin();
					List<PinYinName> lists =new ArrayList<>();
					for(Basepoint p:plist){
						PinYinName pinYinName = new PinYinName();
						Pumpwell pwell =(Pumpwell)p;
						pinYinName.setId(pwell.getId().toString());
						pinYinName.setName(pwell.getName());
						lists.add(pinYinName);
					}
					pinYin.setLists(lists);
					pinYin.setCode("抽水井");
					pinYin.setType(pointType.toString());
					pinyins.add(pinYin);
				}
				//疏干井
				PointEnumType pointType2 = PointEnumType.getType(Integer.valueOf(2));
				List<Basepoint> plist2 = pointService.getListNameByCid(Long.parseLong(cid), pointType2);
				if(plist2!=null && plist2.size()>0){
					PinYin pinYin = new PinYin();
					List<PinYinName> lists =new ArrayList<>();
					for(Basepoint p:plist2){
						PinYinName pinYinName = new PinYinName();
						Dewatering pwell =(Dewatering)p;
						pinYinName.setId(pwell.getId().toString());
						pinYinName.setName(pwell.getName());
						lists.add(pinYinName);
					}
					pinYin.setLists(lists);
					pinYin.setCode("疏干井");
					pinYin.setType(pointType2.toString());
					pinyins.add(pinYin);
				}
				//观测井
				PointEnumType pointType4 = PointEnumType.getType(Integer.valueOf(4));
				List<Basepoint> plist4 = pointService.getListNameByCid(Long.parseLong(cid), pointType4);
				if(plist4!=null && plist4.size()>0){
					PinYin pinYin = new PinYin();
					List<PinYinName> lists =new ArrayList<>();
					for(Basepoint p:plist4){
						PinYinName pinYinName = new PinYinName();
						Observewell pwell =(Observewell)p;
						pinYinName.setId(pwell.getId().toString());
						pinYinName.setName(pwell.getName());
						lists.add(pinYinName);
					}
					pinYin.setLists(lists);
					pinYin.setCode("观测井");
					pinYin.setType(pointType4.toString());
					pinyins.add(pinYin);
				}
				//回灌井
				PointEnumType pointType3 = PointEnumType.getType(Integer.valueOf(3));
				List<Basepoint> plist3 = pointService.getListNameByCid(Long.parseLong(cid), pointType3);
				if(plist3!=null && plist3.size()>0){
					PinYin pinYin = new PinYin();
					List<PinYinName> lists =new ArrayList<>();
					for(Basepoint p:plist3){
						PinYinName pinYinName = new PinYinName();
						Invertedwell pwell =(Invertedwell)p;
						pinYinName.setId(pwell.getId().toString());
						pinYinName.setName(pwell.getName());
						lists.add(pinYinName);
					}
					pinYin.setLists(lists);
					pinYin.setCode("回灌井");
					pinYin.setType(pointType3.toString());
					pinyins.add(pinYin);
				}
				//环境监控
				/**
				PointEnumType pointType5 = PointEnumType.getType(Integer.valueOf(5));
				List<Basepoint> plist5 = pointService.getListNameByCid(Long.parseLong(cid), pointType5);
				if(plist5!=null && plist5.size()>0){
					PinYin pinYin = new PinYin();
					List<PinYinName> lists =new ArrayList<>();
					for(Basepoint p:plist){
						PinYinName pinYinName = new PinYinName();
						Deformmonitor pwell =(Deformmonitor)p;
						pinYinName.setId(pwell.getId().toString());
						pinYinName.setName(pwell.getName());
						lists.add(pinYinName);
					}
					pinYin.setLists(lists);
					pinYin.setCode("环境监测井");
					pinyins.add(pinYin);
				}*/
				return  pinyins;
			}catch (Exception e){
				LOGGER.debug(e.getMessage());
			}
		}
		
		return null;
	}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");


		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	

}
