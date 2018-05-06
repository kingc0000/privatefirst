package com.kekeinfo.web.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.basedata.service.BaseDataService;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.user.service.UserService;



@Component
public class ReloadMemoryData {
	private ServletContext context;
	
	@Autowired UserService userService;
	@Autowired DepartmentNodeService departmentNodeService;
	@Autowired DepartmentService departmentService;
	@Autowired private BaseDataService baseDataService;
	@Autowired private PNodeUtils pnodeUtils;
	
	public void reload(){
		try {
			//获取所有非admin账号
			/**
			List<String> ps = new ArrayList<>();
			ps.add("SUPERADMIN");
			ps.add("ADMIN");
			List<User> users = userService.getByPermission(ps);
			
			HashMap<Long, List<Long>> peids = new HashMap<>();
			HashMap<Long, List<Long>> pvids = new HashMap<>();
			HashMap<Long, List<Long>> ceids = new HashMap<>();
			HashMap<Long, List<Long>> cvids = new HashMap<>();
			for(User u:users){
				List<Long> peidss = PNodeUtils.getIds(u.getId(), "csite", "EDIT-PROJECT", departmentNodeService);
				peids.put(u.getId(), peidss);
				List<Long> pvidss = PNodeUtils.getIds(u.getId(), "csite", "VIEW-PROJECT", departmentNodeService);
				pvids.put(u.getId(), pvidss);
				List<Long> ceidss = PNodeUtils.getIds(u.getId(), "department", "EDIT-PROJECT", departmentNodeService);
				ceids.put(u.getId(), ceidss);
				List<Long> cvidss = PNodeUtils.getIds(u.getId(), "department", "VIEW-PROJECT", departmentNodeService);
				cvids.put(u.getId(), cvidss);
				
			}
			
			context.setAttribute(Constants.PROJECTEDIT, peids);
			context.setAttribute(Constants.PROJECTVIEW, pvids);	
			context.setAttribute(Constants.CSITEEDIT,ceids);
			context.setAttribute(Constants.CSITEVIEW,cvids);
			*/
//			PNodeUtils.reloadAllUser(context, departmentNodeService, userService);
			pnodeUtils.reloadAllDepartments();
			pnodeUtils.reloadAllCsite(null);
			pnodeUtils.reloadAllGuard();
			pnodeUtils.reloadAllMonitor();
			pnodeUtils.relaodAllGroupUser();
			pnodeUtils.reloadAllUserJobs();
			pnodeUtils.reloadAllAuto();
			pnodeUtils.reloadAllAutoDeep();
			
			//加载基础数据
			List<String> attributes = new ArrayList<String>();
			attributes.add("distinct(TYPE)");
			List<Object[]> bbtype= baseDataService.getBySql(attributes, "BASEDATA_TYPE", " ");
			for (Object objects : bbtype) {
				HashMap<String, String> orders = new HashMap<String, String>();
				orders.put("order", "asc");
				Entitites<BasedataType> entities = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{objects.toString()}, orders);
				context.setAttribute(objects.toString(), entities.getEntites());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}
}
