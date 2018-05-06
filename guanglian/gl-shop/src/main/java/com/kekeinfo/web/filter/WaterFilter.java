package com.kekeinfo.web.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.CacheUtils;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;


public class WaterFilter extends HandlerInterceptorAdapter {
	

	private static final Logger LOGGER = LoggerFactory.getLogger(WaterFilter.class);
	
	
	@Autowired
	private UserService userService;
	
	@Autowired private SessionRegistry sessionRegistry; 
		
	@Autowired
	private CacheUtils cache;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
		

		//提成用户
		HttpSession session = request.getSession(false);
	    if (session != null) {  
	        SessionInformation info = sessionRegistry.getSessionInformation(session.getId());  
	  
	        if (info != null) {  
	            if (info.isExpired()) {  
	            	response.sendRedirect(request.getContextPath() + "/water/j_spring_security_logout");
	            	return true;
	            }
	        }
	    }
		
		request.setCharacterEncoding("UTF-8");
		Map<String,Menu> menus = (Map<String,Menu>) cache.getFromCache("MENUMAP"); //系统管理菜单
		Map<String,Menu> watermenus = (Map<String,Menu>) cache.getFromCache("WATERMENUMAP"); //地下水菜单
		Map<String,Menu> monitormenus = (Map<String,Menu>) cache.getFromCache("MONITORMENUMAP"); //监控部门菜单
		Map<String,Menu> guardmenus = (Map<String,Menu>) cache.getFromCache("GUARDMENUMAP"); //监控部门菜单
		
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		
		
		
		/**
		if(request.getRequestURL().toString().toLowerCase().contains(SERVICES_URL_PATTERN)
			    || request.getRequestURL().toString().toLowerCase().contains(REFERENCE_URL_PATTERN)) {
		    return true;
		}
		*/
		String userName = request.getRemoteUser();
		
		if(userName==null) {//** IMPORTANT FOR SPRING SECURITY **//
			//response.sendRedirect(new StringBuilder().append(request.getContextPath()).append("/").append("/water").toString());
		} else {
		
			if(user==null) {
				user = userService.getByUserName(userName);
				//store=null;
			}
			
			if(user==null) {
				//response.sendRedirect(request.getContextPath() + "/water/unauthorized.html");
				return true;
			}
			if(!user.getAdminName().equals(userName)) {
				user = userService.getByUserName(userName);
			}
			//获取浏览器类型
			if(user.getuAgent()!=null && StringUtils.isNotBlank(user.getuAgent())){
				//判断流量器类型
				String ua = request.getHeader("User-Agent");
				//非手机用户，但是用户已经从手机登录了
				if(ua.indexOf("android")==-1 &&ua.indexOf("iOS")==-1){
					user.setuAgent(null);
				}
			}
			
			request.getSession().setAttribute(Constants.ADMIN_USER, user);
			request.setAttribute("headimg", user.getHead());
			request.getSession().setAttribute("app", user.getuAgent());
			request.setAttribute("app", user.getuAgent());
			/**
			if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")){
				List<departmentNode> edits = departmentNodeService.getByTypePName("project", user.getId(), "VIEW-PROJECT");
			}
			*/
		}
		
		
		
		//加载系统管理菜单
		if(menus==null) {
			InputStream in = null;
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			try {
				in =
					(InputStream) this.getClass().getClassLoader().getResourceAsStream("admin/menu.json");

				Map<String,Object> data = mapper.readValue(in, Map.class);

				menus = new LinkedHashMap<String,Menu>();
				List objects = (List)data.get("menus");
				for(Object object : objects) {
					Menu m = getMenu(object);
					menus.put(m.getCode(),m);
				}

				cache.putInCache(menus,"MENUMAP");
			} catch (JsonParseException e) {
				LOGGER.error("Error while creating menu", e);
			} catch (JsonMappingException e) {
				LOGGER.error("Error while creating menu", e);
			} catch (IOException e) {
				LOGGER.error("Error while creating menu", e);
			} finally {
				if(in !=null) {
					try {
						in.close();
					} catch (Exception ignore) {
					}
				}
			}
		} 
		
