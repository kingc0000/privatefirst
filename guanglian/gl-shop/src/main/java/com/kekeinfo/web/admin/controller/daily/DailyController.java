package com.kekeinfo.web.admin.controller.daily;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.daily.model.Daily;
import com.kekeinfo.core.business.daily.model.DailyImage;
import com.kekeinfo.core.business.daily.model.WellCondition;
import com.kekeinfo.core.business.daily.service.DailyService;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.deformmonitor.service.DeformmonitorService;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.dewatering.service.DewateringService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DateConverter;
import com.kekeinfo.web.utils.ImageFilePathUtils;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class DailyController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DailyController.class);
	
	@Autowired
	private DailyService dailyService;
	
	@Autowired private CSiteService cSiteService;
	
	@Autowired private GroupService groupService;
	
	@Autowired private DewateringService dewellService;
	
	@Autowired private DeformmonitorService ewellService;
	
	@Autowired private InvertedwellService iwellService;
	
	@Autowired private ObservewellService owellService;
	
	@Autowired private PumpwellService pwellService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/daily/list.html", method=RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		model.addAttribute("daily",new Daily());
		setMenu(model,request);
		
		String cid = request.getParameter("cid");
		
		List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
		UnderWater csite=null;
		for(UnderWater c:cs){
			if(c.getId().equals(Long.parseLong(cid))){
				csite=c;
				break;
			}
		}
		if(csite==null){
			csite = pnodeUtils.getByCid(Long.parseLong(cid));
		}
		//判断访问来源，来自地图，则默认打开新增
		String from = request.getParameter("from");
		request.setAttribute("from", from);
		
		//权限判断
        boolean hasRight = false;
        try {
        	if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
        		if (request.isUserInRole("EDIT-PROJECT")) {
        			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
        			hasRight = pnodeUtils.hasProjectRight(request, egroups, csite);
        		}
        	} else {
        		hasRight = true;
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("hasRight", hasRight);
		request.setAttribute("activeFun", "daily");  //指定当前操作功能
		request.setAttribute("activePid", cid); //指定项目
		
		
		model.addAttribute("nowdate", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		String ctype = request.getParameter("ctype");
		//手机版
		if(StringUtils.isNotBlank(ctype)){
			model.addAttribute("project", csite);
			return "phone-daily";
		}
		model.addAttribute("csite", csite);
		return "csite-daily";
	}
	
	@RequestMapping(value="/water/daily/preview.html", method=RequestMethod.GET)
	public String pre(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model,request);
		
		String cid = request.getParameter("cid");
		ConstructionSite csite = cSiteService.getByCidWithALLWell(Long.parseLong(cid));
		model.addAttribute("projectname", csite.getProject().getName());
		String did = request.getParameter("did");
		Daily daily = dailyService.getById(Long.parseLong(did));
		Set<WellCondition> wcs =this.setWellName(daily.getWellCon(), csite);
		daily.setWellCon(wcs);
		Set<DailyImage> imgs =daily.getDailyImages();
		if(imgs!=null){
			for(DailyImage img:imgs){
				img.setDailyImage(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getDailyImage(), FileContentType.DAILY));
				if(StringUtils.isNotBlank(img.getJpeg())){
					img.setJpeg(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getJpeg(), FileContentType.DAILY));
				}
			}
		}
		model.addAttribute("daily", daily);
		return "csite-dailypre";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/daily/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getDailys(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response,Locale locale) {
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		String cid = request.getParameter("cid");
		
		//指定根据什么条件进行模糊查询
		List<String> attributes = new ArrayList<String>();
		attributes.add("title"); //名称
		attributes.add("note");
		HashMap<String, String> orderby = new HashMap<String, String>();
		orderby.put("auditSection.dateModified", "desc");
		Map<String, String> fetches = new HashMap<String, String>();
		fetches.put("cSite", "left");
		List<Object[]> where = new ArrayList<Object[]>();
		where.add(new String[]{"cSite.id", cid, "0"});
		
		Entitites<Daily> list = dailyService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
				Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, fetches, true);
		
		DataTable<Map<String, Object>> dt = new DataTable<Map<String, Object>>();
		dt.setsEcho(dataTableParam.getsEcho()+1);
		dt.setiTotalDisplayRecords(list.getTotalCount());
        dt.setiTotalRecords(list.getTotalCount());
        if(list.getEntites()!=null && list.getEntites().size()>0) {
			for(Daily daily : list.getEntites()) {
				Map<String, Object> entry = new HashMap<String, Object>();
				entry.put("id", daily.getId());
				entry.put("datec", DateFormatUtils.format(daily.getDatec(), "yyyy-MM-dd"));
				entry.put("modifiedBy", daily.getAuditSection().getModifiedBy());
				entry.put("dateModified", DateFormatUtils.format(daily.getAuditSection().getDateModified(), "yyyy-MM-dd HH:mm:ss"));
				/**
				JSONArray array = new JSONArray();
				if (!daily.getDailyImages().isEmpty()) {
					for(DailyImage image : daily.getDailyImages()) {
						JSONObject data = new JSONObject();
						data.put("id", image.getId().toString());
						StringBuffer imagePath = new StringBuffer("");
						imagePath.append(request.getContextPath()).append(ImageFilePathUtils.buildDailyImageFilePath(image.getDailyImage()));
						data.put("fullImageUrl", imagePath.toString());
						array.add(data);
					}
				}
				entry.put("dailyImages", array.toJSONString()); //商品图片集合*/
				dt.getAaData().add(entry);
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
	@RequestMapping(value = "/water/daily/today.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody String getTodaydatas(HttpServletRequest request,HttpServletResponse response) {
		//查找今天的日志，没有就返回昨天的
		try {
			String pid=request.getParameter("pid");
			ConstructionSite csite= cSiteService.getByCidWithALLWell(Long.parseLong(pid));
			
			List<String> attributes = new ArrayList<String>();
			attributes.add("cSite.id");
			List<String> fieldValues = new ArrayList<String>();
			fieldValues.add(String.valueOf(Long.parseLong(pid)));
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("datec", "desc");
			Entitites<Daily> list  = dailyService.getPageListByAttributes(attributes, fieldValues, 1, null, orderby);
			Daily daily=null;
			//第一次创建的情况
			if(list ==null || list.getEntites()==null || list.getEntites().size()==0){
				daily = new Daily();
				Set<WellCondition> wcs = new HashSet<>();
				daily.setWellCon(wcs);
			}else{
				daily = list.getEntites().get(0);
			}
			Set<WellCondition> wcs  = this.setWellSize(daily.getWellCon(), csite);
			wcs =this.setWellName(daily.getWellCon(), csite);
		
			Date now = new Date();
			DateConverter datec = new DateConverter();
			if(daily.getDatec()!=null && !datec.DateEq(now, daily.getDatec())){
				daily.setId(null);
				daily.setDailyImages(null);
				//清空当日井号ids字符串
				for (WellCondition wellCondition : wcs) {
					wellCondition.setWellIds("");
				}
			}
			daily.setWellCon(wcs);
			//if(daily!=null){
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(daily);
			return json;
		//	}
			
		} catch (ServiceException | IOException | ParseException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage());
		}
		
		return null;
	}
	
	private WellCondition getWellCon(Set<WellCondition> wcs,int type){
		WellCondition well =null;
		for(WellCondition wc:wcs){
			if(wc.getwType()==type){
				well=wc;
				break;
			}
		}
		if(well==null){
			well =new WellCondition();
			well.setwType(type);
		}
		return well;
	}
	private Set<WellCondition> setWellSize(Set<WellCondition> wcs,ConstructionSite csite){
		//疏干井,降水井,回灌井,观测井,监测点
		Set<Pumpwell> pwells = csite.getPwells();
		if(pwells!=null && pwells.size()>0){
			WellCondition well = this.getWellCon(wcs,1);
			well.setDesignQua(pwells.size());
			wcs.add(well);
		}
		Set<Dewatering> dewells = csite.getDewells();
		if(dewells!=null && dewells.size()>0){
			WellCondition well = this.getWellCon(wcs,0);
			well.setDesignQua(dewells.size());
			wcs.add(well);
		}
		Set<Invertedwell> iwells = csite.getIwells();
		if(iwells!=null && iwells.size()>0){
			WellCondition well = this.getWellCon(wcs,2);
			well.setDesignQua(iwells.size());
			wcs.add(well);
		}
		Set<Observewell> owells = csite.getOwells();
		if(owells!=null && owells.size()>0){
			WellCondition well = this.getWellCon(wcs,3);
			well.setDesignQua(owells.size());
			wcs.add(well);
		}
		Set<Deformmonitor> ewells = csite.getEwells();
		if(ewells!=null && ewells.size()>0){
			WellCondition well = this.getWellCon(wcs,4);
			well.setDesignQua(ewells.size());
			wcs.add(well);
		}
		return wcs;
	}
	
	private Set<WellCondition> setWellName(Set<WellCondition> wcs,ConstructionSite csite){
		for(WellCondition wc:wcs){
			if(StringUtils.isNotBlank(wc.getWellIds())){
				String[] ids = wc.getWellIds().split(",");
				StringBuffer names = new StringBuffer();
				//疏干井,降水井,回灌井,观测井,监测点
				switch (wc.getwType()) {
				case 0:
					for(String s:ids){
						for(Dewatering de:csite.getDewells()){
							if(de.getId().equals(Long.parseLong(s))){
								names.append(de.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;
				case 1:
					for(String s:ids){
						for(Pumpwell p:csite.getPwells()){
							if(p.getId().equals(Long.parseLong(s))){
								names.append(p.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;
				case 2:
					for(String s:ids){
						for(Invertedwell i:csite.getIwells()){
							if(i.getId().equals(Long.parseLong(s))){
								names.append(i.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;
				case 3:
					for(String s:ids){
						for(Observewell o:csite.getOwells()){
							if(o.getId().equals(Long.parseLong(s))){
								names.append(o.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;
				case 4:
					for(String s:ids){
						for(Deformmonitor e:csite.getEwells()){
							if(e.getId().equals(Long.parseLong(s))){
								names.append(e.getName()).append(",");
							}
						}
					}
					if(names.length()>0){
						names=names.deleteCharAt(names.lastIndexOf(","));
					}
					wc.setWellnames(names.toString());
					break;	
				}
				
			}
		}
		return wcs;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/daily/wells.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody String getWellsdatas(HttpServletRequest request,HttpServletResponse response) {
		String index =request.getParameter("wtype");
		String pid = request.getParameter("pid");
		String chkd = request.getParameter("chkd");
		String[] ckdid=null;
		if(StringUtils.isNotBlank(chkd)){
			ckdid =chkd.split(",");
		}
		try {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			/**
			 * 疏干井,降水井,回灌井,观测井,监测点
			 */
			List<String> attributes = new ArrayList<String>();
			attributes.add("cSite.id");
			List<String> fieldValues = new ArrayList<String>();
			fieldValues.add(pid);
			switch (index) {
			case "0":
				Entitites<Dewatering> delist  = dewellService.getPageListByAttributes(attributes, fieldValues,null, null,null);
				for(Dewatering p:delist.getEntites()){
					if(p.isDone()==false){
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", p.getId());
						entry.put("name", p.getName());
						resultList.add(entry);
					}else if(ckdid!=null && ckdid.length>0 ){
						for(String s:ckdid){
							if(p.getId().equals(Long.parseLong(s))){
								Map<String, Object> entry = new HashMap<String, Object>();
								entry.put("id", p.getId());
								entry.put("name", p.getName());
								entry.put("checked", "checked");
								resultList.add(entry);
								break;
							}
						}
					}
					
				}
				break;
			case "1":
				Entitites<Pumpwell> plist  = pwellService.getPageListByAttributes(attributes, fieldValues,null, null,null);
				for(Pumpwell p:plist.getEntites()){
					if(p.isDone()==false){
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", p.getId());
						entry.put("name", p.getName());
						resultList.add(entry);
					}else if(ckdid!=null && ckdid.length>0 ){
						for(String s:ckdid){
							if(p.getId().equals(Long.parseLong(s))){
								Map<String, Object> entry = new HashMap<String, Object>();
								entry.put("id", p.getId());
								entry.put("name", p.getName());
								entry.put("checked", "checked");
								resultList.add(entry);
								break;
							}
						}
					}
					
				}
				break;
			case "2":
				Entitites<Invertedwell> ilist  = iwellService.getPageListByAttributes(attributes, fieldValues,null, null,null);
				for(Invertedwell p:ilist.getEntites()){
					if(p.isDone()==false){
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", p.getId());
						entry.put("name", p.getName());
						resultList.add(entry);
					}else if(ckdid!=null && ckdid.length>0 ){
						for(String s:ckdid){
							if(p.getId().equals(Long.parseLong(s))){
								Map<String, Object> entry = new HashMap<String, Object>();
								entry.put("id", p.getId());
								entry.put("name", p.getName());
								entry.put("checked", "checked");
								resultList.add(entry);
								break;
							}
						}
					}
				}
				break;
			case "3":
				Entitites<Observewell> olist  = owellService.getPageListByAttributes(attributes, fieldValues,null, null,null);
				for(Observewell p:olist.getEntites()){
					if(p.isDone()==false){
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", p.getId());
						entry.put("name", p.getName());
						resultList.add(entry);
					}else if(ckdid!=null && ckdid.length>0 ){
						for(String s:ckdid){
							if(p.getId().equals(Long.parseLong(s))){
								Map<String, Object> entry = new HashMap<String, Object>();
								entry.put("id", p.getId());
								entry.put("name", p.getName());
								entry.put("checked", "checked");
								resultList.add(entry);
								break;
							}
						}
					}
					
				}
				break;
			case "4":
				Entitites<Deformmonitor> elist  = ewellService.getPageListByAttributes(attributes, fieldValues,null, null,null);
				for(Deformmonitor p:elist.getEntites()){
					if(p.isDone()==false){
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", p.getId());
						entry.put("name", p.getName());
						resultList.add(entry);
					}else if(ckdid!=null && ckdid.length>0 ){
						for(String s:ckdid){
							if(p.getId().equals(Long.parseLong(s))){
								Map<String, Object> entry = new HashMap<String, Object>();
								entry.put("id", p.getId());
								entry.put("name", p.getName());
								entry.put("checked", "checked");
								resultList.add(entry);
								break;
							}
						}
					}
				}
				break;
			
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(resultList);
			return json;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage());
		}
		
		return null;
	}
	private Daily setConditon(Daily daily) {
		Set<WellCondition> wcs = new HashSet<>();
		for(int i=0;i<daily.getwType().length;i++){
			if(StringUtils.isNotBlank(daily.getDesignQua()[i])){
				try{
					WellCondition wc = new WellCondition();
					//去掉不用的空格
					String wellids=daily.getWellIds()[i];
					if(StringUtils.isNotBlank(wellids)){
						String[] wids =wellids.split(",");
						StringBuffer ids = new StringBuffer();
						for(String s:wids){
							ids.append(s).append(",");
						}
						ids.deleteCharAt(ids.lastIndexOf(","));
						wc.setWellIds(ids.toString());
					}else{
						wc.setWellIds("");
					}
					
					wc.setPlanCmp(Integer.parseInt(daily.getPlanCmp()[i]));
					wc.setCumCmp(Integer.parseInt(daily.getCumCmp()[i]));
					wc.setDesignQua(Integer.parseInt(daily.getDesignQua()[i]));
					wc.setDest(daily.getDest()[i]);
					wc.setMemo(daily.getMemo()[i]);
					wcs.add(wc);
				}catch (Exception e){
					LOGGER.error(e.getMessage());
				}
				
			}
		}
		daily.setWellCon(wcs);
		return daily;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/daily/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String save(@ModelAttribute("daily") Daily daily, @RequestParam(required=false, value="fileupload") List<CommonsMultipartFile> uploadfiles, Model model, HttpServletRequest request, HttpServletResponse response)  {
		//删除的日志图片ID集合，采用#作为分隔符，例如"imgId1#imgId2#"
		String delImageIds = request.getParameter("delids");
		LOGGER.info("daily image will delete, id=" + delImageIds);
		AjaxResponse resp = new AjaxResponse();
		try {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			daily.getAuditSection().setModifiedBy(user.getFirstName());
			daily.setUserId(user.getId());
			daily =  this.setConditon(daily);
			//设置日期
			//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			//String D1 = dateFormat.format(new Date());
			//daily.setDatec(dateFormat.parse(D1));
			//处理新增加的日志图片
			Set<DailyImage> newImages = new HashSet<DailyImage>();
			if(uploadfiles!=null && !uploadfiles.isEmpty()) {
				try {
					for (int i = 0; i < uploadfiles.size(); i++) {
						MultipartFile img = uploadfiles.get(i);
						if(img.getSize()<=0) continue; //如果图片大小<=0，则图片不作处理
						String imageName = img.getOriginalFilename();
						String suffix = RadomSixNumber.getImageSuffix(img.getContentType());
						if (suffix.equals("")) {
							suffix = Constants.IMAGE_JPG;
						}
						String in = RadomSixNumber.getImageName(imageName, suffix);
						DailyImage dailyImage = new DailyImage();
						dailyImage.setImage(img.getInputStream());
						dailyImage.setDailyImage(in);
						
						dailyImage.setImageType(0);
						dailyImage.setDaily(daily);
						//压缩
						dailyImage.setJpeg(RadomSixNumber.getImageName(dailyImage.getDailyImage()));
						InputStream inputStream = img.getInputStream();
						dailyImage.setJdigital(ImageUtils.ByteArrayOutputStream(inputStream));
						newImages.add(dailyImage);
					}
					daily.setDailyImages(newImages);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//处理数据
			//ConstructionSite csite = cSiteService.getByCidWithALLWell(daily.getcSite().getId());
			
			dailyService.save(daily, delImageIds);
		} catch (ServiceException e ) {
			LOGGER.error("Error while save daily ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/daily/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.valueOf(listId[i]);
				try {
					dailyService.deleteById(id);
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
		activeMenus.put("csite", "csite");
		model.addAttribute("activeMenus",activeMenus);
	}
	
}
