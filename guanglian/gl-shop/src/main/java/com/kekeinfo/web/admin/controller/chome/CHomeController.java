package com.kekeinfo.web.admin.controller.chome;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.last.model.CsiteLast;
import com.kekeinfo.core.business.last.service.CsiteLastService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.DataUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;



@Controller
public class CHomeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CHomeController.class);
	@Autowired CsiteLastService csiteLastService;
	@Autowired DataUtils dataUtils;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired GroupService groupService;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/{mediafrom}/chome.html", method=RequestMethod.GET)
	public String pchome(@PathVariable String mediafrom,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		List<CsiteLast> clast = csiteLastService.getByUserID(user.getId());
		List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
		
		if(clast!=null){
			List<UnderWater> last = new ArrayList<>();
			for(CsiteLast c:clast){
				for(UnderWater ud:cs){
					if(ud.getId()==c.getCid()){
						last.add(ud);
						break;
					}
				}
			}
			model.addAttribute("last", last);
		}
		
		//加载项目集合
		HashMap<String, Set<UnderWater>> set  = pnodeUtils.getWaterCsitesZone(request);
		request.setAttribute("projects", set);
		
		setMenu(model,request);
		model.addAttribute("app", user.getuAgent());
		request.setAttribute(Constants.MEDIA_FROM, mediafrom);
		return mediafrom+"-water-chome";
		
	}
	
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/{mediafrom}/ctype.html", method=RequestMethod.GET)
	public String ctype(@PathVariable String mediafrom, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String zone = request.getParameter("zone");
		model.addAttribute("zone",zone);
		setMenu(model,request);
		request.setAttribute(Constants.MEDIA_FROM, mediafrom);
		return mediafrom+"-water-ctype";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/{mediafrom}/actype.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String actype(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String zone = request.getParameter("zone");
		if(StringUtils.isNotBlank(zone)){
			@SuppressWarnings("unchecked")
			List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_PROJECT_TYPE);
			//List<Map<String, Object>> relist = new ArrayList<>();
			//加载项目集合
			HashMap<String, Set<UnderWater>> set  = pnodeUtils.getWaterCsitesZone(request);
			Set<UnderWater> uw = set.get(zone);
			
			
			if(uw!=null){
				Map<String, List<UnderWater>> uws = new TreeMap<String, List<UnderWater>>();
				
				List<UnderWater> cs = new ArrayList<>();
				List<Long> ids =new ArrayList<>();
				for(UnderWater u:uw){
					List<UnderWater> ulist = uws.get(u.getCtype());
					if(ulist==null){
						ulist = new ArrayList<>();
						uws.put(u.getCtype(), ulist);
					}
					ulist.add(u);
					
					ids.add(u.getId());
					cs.add(u);
				}
				//Map<String, Object> dataResult = null;
				//dataResult =dataUtils.getWarningData(dataResult,ids,cs);
				
				//计算第一级对象
				//List<Map<String, Object>> templist =this.getRemap(upws, null, dataResult, blist);
				LOGGER.debug("Logger :计算第二级的对象");
				//计算第二级的对象
				List<Map<String, Object>> relist =this.getRemap(uws, blist);
				 
				ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(relist);
					return json;
				
			}
		}
		
		return "";
	}
	
	
	@SuppressWarnings("unchecked")
	private  List<Map<String, Object>> getRemap(Map<String, List<UnderWater>> uws,List<BasedataType> blist){
		Iterator<String> iter = uws.keySet().iterator();
		List<Map<String, Object>> relist = new ArrayList<>();
		List<Map<String, Object>> templist = new ArrayList<>();
		while (iter.hasNext()) {
			Map<String, Object> entry_uws = new HashMap<String, Object>();
			Object key = iter.next();
			List<UnderWater> val = uws.get(key);
			int down =0;
			int run =0;
			int srun=0;
			int over =0;
			int erro=0;
			for(UnderWater un:val){
				switch (un.getStatus()) {
				case -1:
					over++;
					break;
				case 0:
					down++;
					break;
				case 1:
					run++;
					break;
				case 2:
					srun++;
					break;
				}
				//告警信息
				if(un.getRunstatus()>0){
					erro++;
				}
			}
			
			entry_uws.put("val", key.toString());
			entry_uws.put("down", down);
			entry_uws.put("run", run);
			entry_uws.put("srun", srun);
			entry_uws.put("erro", erro);
			entry_uws.put("over", over);
			entry_uws.put("total", down+run+srun-erro);
			String ctype = key.toString();
			int index = ctype.indexOf("_");
			for(BasedataType bt:blist){
				if(bt.getValue().equalsIgnoreCase(key.toString().trim())){
					entry_uws.put("name", bt.getName());
					break;
				}
			}
			
			if(index==-1){
				relist.add(entry_uws);
			}else{
				//将二级合入到一级
				String suName = ctype.substring(0,index);
				Map<String, Object> entry_ups=null;
				List<Map<String, Object>> submap =null;
				for(Map<String, Object> map:templist){
					if(map.get("name").toString().equalsIgnoreCase(suName)){
						entry_ups =map;
						submap = (List<Map<String, Object>>) entry_ups.get("sublist");
						break;
					}
				}
				if(entry_ups==null){
					entry_ups = new HashMap<String, Object>();
					submap =new ArrayList<>();
					entry_ups.put("down", down);
					entry_ups.put("run", run);
					entry_ups.put("srun", srun);
					entry_ups.put("erro", erro);
					entry_ups.put("over", over);
					entry_ups.put("total", down+run+srun-erro);
					templist.add(entry_ups);
				}else{
					entry_ups.put("down", Integer.parseInt(entry_ups.get("down").toString())+down);
					entry_ups.put("run", Integer.parseInt(entry_ups.get("run").toString())+run);
					entry_ups.put("srun", Integer.parseInt(entry_ups.get("srun").toString())+srun);
					entry_ups.put("erro", Integer.parseInt(entry_ups.get("erro").toString())+erro);
					entry_ups.put("over", Integer.parseInt(entry_ups.get("over").toString())+over);
					entry_ups.put("total", Integer.parseInt(entry_ups.get("total").toString())+down+srun+run-erro);
				}
				entry_ups.put("name", suName);
				submap.add(entry_uws);
				entry_ups.put("sublist",submap);
				
			}
			
		}
		//将两者合并
		if(templist!=null){
			for(Map<String, Object> map:templist){
				relist.add(map);
			}
		}
		return relist;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/{mediafrom}/cprojects.html", method=RequestMethod.GET)
	public String cprojrcts(@PathVariable String mediafrom, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ctype =request.getParameter("ctype");
		String zone =request.getParameter("zone");
		if(StringUtils.isNotBlank(ctype) && StringUtils.isNotBlank(zone)){
			//加载项目集合
			HashMap<String, Set<UnderWater>> set  = pnodeUtils.getWaterCsitesZone(request);
			Set<UnderWater> uw = set.get(zone);
			if(uw!=null && uw.size()>0){
				List<UnderWater> jylist = new ArrayList<>();
				List<UnderWater> sglist = new ArrayList<>();
				List<UnderWater> cjlist = new ArrayList<>();
				List<UnderWater> wclist = new ArrayList<>();
				for(UnderWater u:uw){
					if(u.getCtype().equalsIgnoreCase(ctype.trim())){
						switch (u.getStatus()) {
						case 1:
							jylist.add(u);
							break;
						case 2:
							sglist.add(u);
							break;
						case 0:
							cjlist.add(u);
							break;
						case -1:
							wclist.add(u);
						}
					}
				}
				model.addAttribute("jylist", jylist);
				model.addAttribute("sglist", sglist);
				model.addAttribute("cjlist", cjlist);
				model.addAttribute("wclist", wclist);
			}
			@SuppressWarnings("unchecked")
			List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_PROJECT_TYPE);
			for(BasedataType bt:blist){
				if(bt.getValue().equalsIgnoreCase(ctype)){
					model.addAttribute("ctype",  bt.getName());
					break;
				}
			}
		}
		//判断是否手机用户
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		model.addAttribute("app", user.getuAgent());
		
		request.setAttribute(Constants.MEDIA_FROM, mediafrom);
		return mediafrom+"-water-cprojects";
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/cproject.html", method=RequestMethod.GET)
	public String cproject(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cid =request.getParameter("cid");
		model.addAttribute("cid", cid);
		//加载刷新时间
		List<BasedataType> rlist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.FRESHTIME);
		if(rlist.isEmpty()){
			model.addAttribute("rfreshtime", "60");
		}else {
			model.addAttribute("rfreshtime", rlist.get(0).getValue());
		}
		
		if(StringUtils.isNotBlank(cid) ){
			UnderWater un = pnodeUtils.getByCid(Long.parseLong(cid),request);
			model.addAttribute("project", un);
			model.addAttribute("activeFun", "monitor");  //指定当前操作功能
			//判断用户是否有该项目的编辑权限
	        boolean hasRight = false;
	        try {
	        	if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
	        		hasRight = true;
	        	} else {
	        		if (request.isUserInRole("EDIT-PROJECT")) {
	        			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
	        			hasRight = pnodeUtils.hasProjectRight(request, egroups, un);
	        		}
	        	}
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("hasRight", hasRight);
		}
		
		return "water-cproject";
	}

	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/csite/cpdetail.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String cpdetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cid = request.getParameter("cid");
		if(StringUtils.isNotBlank(cid) ){
			//记录打开项目
			UnderWater un = pnodeUtils.getByCid(Long.parseLong(cid),request);
			CsiteLast clast = new CsiteLast();
			clast.setCid(un.getId());
			clast.setDateModified(new Date());
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			clast.setUid(user.getId());
			csiteLastService.saveNew(clast);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(un);
			return json;
		}
		return "";	
	}
	
	/**
	@SuppressWarnings("unchecked")
	private boolean erro(Map<String, Object> dataResult, UnderWater un){
		List<Map<String, Object>> wpwell =(List<Map<String, Object>>) dataResult.get("wpwell");
		if(wpwell!=null){
			for(Map<String, Object> map:wpwell){
				Long cid = (Long)map.get("cid");
				if(cid.equals(un.getId())){
					return true;
				}
			}
		}
		List<Map<String, Object>> wdewell =(List<Map<String, Object>>) dataResult.get("wdewell");
		if(wdewell!=null){
			for(Map<String, Object> map:wdewell){
				Long cid = (Long)map.get("cid");
				if(cid.equals(un.getId())){
					return true;
				}
			}
		}
		List<Map<String, Object>> wowell =(List<Map<String, Object>>) dataResult.get("wowell");
		if(wowell!=null){
			for(Map<String, Object> map:wowell){
				Long cid = (Long)map.get("cid");
				if(cid.equals(un.getId())){
					return true;
				}
			}
		}
		List<Map<String, Object>> wiwell =(List<Map<String, Object>>) dataResult.get("wiwell");
		if(wiwell!=null){
			for(Map<String, Object> map:wiwell){
				Long cid = (Long)map.get("cid");
				if(cid.equals(un.getId())){
					return true;
				}
			}
		}
		List<Map<String, Object>> wewell =(List<Map<String, Object>>) dataResult.get("wewell");
		if(wewell!=null){
			for(Map<String, Object> map:wewell){
				Long cid = (Long)map.get("cid");
				if(cid.equals(un.getId())){
					return true;
				}
			}
		}
		
		return false;
	}
	*/
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");
		model.addAttribute("activeMenus",activeMenus);
		
	}
	


}
