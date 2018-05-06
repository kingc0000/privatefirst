package com.kekeinfo.web.admin.controller.user;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.gson.Gson;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.model.FeaturesType;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.system.service.EmailService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.controller.ControllerConstants;
import com.kekeinfo.web.admin.entity.secutity.Password;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.admin.security.WebUserServices;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DateUtil;
import com.kekeinfo.web.utils.EmailTemplatesUtils;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PasswordReset;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.UserUtils;

@SuppressWarnings("deprecation")
@Controller
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	UserService userService;

	@Autowired
	GroupService groupService;
	
	@Autowired
	EmailService emailService;
	
//	@Autowired
//	LabelUtils messages;
	
	@Autowired ContentService contentService;
	
	@Autowired DepartmentService departmentService;
	
	@Autowired DepartmentNodeService departmentNodeService;
	
	
	@Autowired
	private EmailTemplatesUtils emailTemplatesUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired private WebUserServices userDetailsService;
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/water/users/list.html", method=RequestMethod.GET)
	public String displayUsers(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		//The users are retrieved from the paging method
		model.addAttribute("user",new User());
		//this.populateUserObjects(model);
		List<Group> userGroups = groupService.list();
		model.addAttribute("groups", userGroups);
		setMenu(model,request);
		return ControllerConstants.Tiles.User.users;
	}
	
	/**
	 * Displays a list of users that can be managed by admins
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/water/users/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getContacts(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd H:m:s");
		
		try {

			//指定项目ID进行查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("adminName");
			attributes.add("adminEmail");
			attributes.add("firstName");
			
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateCreated", "desc");
			Entitites<User> list  = userService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby,null,null);
			DataTable<User> dt = new DataTable<User>();
			//dt.setAaData(stores.getEntites());
			long sEcho = dataTableParam.getsEcho()+1;
			dt.setsEcho(sEcho);
			dt.setiTotalDisplayRecords(list.getTotalCount());
	        dt.setiTotalRecords(list.getTotalCount());
	        Gson gson = new Gson();
	        String jsondata= gson.toJson(dt);
	        if(list.getEntites() !=null && list.getEntites().size()>0){
	        	
		        StringBuffer json = new StringBuffer();
		        json.append("[");
		        //获取基础数据
		      
		        for(User ms:list.getEntites()){
		        	json.append("{\"id\":").append(ms.getId()).append(",");
		        	json.append("\"adminName\":").append("\"").append(ms.getAdminName()).append("\",");
		        	if(ms.getGroups() !=null && ms.getGroups().size()>0){
		        		json.append("\"groups\":[");
		        		for(Group g:ms.getGroups()){
		        			json.append(g.getId()).append(",");
		        		}
		        		//去掉最后一个逗号
		 		       json.deleteCharAt(json.lastIndexOf(","));
		 		       json.append("],");
		        	}else{
		        		json.append("\"groups\":\"\",");
		        	}
		        	json.append("\"firstName\":").append("\"").append(ms.getFirstName()==null?"":ms.getFirstName()).append("\",");
		        	json.append("\"adminEmail\":").append("\"").append(ms.getAdminEmail()==null?"":ms.getAdminEmail()).append("\",");
		        	json.append("\"active\":").append("\"").append(ms.isActive()).append("\",");
		        	json.append("\"telephone\":").append("\"").append(ms.getTelephone()==null?"":ms.getTelephone()).append("\",");
		        	json.append("\"dateCreated\":").append("\"").append(ms.getAuditSection()==null?"":format.format(ms.getAuditSection().getDateCreated())).append("\",");
		        	json.append("\"loginTime\":").append("\"").append(ms.getLoginTime()==null ? "" :format.format(ms.getLoginTime())).append("\",");
		        	json.append("\"enterEnter\":").append("\"").append(ms.getEnterEnter()==null ? "" :DateUtil.formatDate(ms.getEnterEnter(), "yyyy-MM-dd")).append("\",");
		        	json.append("\"operate\":").append(ms.getId()).append("},");
		        	
		        }
		        json.append("]");
		        //去掉最后一个逗号
		       json.deleteCharAt(json.lastIndexOf(","));
		      
		       jsondata = jsondata.substring(0, jsondata.length()-1)+",\"aaData\":"+json.toString()+"}";
	        }else{
	        	jsondata = jsondata.substring(0, jsondata.length()-1)+",\"aaData\":[]"+"}";
	        }
	        
		     
		        return jsondata;
	        
	     

		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return returnString;		
	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/users/password.html", method=RequestMethod.GET)
	public String displayChangePassword(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model,request);
		//String userName = request.getRemoteUser();
		//User user = userService.getByUserName(userName);
		
		Password password = new Password();
		//password.setUser(user);
		
		model.addAttribute("password",password);
		//model.addAttribute("user",user);
		return ControllerConstants.Tiles.User.password;
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/users/checkoldpwd.shtml", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody String checkOldPwd(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("password");
		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(0);
		try {
			
			if(!StringUtils.isBlank(code)) {
				String userName = request.getRemoteUser();
				User user = userService.getByUserName(userName);
				
				String tempPass = passwordEncoder.encodePassword(code, null);
				
				//password match
				if(!tempPass.equals(user.getAdminPassword())) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setStatusMessage("旧密码不正确");
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error while getting user", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/users/getDepartmentNode.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String getdepartmentNoded(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String uid = request.getParameter("uid"); //用户ID
		//pid一定要存在
		String gid = request.getParameter("gid"); //权限组ID
		try
		{
			List<Department> departments = departmentService.listWithCite();
			//处理部门、工程特性、项目三级目录结构
			Map<String, String> featuresMap = pnodeUtils.getFeaturesNames();
			for(Department p:departments){
				for(Project cs:p.getcSites()){
					String features = cs.getFeatures();
					FeaturesType ftype = p.getFeaturesMap().get(features);
					if (ftype==null) {
						ftype = new FeaturesType();
						ftype.setValue(features);
						ftype.setName(featuresMap.get(features));
						ftype.setSstatus(-1);
						p.getFeaturesMap().put(features, ftype);
					}
					cs.setSstatus(-1);
					ftype.getcSites().add(cs);
				}
			}
			
			//根节点的选中状态， -1未选中，1全部选中，0部分选中
			int stauts=-1; 
			if(!StringUtils.isBlank(uid)){
				List<DepartmentNode> departmentNodes =departmentNodeService.getByGroupID(Integer.parseInt(gid),Long.parseLong(uid));
				if(departmentNodes!=null && departmentNodes.size()>0){
					//全选状态
					boolean isSelectAll=false;
					for(DepartmentNode pnode:departmentNodes){
						if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll()){
							isSelectAll=true;
							for(Department p:departments){
								allSelected(p);
							}
							stauts=1;
							break;
						}
					}
					if(!isSelectAll){
						if (departmentNodes!=null && !departmentNodes.isEmpty()) {
							stauts = 0;
						}
						for(Department p:departments){
							for(DepartmentNode pnode:departmentNodes){
								//处理部门类型权限选择
								if (pnode.getType().equalsIgnoreCase("department")) {
									if (pnode.getDepartmentid().equals(p.getId()) && pnode.isAll()) { //该部门下所有都选中
										allSelected(p);
									}
								} else if (pnode.getType().equalsIgnoreCase("features") && StringUtils.isNotBlank(pnode.getFeatures())) { //处理工程特性，如果工程特性不为空，则说明pnode是按照工程特性进行处理的
									if ( pnode.getDepartmentid().equals(p.getId()) && pnode.isAll()) {
										FeaturesType ftype = p.getFeaturesMap().get(pnode.getFeatures());
										if (ftype!=null) {
											allSelected(ftype);
										}
									}
								} else if (pnode.getType().equalsIgnoreCase("csite")) { //处理项目
									//处理该部门下的工程
									Iterator<Map.Entry<String, FeaturesType>> iter = p.getFeaturesMap().entrySet().iterator();
									while (iter.hasNext()) {
										Map.Entry<String, FeaturesType> entry = iter.next();
										FeaturesType ftype = entry.getValue();
										for(Project cs:ftype.getcSites()){
											if (pnode.getDepartmentid().equals(cs.getId())) {
												cs.setSstatus(1);
												if (ftype.getSstatus()!=1) { //如果没有被全选中，则可以设定为部分选中
													ftype.setSstatus(0);
												}
												if (p.getSstatus()!=1) {
													p.setSstatus(0);
												}
											}
										}
									}
								}
								
							}
						}
					}
				} 
			}
			
			//转json
			if(departments!=null && departments.size()>0){
				StringBuffer json = new StringBuffer();
		        json.append("{\"status\":").append(stauts).append(",\"data\":[");
		       
		        for(Department ms:departments){
		        	json.append("{\"id\":").append(ms.getId()).append(",");
		        	json.append("\"name\":").append("\"").append(ms.getName()).append("\",");
		        	json.append("\"status\":").append("\"").append(ms.getSstatus()).append("\",");
		        	
		        	Map<String, FeaturesType> fmap = ms.getFeaturesMap();
		        	if (!fmap.isEmpty()) {
		        		Iterator<Map.Entry<String, FeaturesType>> iter = fmap.entrySet().iterator();
		        		json.append("\"featureses\":[");
		        		while (iter.hasNext()) {
		        			Map.Entry<String, FeaturesType> entry = iter.next();
		        			FeaturesType ftype = entry.getValue();
		        			json.append("{\"id\":").append("\"").append(ms.getId()+"#"+ftype.getValue()).append("\","); //部门ID-工程特性值
							json.append("\"name\":").append("\"").append(ftype.getName()).append("\",");
							json.append("\"status\":").append("\"").append(ftype.getSstatus()).append("\",");
							json.append("\"cSites\":[");
		        			for(Project csite:ftype.getcSites()){
		        				json.append("{\"id\":").append(csite.getId()).append(",");
								json.append("\"name\":").append("\"").append(csite.getName()).append("\",");
								json.append("\"status\":").append("\"").append(csite.getSstatus()).append("\"},");
		        			}
		        			json.deleteCharAt(json.lastIndexOf(",")).append("]");
							json.append("},");
		        		}
		        		json.deleteCharAt(json.lastIndexOf(",")).append("]");
						json.append("},");
					} else {
						//去掉最后一个逗号
				        json.deleteCharAt(json.lastIndexOf(","));
						json.append("},");
					}
		        }
		        //去掉最后一个逗号
		        json.deleteCharAt(json.lastIndexOf(","));
		        json.append("]}");
		        System.out.println(json.toString());
		       return json.toString();
			}
		}catch (Exception e){
			LOGGER.debug(e.toString());
		}
		
		
		return null;
	}

	/**
	 * 部门下的所有工程特性和项目均选中
	 * @param p
	 */
	private void allSelected(Department p) {
		p.setSstatus(1);
		Iterator<Map.Entry<String, FeaturesType>> iter = p.getFeaturesMap().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, FeaturesType> entry = iter.next();
			FeaturesType ftype = entry.getValue();
			ftype.setSstatus(1);
			for(Project cs:ftype.getcSites()){
				cs.setSstatus(1);
			}
		}
	}
	/**
	 * 工程特性下的所有项目均选中
	 * @param ftype
	 */
	private void allSelected(FeaturesType ftype) {
		ftype.setSstatus(1);
		for(Project cs:ftype.getcSites()){
			cs.setSstatus(1);
		}
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/users/head.html", method=RequestMethod.GET)
	public String userhaed(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model,request);
		String userName = request.getRemoteUser();
		User user = userService.getByUserName(userName);
		model.addAttribute("headimg",user.getHead());
		return "water-head";
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/users/savePassword.html", method=RequestMethod.POST)
	public String changePassword(@ModelAttribute("password") Password password, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		setMenu(model,request);
		String userName = request.getRemoteUser();
		User dbUser = userService.getByUserName(userName);
		
		
		//validate password not empty
		if(StringUtils.isBlank(password.getPassword())) {
			ObjectError error = new ObjectError("password", "密码不允许为空");
			result.addError(error);
			return ControllerConstants.Tiles.User.password;
		}
		

		String tempPass = passwordEncoder.encodePassword(password.getPassword(), null);
		
		//password match
		if(!tempPass.equals(dbUser.getAdminPassword())) {
			ObjectError error = new ObjectError("password", "原密码错误");
			result.addError(error);
			return ControllerConstants.Tiles.User.password;
		}


		
		if(StringUtils.isBlank(password.getNewPassword())) {
			ObjectError error = new ObjectError("newPassword", "新密码不允许为空");
			result.addError(error);
		}
		
		if(StringUtils.isBlank(password.getRepeatPassword())) {
			ObjectError error = new ObjectError("newPasswordAgain", "第二个新密码不允许为空");
			result.addError(error);
		}
		
		if(!password.getRepeatPassword().equals(password.getNewPassword())) {
			ObjectError error = new ObjectError("newPasswordAgain", "两次密码不一致");
			result.addError(error);
		}
		
		if(password.getNewPassword().length()<6) {
			ObjectError error = new ObjectError("newPassword","密码长度不能少于6位字符串");
			result.addError(error);
		}
		
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.User.password;
		}
		
		
		
		String pass = passwordEncoder.encodePassword(password.getNewPassword(), null);
		dbUser.setAdminPassword(pass);
		userService.update(dbUser);
		
		model.addAttribute("success","success");
		return ControllerConstants.Tiles.User.password;
	}
	
	
	/**
	 * From user list
	 * @param id
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/users/displayStoreUser.html", method=RequestMethod.GET)
	public String displayUserEdit(@ModelAttribute("id") Long id, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		User dbUser = userService.getById(id);
		
		if(dbUser==null) {
			LOGGER.info("User is null for id " + id);
			return "redirect:/water/users/list.html";
		}
		
		
		return displayUser(dbUser,model,request,response,locale);

	}
	
	/**
	 * From user profile
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/users/displayUser.html", method=RequestMethod.GET)
	public String displayUserEdit(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		
		String userName = request.getRemoteUser();
		User user = userService.getByUserName(userName);
		return displayUser(user,model,request,response,locale);

	}
	
	private void populateUserObjects(Model model) throws Exception {
		
		//get groups
		List<Group> groups = new ArrayList<Group>();
		List<Group> userGroups = groupService.list();
		for(Group group : userGroups) {
			if(!group.getGroupName().equals(Constants.GROUP_SUPERADMIN)) {
				groups.add(group);
			}
		}
		
		
		model.addAttribute("groups", groups);
		
		
	}
	
	
	
	private String displayUser(User user, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		

		//display menu
		setMenu(model,request);
		
		
		if(user==null) {
			user = new User();
		} else {
			user.setAdminPassword("TRANSIENT");
		}
		
		this.populateUserObjects( model);
		

		model.addAttribute("user", user);
		
		

		return ControllerConstants.Tiles.User.profile;
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/water/users/checkUserCode.shtml", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody String checkUserCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("adminName");
		String id = request.getParameter("id");

		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(0);
		try {
			
			if(!StringUtils.isBlank(code)) {
				if(!StringUtils.isBlank(id)){
					User dbUser = userService.getById(Long.parseLong(id));
					if(dbUser!=null){
						if(dbUser.getAdminName().equalsIgnoreCase(code)){
							return resp.toJSONString();
						}else{
							User user = userService.getByUserName(code);
							if(user !=null){
								resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
								resp.setStatusMessage("该用户名已经存在");
							}else{
								resp.setStatusMessage("您已经修改了登陆用户名，保存成功后需要重新登陆系统！！！");
							}
							
						}
					}
				}
				User user = userService.getByUserName(code);
				if(user !=null){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setStatusMessage("该用户名已经存在");
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error while getting user", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			
		}
		
		
		
		return resp.toJSONString();
	}
	
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/water/users/save.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String save(@Valid @ModelAttribute("user") User user, Model model, HttpServletRequest request, HttpServletResponse response, Locale userLocale)  {
		
		AjaxResponse resp = new AjaxResponse();
		//BaseStore base = (BaseStore)request.getAttribute(Constants.ADMIN_STORE);
		try{
			boolean isNew=false;
			String password=null;
			
			//不允许为空
			if (StringUtils.isBlank(user.getAdminName())) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setStatusMessage("用户名不允许为空");
				return resp.toJSONString();
			} 
			//判断用户名是否存在
			User tmp = userService.getByUserName(user.getAdminName());
			if(tmp!=null){
				if (!(user.getId()!=null&&user.getId().equals(tmp.getId()))) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
					resp.setStatusMessage("用户名已经存在");
					return resp.toJSONString();
				} 
			}
			User dbUser =null;
			if(user.getId()!=null){
				 dbUser = userService.getByUserId(user.getId());
				//获取db的不修改部分
				user.setAdminPassword(dbUser.getAdminPassword());
				user.setAuditSection(dbUser.getAuditSection());
				user.setLastAccess(dbUser.getLastAccess());
				user.setLoginTime(dbUser.getLoginTime());
			}else{
				password = PasswordReset.generateRandomString();
				String encodedPassword = passwordEncoder.encodePassword(password, null);
				user.setAdminPassword(encodedPassword);
				isNew=true;
			}
			
			List<Group> groups =new ArrayList<Group>();
			
			for(Group group:user.getGroups()){
				groups.add(groupService.getById(Integer.parseInt(group.getGroupName())));
			}
			user.setGroups(groups);
			//加载权限树
			String pright=request.getParameter("pright");
			
			Set<DepartmentNode> delNode =null;
			if(!StringUtils.isBlank(pright)){
				//没有起作用
				if(dbUser!=null){
					delNode=dbUser.getpNodes();
				}
				
				//不为空，则需要重新更新部门项目权限组信息
				JSONParser jparser = new JSONParser();
				JSONArray jsonArray = (JSONArray)jparser.parse(pright);
				if (jsonArray != null && jsonArray.size() > 0) { //有，设定部门项目权限组信息
					/**
					 * 1.处理查看权限，如果viewDone=false，说明用户前段没有处理该类型权限，需要保留原有权限信息，否则记录新的权限信息
					 * 2.处理编辑权限，拥有编辑权限，则同时增加一条查看权限，如果editDone=false，说明用户前段没有处理该类型权限，需要保留原有权限信息，否则记录新的权限信息
					 */
					
					Set<DepartmentNode> pns = new HashSet<DepartmentNode>();
