package com.kekeinfo.core.business.department.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.camera.model.Camera;
import com.kekeinfo.core.business.camera.service.CameraService;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.department.dao.DepartmentDao;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.PermissionUtils;

@Service("departmentService")
public class DepartmentServiceImpl extends KekeinfoEntityServiceImpl<Long, Department> 
		implements DepartmentService {
	

		
	@Autowired UserService userService;
	
	@Autowired GroupService groupService;
	
	@Autowired CameraService cameraService;
	
	@Autowired CSiteService cSiteService;
		
	private DepartmentDao departmentDao;
	
	@Autowired
	public DepartmentServiceImpl(DepartmentDao departmentDao) {
		super(departmentDao);
		this.departmentDao = departmentDao;
	}

	
	//@Override
	public Department getMerchantStore(String merchantStoreCode) throws ServiceException {
		return departmentDao.getMerchantCode(merchantStoreCode);
	}
	
	@Override
	public void saveOrUpdate(Department store) throws ServiceException {
		
		if(store.getId()==null) {
			super.save(store);
		} else {
			super.update(store);
		}
	}
	
	@Override
	public Department getByName(String code) throws ServiceException {
		
		return departmentDao.getMerchantStore(code);
	}
	
	@Override
	public void delete(Department merchant) throws ServiceException {
		
		super.delete(merchant);
	}


	@Override
	public List<Object[]> getPinYin() throws ServiceException {
		// TODO Auto-generated method stub
		return departmentDao.getPinYin();
	}


	@Override
	public List<Department> listWithCite() throws ServiceException {
		// TODO Auto-generated method stub
		return departmentDao.listWithCite();
	}


	@Override
	public List<Department> listByCite(List<Long> cids) throws ServiceException {
		// TODO Auto-generated method stub
		return departmentDao.listByCite(cids);
	}


	@Transactional
	@Override
	public void saveOrUpdateWithUser(Department store, User user) throws ServiceException {
		// TODO Auto-generated method stub
		//用户是否改变
		boolean isChange=true;
		//修改管理员的操作
		if(store.getId()!=null){
			Department dbD = this.getById(store.getId());
			store.setcSites(dbD.getcSites());
			store.setAuditSection(dbD.getAuditSection());
			if(dbD !=null){
				if(dbD.getDepartmentOwnerid()!=null){
					User pUser = userService.getById(dbD.getDepartmentOwnerid());
					if(pUser!=null ){
						if(!pUser.getId().equals(store.getDepartmentOwnerid())){
							Set<DepartmentNode> dps = pUser.getpNodes();
							if(dps!=null && dps.size()>0){
								Iterator<DepartmentNode> iter = dps.iterator();
								 while (iter.hasNext()) {
									 DepartmentNode dn = (DepartmentNode) iter.next();
									 if(dn.getDepartmentid().equals(store.getId())){
										 iter.remove();
									 }
								 }
							}
							userService.saveOrUpdate(user);
						}else {
							isChange=false;
						}
					}
				}
			}
		}
		
		saveOrUpdate(store);
		//为用户创建权限树
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
				//Group eGroup =PermissionUtils.getViewGroup(groupService) ;
				List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
				//赋值第一个编辑权限
				if(!egroups.isEmpty()){
					 isadd = PermissionUtils.isAdd(user.getpNodes(), egroups,store.getId(),"department");
					if(!isadd){
						DepartmentNode pn= new DepartmentNode();
						pn.setGroupid(egroups.get(0).getId());
						pn.setDepartmentid(store.getId());
						pn.setUser(user);
						pn.setType("department");
						pn.setAll(true);
						user.getpNodes().add(pn);
					}
					
					
					isadd=PermissionUtils.findGroup(user.getGroups(), egroups);
					
					if(!isadd){
						user.getGroups().add(egroups.get(0));
					}
					
					Group vgroup = PermissionUtils.getViewGroup(groupService);
					List<Group> vgroups = new ArrayList<>();
					vgroups.add(vgroup);
					isadd = PermissionUtils.isAdd(user.getpNodes(), vgroups,store.getId(),"department");
					if(!isadd){
						DepartmentNode pn= new DepartmentNode();
						pn.setGroupid(vgroup.getId());
						pn.setDepartmentid(store.getId());
						pn.setUser(user);
						pn.setType("department");
						pn.setAll(true);
						user.getpNodes().add(pn);
					}
					isadd=PermissionUtils.findGroup(user.getGroups(), vgroup);
					if(!isadd){
						user.getGroups().add(vgroup);
					}
					userService.saveOrUpdate(user);
				}
			}
		}
		
	}


	@Override
	public Department getByIDWithCsite(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return departmentDao.getMerchantStore(id);
	}


	@Override
	@Transactional
	public void deleteWithPermission(Department department) throws ServiceException {
		// TODO Auto-generated method stub
		StringBuffer nsql = new StringBuffer();
		ArrayList<String> delUserGroup = new ArrayList<>();
		//delUserGroup.add("DELETE FROM PNODE WHERE ");
		nsql.append("DELETE FROM PNODE WHERE ").append(" (DEPARTMENT_TYPE='department' AND DEPARTMENT_ID= ").append(department.getId()).append(") ");
		StringBuffer cids = new StringBuffer();
		//保留摄像头
		for(Project csite:department.getcSites()){
			cids.append(csite.getId()).append(",");
			//csite.setCamera(null);
			//保留摄像头
			List<String> where = new ArrayList<>();
			where.add("project.id");
			where.add(csite.getId().toString());
			Entitites<Camera> list  = cameraService.getPageListByAttributesLike(null, null, null, null, null,where,null);
			//ConstructionSite cs = cSiteService.getByCid(csite.getId());
			for(Camera c:list.getEntites()){
				c.setProject(null);
				cameraService.saveOrUpdate(c);
			}
				
		}
		
		
		if(cids.length()>0){
			//去掉最后一个逗号
			cids.deleteCharAt(cids.lastIndexOf(","));
			nsql.append(" OR (DEPARTMENT_TYPE='csite' AND DEPARTMENT_ID in ( ").append(cids).append(" ))");
			
		}
		delUserGroup.add(nsql.toString());
		this.excuteByNativeSql(delUserGroup);
		
		
		this.delete(department);
	}

}