		//加载地下水管理菜单
		if(watermenus==null) {
			InputStream in = null;
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			try {
				in =
					(InputStream) this.getClass().getClassLoader().getResourceAsStream("admin/water.json");

				Map<String,Object> data = mapper.readValue(in, Map.class);

				watermenus = new LinkedHashMap<String,Menu>();
				List objects = (List)data.get("menus");
				for(Object object : objects) {
					Menu m = getMenu(object);
					watermenus.put(m.getCode(),m);
				}

				cache.putInCache(watermenus,"WATERMENUMAP");
			} catch (JsonParseException e) {
				LOGGER.error("Error while creating water menu", e);
			} catch (JsonMappingException e) {
				LOGGER.error("Error while creating water menu", e);
			} catch (IOException e) {
				LOGGER.error("Error while creating water menu", e);
			} finally {
				if(in !=null) {
					try {
						in.close();
					} catch (Exception ignore) {
					}
				}
			}
		} 

		//加载监控部门菜单
		if(monitormenus==null) {
			InputStream in = null;
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			try {
				in =
					(InputStream) this.getClass().getClassLoader().getResourceAsStream("admin/monitor.json");

				Map<String,Object> data = mapper.readValue(in, Map.class);

				monitormenus = new LinkedHashMap<String,Menu>();
				List objects = (List)data.get("menus");
				for(Object object : objects) {
					Menu m = getMenu(object);
					monitormenus.put(m.getCode(),m);
				}

				cache.putInCache(monitormenus,"MONITORMENUMAP");
			} catch (JsonParseException e) {
				LOGGER.error("Error while creating monitor menu", e);
			} catch (JsonMappingException e) {
				LOGGER.error("Error while creating monitor menu", e);
			} catch (IOException e) {
				LOGGER.error("Error while creating monitor menu", e);
			} finally {
				if(in !=null) {
					try {
						in.close();
					} catch (Exception ignore) {
					}
				}
			}
		}
		
		//加载监护部门菜单
				if(guardmenus==null) {
					InputStream in = null;
					ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
					try {
						in =
							(InputStream) this.getClass().getClassLoader().getResourceAsStream("admin/guard.json");

						Map<String,Object> data = mapper.readValue(in, Map.class);

						guardmenus = new LinkedHashMap<String,Menu>();
						List objects = (List)data.get("menus");
						for(Object object : objects) {
							Menu m = getMenu(object);
							guardmenus.put(m.getCode(),m);
						}

						cache.putInCache(guardmenus,"GUARDMENUMAP");
					} catch (JsonParseException e) {
						LOGGER.error("Error while creating guard menu", e);
					} catch (JsonMappingException e) {
						LOGGER.error("Error while creating guard menu", e);
					} catch (IOException e) {
						LOGGER.error("Error while creating guard menu", e);
					} finally {
						if(in !=null) {
							try {
								in.close();
							} catch (Exception ignore) {
							}
						}
					}
				}
		
		
		List<Menu> list = new ArrayList<Menu>(menus.values());
		request.setAttribute("MENULIST", list);
		
		List<Menu> waterlist = new ArrayList<Menu>(watermenus.values());
		request.setAttribute("WATERMENULIST", waterlist);
		
		List<Menu> monitorlist = new ArrayList<Menu>(monitormenus.values());
		request.setAttribute("MONITORMENULIST", monitorlist);
		
		List<Menu>guardlist = new ArrayList<Menu>(guardmenus.values());
		request.setAttribute("GUARDMENULIST", guardlist);
		
		request.setAttribute("MENUMAP", menus);
		request.setAttribute("WATERMENUMAP", watermenus);
		
		response.setCharacterEncoding("UTF-8");
		
		return true;
	}
	
	
	@SuppressWarnings("rawtypes")
	private Menu getMenu(Object object) {
		
		Map o = (Map)object;
		Map menu = (Map)o.get("menu");
		
		Menu m = new Menu();
		m.setCode((String)menu.get("code"));
		m.setName((String)menu.get("name"));
		
		m.setUrl((String)menu.get("url"));
		m.setIcon((String)menu.get("icon"));
		m.setRole((String)menu.get("role"));
		
		List menus = (List)menu.get("menus");
		if(menus!=null) {
			for(Object oo : menus) {
				
				Menu mm = getMenu(oo);
				m.getMenus().add(mm);
			}
			
		}
		
		return m;
		
	}

}