//					Group editGroups = groupService.listGroupByPermissionName("EDIT-PROJECT", 1).get(0);
//					Group viewGroups = groupService.listGroupByPermissionName("VIEW-PROJECT", 1).get(0);
					Group viewGroup = groupService.getByName("PROJECTVIEWGROUP");
					Group editGroup = groupService.getByName("PROJECTEDITGROUP");
					boolean viewDone = false; //是否处理了查看权限
					boolean editDone = false; //是否处理了编辑权限
					//处理部门项目权限组-查看权限
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject o = (JSONObject) jsonArray.get(i);
						Long groupid = (Long) o.get("groupid"); //部门项目组ID
						if (groupid.intValue()==viewGroup.getId().intValue()) { 
							viewDone = true;
							JSONArray item = (JSONArray) o.get("item"); 
							if (item.size() > 0) {
								for (int j = 0; j < item.size(); j++) {
									JSONObject it = (JSONObject) item.get(j);
									
									DepartmentNode pn = new DepartmentNode();
									pn.setGroupid(viewGroup.getId()); //查看权限
									pn.setUser(user);
									String type = it.get("type").toString();
									pn.setType(type);
									if (type.equalsIgnoreCase("features")) {
										// 工程特性类型，则id=departmentid+"-"+featuresValue
										String idstr = (String) it.get("id");
										String[] values = idstr.split("#");
										pn.setDepartmentid(Long.valueOf(values[0]));
										pn.setFeatures(values[1]);
									} else {
										pn.setDepartmentid(Long.parseLong(it.get("id").toString()));
									}
									
									boolean ishalf = (boolean) it.get("checkedMiddle");
									if (!ishalf) {
										pn.setAll(true);
									}
									pns.add(pn);
								}
							}
							break;
						}
					}
					if (!viewDone) { //保留原有查看信息
						//获取用户原有的部门项目权限组查看权限
						if (dbUser!=null) {
							List<DepartmentNode> viewDepartNodes = departmentNodeService.getByGroupID(viewGroup.getId(), dbUser.getId());
							user.getpNodes().addAll(viewDepartNodes);
						}
					}
					//处理部门项目权限组-编辑权限
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject o = (JSONObject) jsonArray.get(i);
						Long groupid = (Long) o.get("groupid"); //部门项目组ID
						if (groupid.intValue()==editGroup.getId().intValue()) {
							editDone = true;
							JSONArray item = (JSONArray) o.get("item"); 
							if (item.size() > 0) {
								for (int j = 0; j < item.size(); j++) {
									JSONObject it = (JSONObject) item.get(j);
									DepartmentNode editpn = new DepartmentNode();//编辑权限
									DepartmentNode viewpn = new DepartmentNode();//查看权限
									
									editpn.setGroupid(editGroup.getId()); 
									viewpn.setGroupid(viewGroup.getId()); 
									String type = it.get("type").toString();
									editpn.setUser(user);
									viewpn.setUser(user);
									editpn.setType(type);
									viewpn.setType(type);
									if (type.equalsIgnoreCase("features")) {
										// 工程特性类型，则id=departmentid+"_"+featuresValue
										String idstr = (String) it.get("id");
										String[] values = idstr.split("#");
										editpn.setDepartmentid(Long.valueOf(values[0]));
										editpn.setFeatures(values[1]);
										viewpn.setDepartmentid(Long.valueOf(values[0]));
										viewpn.setFeatures(values[1]);
									} else {
										editpn.setDepartmentid(Long.parseLong(it.get("id").toString()));
										viewpn.setDepartmentid(Long.parseLong(it.get("id").toString()));
									}
									
									boolean ishalf = (boolean) it.get("checkedMiddle");
									if (!ishalf) {
										editpn.setAll(true);
										viewpn.setAll(true);
									}
									pns.add(editpn);
									pns.add(viewpn);
								}
								addViewGroup(user, viewGroup);
							}
							break;
						}
					}
					if (!editDone) { //保留原有编辑信息
						//获取用户原有的部门项目权限组编辑权限
						if (dbUser!=null) {
							List<DepartmentNode> editDepartNodes = departmentNodeService.getByGroupID(editGroup.getId(), dbUser.getId());
							//复制编辑权限至查看权限
							if (!editDepartNodes.isEmpty()) {
								Set<DepartmentNode> viewDepartNodes = new HashSet<DepartmentNode>();
								for (DepartmentNode departmentNode : editDepartNodes) {
									DepartmentNode viewDepartNode = new DepartmentNode();
									PropertyUtils.copyProperties(viewDepartNode, departmentNode);
									viewDepartNode.setGroupid(viewGroup.getId());
									viewDepartNode.setId(null);
									viewDepartNodes.add(viewDepartNode);
								}
								user.getpNodes().addAll(editDepartNodes);
								user.getpNodes().addAll(viewDepartNodes);
								addViewGroup(user, viewGroup);
							}
						}
					}
					user.getpNodes().addAll(pns);
				} else {
					//没有，则清空权限
					if(dbUser!=null){
						delNode=dbUser.getpNodes();
					}
				}
				userService.saveOrUpdate(user,delNode);
				userDetailsService.removeSession(user);
			} else {
				//页面没有处理部门项目权限组信息，则保留用户原来的部门项目组信息
				if(dbUser!=null){
					user.setpNodes(dbUser.getpNodes());
				}
				userService.saveOrUpdate(user);
			}
			if(isNew){
				try{
					//creation of a user, send an email
					emailTemplatesUtils.sendUserRegistrationEmail(userLocale, request, user, password);
				}catch (Exception e){
					resp.setStatus(7);
					resp.setStatusMessage("用户创建成功，但是邮件发送失败！！！");
				}
			}
			
			//权限到缓存
			//pnodeUtils.resetUser(user, departmentNodeService);
			
		}catch (Exception e){
			LOGGER.error("Error while save store ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}

	/**
	 * 增加用户的部门项目查看group权限
	 * @param user
	 * @param viewGroup
	 */
	private void addViewGroup(User user, Group viewGroup) {
		//增加用户的部门项目查看group权限
		boolean hasview = false;
		for (Group group : user.getGroups()) {
			if (group.getId().equals(viewGroup.getId())) {
				hasview = true;
				break;
			}
		}
		if (!hasview) { //如果没有，则增加
			user.getGroups().add(viewGroup);
		}
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/users/saveuser.html", method=RequestMethod.POST)
	public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpServletRequest request, Locale locale,HttpServletResponse response) throws Exception {

		setMenu(model,request);
			
		this.populateUserObjects(model);
		User dbUser = null;
		//edit mode, need to get original user important information
		if(user.getId()!=null) {
			dbUser = userService.getByUserId(user.getId());
			if(dbUser==null) {
				return "redirect:/water/users/displayUser.html";
			}
		}
		
		if(user.getId()!=null && user.getId()>0) {
			if(user.getId().longValue()!=dbUser.getId().longValue()) {
				return "redirect:/water/users/displayUser.html";
			}
			
			List<Group> groups = dbUser.getGroups();
			//boolean removeSuperAdmin = true;
			user.setGroups(groups);

		}
		
		if (result.hasErrors()) {
			return ControllerConstants.Tiles.User.profile;
		}
		
		if(user.getId()!=null && user.getId()>0) {
			user.setAdminPassword(dbUser.getAdminPassword());
		} else {
			String encoded = passwordEncoder.encodePassword(user.getAdminPassword(),null);
			user.setAdminPassword(encoded);
		}
		userService.saveOrUpdate(user);
		//如果用户名当前登陆的用户名改了，需要重新设置remote用户
		String userName = request.getRemoteUser();
		if(userName.equalsIgnoreCase(dbUser.getAdminName()) && !userName.equalsIgnoreCase(user.getAdminName())){
			//logoff the user
		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    if (auth != null){  
		    	try{
		    		 new SecurityContextLogoutHandler().logout(request, response, auth);
			         //new PersistentTokenBasedRememberMeServices().logout(request, response, auth);
		    	}catch (Exception e){
		    		
		    	}
		        
		    }
			
			return "water/logon";
		}
		

		model.addAttribute("success","success");
		return ControllerConstants.Tiles.User.profile;
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/water/users/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();


		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				try {
					Long userId = Long.valueOf(listId[i]);
					User user = userService.getByUserId(userId);
					/**
					 * In order to remove a User the logged in ser must be STORE_ADMIN
					 * or SUPER_USER
					 */

					if(user==null){
						resp.setStatusMessage("用户不存在");
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
						resplist.add(resp);
						continue;
					}
					
					if(!request.isUserInRole(Constants.GROUP_ADMIN)) {
						resp.setStatusMessage("非管理员用户不能操作");
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
						resplist.add(resp);
						continue;
					}

					//check if the user removed has group ADMIN
					boolean isAdmin = false;
					if(UserUtils.userInGroup(user, Constants.GROUP_SUPERADMIN)) {
						isAdmin = true;
					}
					
					if(isAdmin) {
						resp.setStatusMessage("不允许删除超级管理员用户");
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
						resplist.add(resp);
						continue;
					}
					userService.deleteUser(user);
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					resplist.add(resp);
				} catch (Exception e) {
					LOGGER.error("Error while deleting product price", e);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorMessage(e);
					resplist.add(resp);
				}
			}
		}
		return resplist;
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/water/users/setpassword.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody List<AjaxResponse> setpass(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		String password = request.getParameter("newpass");

		if (listId!=null&&listId.length>0 && StringUtils.isNotBlank(password)) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				try {
					Long userId = Long.valueOf(listId[i]);
					User user = userService.getByUserId(userId);
					/**
					 * In order to remove a User the logged in ser must be STORE_ADMIN
					 * or SUPER_USER
					 */

					if(user==null){
						resp.setStatusMessage("用户不存在");
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
						resplist.add(resp);
						continue;
					}
					
					if(!request.isUserInRole(Constants.GROUP_ADMIN)) {
						resp.setStatusMessage("非管理员用户不能操作");
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
						resplist.add(resp);
						continue;
					}

					
					String encodedPassword = passwordEncoder.encodePassword(password, null);
					user.setAdminPassword(encodedPassword);
					userService.saveOrUpdate(user);
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					resplist.add(resp);
				} catch (Exception e) {
					LOGGER.error("Error while deleting product price", e);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorMessage(e);
					resplist.add(resp);
				}
			}
		}
		return resplist;
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/water/users/active.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String active(HttpServletRequest request, Locale locale) throws Exception {
		
		String sUserId = request.getParameter("listId");

		AjaxResponse resp = new AjaxResponse();
		
		//String userName = request.getRemoteUser();
		//User remoteUser = userService.getByUserName(userName);

		
		try {
			
			Long userId = Long.parseLong(sUserId);
			User user = userService.getByUserId(userId);
			
			/**
			 * In order to remove a User the logged in ser must be STORE_ADMIN
			 * or SUPER_USER
			 */
			

			if(user==null){
				resp.setStatusMessage("用户不存在");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}
			
			if(!request.isUserInRole(Constants.GROUP_ADMIN)) {
				resp.setStatusMessage("非管理员用户不能操作");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}

			
			//check if the user removed has group ADMIN
			boolean isAdmin = false;
			if(UserUtils.userInGroup(user, Constants.GROUP_ADMIN) || UserUtils.userInGroup(user, Constants.GROUP_SUPERADMIN)) {
				isAdmin = true;
			}

			
			if(isAdmin) {
				resp.setStatusMessage("不能更改超级管理员用户状态");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}
			boolean isActive = !user.isActive();
			user.setActive(isActive);
			userService.saveOrUpdate(user);
			
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			resp.setStatusMessage("操作成功");
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
		
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/water/users/repassword.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String rePassword(HttpServletRequest request, Locale locale) throws Exception {
		
		//do not remove super admin
		
		String sUserId = request.getParameter("listId");

		AjaxResponse resp = new AjaxResponse();
		
		String userName = request.getRemoteUser();
		User remoteUser = userService.getByUserName(userName);

		
		try {
			
			Long userId = Long.parseLong(sUserId);
			User user = userService.getByUserId(userId);
			
			/**
			 * In order to remove a User the logged in ser must be STORE_ADMIN
			 * or SUPER_USER
			 */
			

			if(user==null){
				resp.setStatusMessage("用户不存在");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}
			
			if(!request.isUserInRole(Constants.GROUP_ADMIN)) {
				resp.setStatusMessage("非管理员用户不能操作");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}

			
			//check if the user removed has group ADMIN
			boolean isAdmin = false;
			if(UserUtils.userInGroup(remoteUser, Constants.GROUP_ADMIN) || UserUtils.userInGroup(remoteUser, Constants.GROUP_SUPERADMIN)) {
				isAdmin = true;
			}

			
			if(!isAdmin) {
				resp.setStatusMessage("不能重置超级管理员密码");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			}
			String password = PasswordReset.generateRandomString();
			String encodedPassword = passwordEncoder.encodePassword(password, null);
			user.setAdminPassword(encodedPassword);
			userService.saveOrUpdate(user);
			emailTemplatesUtils.sendRetPassword(user, request, locale,password);
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			resp.setStatusMessage("操作成功");

		
		
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while rePassword", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("profile", "profile");
		activeMenus.put("user", "create-user");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("profile");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/users/removeHead.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String removeImage( HttpServletRequest request, HttpServletResponse response) {

		
		AjaxResponse resp = new AjaxResponse();
		
		try {
			String userName = request.getRemoteUser();
			User user = userService.getByUserName(userName);
			String fileName = user.getHead();
			
			user.setHead(null);
			userService.update(user);
			try{
				contentService.removeFile(FileContentType.IMAGE,fileName);
			}catch (Exception e){
				LOGGER.error("Error while deleting user Head remove img fail", e);
			}
			
			//放到seesion
            request.getSession().setAttribute(Constants.ADMIN_USER, user);
		} catch (Exception e) {
			LOGGER.error("Error while deleting user Head ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/users/savehead.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String saveUserhaed(@RequestParam(value="contentImages", required=false) @Valid final CommonsMultipartFile contentImages,
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		String userName = request.getRemoteUser();
		User user = userService.getByUserName(userName);
		AjaxResponse resp = new AjaxResponse();
		try{
			if(contentImages != null) {

				String imageName = contentImages.getOriginalFilename();
				//图片没有改动不用保存
				if(imageName !=""){
					InputStream inputStream = contentImages.getInputStream();
				
					BufferedImage imBuff = ImageIO.read(inputStream);
					int type = imBuff.getType() == 0? BufferedImage.TYPE_INT_ARGB : imBuff.getType();
					imBuff =ImageUtils.resizeImage(imBuff, 45, 45, type);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ImageIO.write(imBuff, "jpeg", os);
					inputStream = new ByteArrayInputStream(os.toByteArray());
					
		            String in = RadomSixNumber.getImageName(imageName, Constants.IMAGE_PNG);
		            InputContentFile cmsContentImage = new InputContentFile();
		            cmsContentImage.setFileName(in);
		            cmsContentImage.setMimeType( contentImages.getContentType());
		            cmsContentImage.setFile( inputStream );
		            cmsContentImage.setFileContentType(FileContentType.IMAGE);
		            contentService.addLogo(cmsContentImage);
		            user.setHead(in);
		            userService.saveOrUpdate(user);
		            //放到seesion
		            request.getSession().setAttribute(Constants.ADMIN_USER, user);
				}
			}
		}catch(Exception e){
			LOGGER.error("Error while save base_store ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	//password reset functionality  ---  Sajid Shajahan  
	/**
	 * 发起用户密码重置请求
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@RequestMapping(value="/water/users/resetPassword.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String resetPassword(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
		AjaxResponse resp = new AjaxResponse();
		String userName = request.getParameter("usercode");

		try {
			//BaseStore base = basestoreService.getBaseStore();
			if (!StringUtils.isBlank(userName)) {
				User dbUser = userService.getByUserName(userName);
				if (dbUser == null) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
					resp.setStatusMessage("用户不存在");
					return resp.toJSONString();
				}
				String radom = RadomSixNumber.getRadomNumber();
				dbUser.setTemp(radom);
				userService.saveOrUpdate(dbUser);
				emailTemplatesUtils.sendRetPasswordUserEmail(dbUser, request, locale);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			} else {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
				resp.setStatusMessage("重置密码失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE_MESSAGE);
			resp.setStatusMessage("重置密码失败");
			return resp.toJSONString();
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	/**
	 * 重置密码链接校验
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/water/users/verify.html", method=RequestMethod.GET)
	public String resetPasswordVerify(Model model, HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception {
		String account= request.getParameter("account");
		String  code = request.getParameter("code");
		//BaseStore base = basestoreService.getBaseStore();
		if(!StringUtils.isBlank(account) && !StringUtils.isBlank(code)){
			User dbUser = userService.getByUserName(account);
			if(dbUser!=null && dbUser.getTemp().equalsIgnoreCase(code)){
				String password = PasswordReset.generateRandomString();
				String encodedPassword = passwordEncoder.encodePassword(password, null);
				dbUser.setAdminPassword(encodedPassword);
				String temp=RadomSixNumber.getRadomNumber();
				dbUser.setTemp(temp);
				try{
					emailTemplatesUtils.sendRetPasswordUser(dbUser,request,locale,password);
					userService.saveOrUpdate(dbUser);
					model.addAttribute( "image", "shield_ok.ico" );
					model.addAttribute("information", "密码重置成功");
				}catch (Exception e){
					e.printStackTrace();
					model.addAttribute( "image", "shield_error.png" );
					model.addAttribute("information", "密码重置失败");
				}
			}else{
				model.addAttribute( "image", "shield_error.png" );
				model.addAttribute("information", "密码重置无效");
			}
		}else{
			model.addAttribute( "image", "shield_error.png" );
			model.addAttribute("information", "密码重置无效");
		}
		
		/** template **/
		//StringBuilder template = new StringBuilder().append(com.kekeinfo.web.shop.controller.ControllerConstants.Tiles.Customer.information).append(".").append(base.getStoreTemplate());
		return  "water/infomation";
	}
	
}
