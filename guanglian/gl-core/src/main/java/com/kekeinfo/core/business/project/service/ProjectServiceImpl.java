package com.kekeinfo.core.business.project.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.guard.service.GuardService;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.business.monitor.service.MonitorService;
import com.kekeinfo.core.business.project.dao.ProjectDao;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.PermissionUtils;

@Service("projectService")
public class ProjectServiceImpl extends KekeinfoEntityServiceImpl<Long, Project> implements ProjectService {
	private ProjectDao projectDao;

	@Autowired CSiteService cSiteService;
	@Autowired MonitorService monitorService;
	@Autowired DepartmentService departmentService;
	@Autowired UserService userService;
	@Autowired GroupService groupService;
	@Autowired GuardService guardService;
	
	@Autowired
	public ProjectServiceImpl(ProjectDao projectDao) {
		super(projectDao);
		this.projectDao = projectDao;
	}

	@Override
	public Project getByIdWithDepartment(long pid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.projectDao.getByIdWithDepartment(pid);
	}
	
	/**
	 * 0：正常
	 * @param project
	 * @return
	 * @throws ServiceException
	 */
	private int createSub(Project project) throws ServiceException{
		//如果是地下水部门要新建一个csite，如果是监控部门要新建一个MONITOR
		Department de = departmentService.getById(project.getDepartment().getId());
		if(StringUtils.isNotBlank(de.getCode())){
			if(de.getCode().equalsIgnoreCase("water")){
				if(project.getId()!=null){
					List<ConstructionSite> cs =cSiteService.getByPid(project.getId());
					if(cs!=null && cs.size()>0){
						return 0;
					}
				}
				
				ConstructionSite csite =new ConstructionSite();
				csite.setProject(project);
				cSiteService.save(csite);
				return 1;
			}else if(de.getCode().equalsIgnoreCase("monitor")){
				if(project.getPtype()==0){
					if(project.getId()!=null){
						List<Monitor> ms =monitorService.getByPid(project.getId());
						if(ms!=null && ms.size()>0){
							return 0;
						}
					}
					Monitor monitor = new Monitor();
					monitor.setProject(project);
					monitorService.save(monitor);
					return 2;
				}else{
					if(project.getId()!=null){
						List<com.kekeinfo.core.business.guard.model.Guard> ms =guardService.getByPid(project.getId());
						if(ms!=null && ms.size()>0){
							return 0;
						}
					}
					com.kekeinfo.core.business.guard.model.Guard monitor = new com.kekeinfo.core.business.guard.model.Guard();
					monitor.setProject(project);
					guardService.save(monitor);
					return 3;
				}
				
			}
		}
		return 0;
	}
	
	/**
	 * 0:正常
	 * 1：地下水
	 * 2：监控
	 * 3：监护
	 */
	@Transactional
	@Override
	public int  saveOrUpdate(Project project,User user) throws ServiceException{
		int index=0;
		//用户是否改变
		boolean isChange=true;
		if(project.getId()==null){
			this.save(project);
			index =createSub(project);
		}else{
			//修改管理员的操作
			Project dbD = this.getById(project.getId());
			if(dbD !=null){
				if(dbD.getProjectOwnerid()!=null){
					User pUser = userService.getById(dbD.getProjectOwnerid());
					if(pUser!=null ){
						if(!pUser.getId().equals(project.getProjectOwnerid())){
							Set<DepartmentNode> dps = pUser.getpNodes();
							if(dps!=null && dps.size()>0){
								Iterator<DepartmentNode> iter = dps.iterator();
								 while (iter.hasNext()) {
									 DepartmentNode dn = (DepartmentNode) iter.next();
									 if(dn.getDepartmentid().equals(project.getId())){
										 iter.remove();
									 }
								 }
							}
							userService.saveOrUpdate(pUser);
						}else{
							isChange=false;
						}
					}
				}
			}
			//修改所属部门的操作
			if(!dbD.getDepartment().getId().equals(project.getDepartment().getId()) || dbD.getPtype()!=project.getPtype()){
				createSub(project);
			}
			this.update(project);
		}
		
		if(user !=null && isChange==true){
			//判断用户是否属于超级用户
			List<Group> sgroups = groupService.listGroupByPermissionName("SUPERADMIN",0);
			boolean isadd = PermissionUtils.isInGroup(user, sgroups);
			if(!isadd){
				sgroups = groupService.listGroupByPermissionName("ADMIN",0);
				isadd = PermissionUtils.isInGroup(user, sgroups);
			}
			
			if(!isadd){
				//查找编辑权限的组
				List<Group> groups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
				if(!groups.isEmpty()){
					//查找对部门是否有编辑权限
					isadd = PermissionUtils.isAdd(user.getpNodes(), groups, project.getDepartment().getId(), "csite");
					if(!isadd){
						isadd = PermissionUtils.isAdd(user.getpNodes(), groups,project.getId(),"csite");
					}
					 
					if(!isadd){
						DepartmentNode pn= new DepartmentNode();
						pn.setGroupid(groups.get(0).getId());
						pn.setDepartmentid(project.getId());
						pn.setUser(user);
						pn.setType("csite");
						user.getpNodes().add(pn);
					}
					
					isadd=PermissionUtils.findGroup(user.getGroups(), groups);
					
					if(!isadd){
						user.getGroups().add(groups.get(0));
					}
					
					//设置相应的查看权限
					Group vgroup = PermissionUtils.getViewGroup(groupService);
					List<Group> vgroups = new ArrayList<>();
					vgroups.add(vgroup);
					isadd = PermissionUtils.isAdd(user.getpNodes(), vgroups,project.getId(),project.getDepartment().getId());
					if(!isadd){
						DepartmentNode pn= new DepartmentNode();
						pn.setGroupid(vgroup.getId());
						pn.setDepartmentid(project.getId());
						pn.setUser(user);
						pn.setType("csite");
						pn.setAll(true);
						user.getpNodes().add(pn);
						DepartmentNode dpn= new DepartmentNode();
						dpn.setGroupid(vgroup.getId());
						dpn.setDepartmentid(project.getDepartment().getId());
						dpn.setUser(user);
						dpn.setType("department");
						dpn.setAll(false);
						user.getpNodes().add(dpn);
					}
					
					isadd=PermissionUtils.findGroup(user.getGroups(), vgroup);
					if(!isadd){
						user.getGroups().add(vgroup);
					}
					userService.saveOrUpdate(user);
				}
			}
		}
		return index;
	}

	@Override
	public List<Project> withGroupUser() throws ServiceException {
		// TODO Auto-generated method stub
		return this.projectDao.withGroupUser();
	}
	
	@Override 
	@Transactional
	public void delete(Project project)throws ServiceException{
		super.deleteById(project.getId());
		List<ConstructionSite> cs =cSiteService.getByPid(project.getId());
		if(cs!=null && cs.size()>0){
			for(ConstructionSite c:cs){
				cSiteService.delete(c);
			}
		}
		List<Monitor> ms =monitorService.getByPid(project.getId());
		for(Monitor m:ms){
			monitorService.delete(m);
		}
		List<com.kekeinfo.core.business.guard.model.Guard> gs =guardService.getByPid(project.getId());
		for(com.kekeinfo.core.business.guard.model.Guard m:gs){
			guardService.delete(m);
		}
		
	}

	@Override
	public Project withUserGroup(long pid) throws ServiceException {
		// TODO Auto-generated method stub
		return projectDao.withUserGroup(pid);
	}
}
