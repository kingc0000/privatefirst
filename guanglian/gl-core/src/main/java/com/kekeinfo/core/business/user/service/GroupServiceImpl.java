package com.kekeinfo.core.business.user.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.user.dao.GroupDao;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.Permission;

@Service("groupService")
public class GroupServiceImpl extends
		KekeinfoEntityServiceImpl<Integer, Group> implements GroupService {

	GroupDao groupDao;
	
	@Autowired PermissionService permissionService;

	@Autowired
	public GroupServiceImpl(GroupDao groupDao) {
		super(groupDao);
		this.groupDao = groupDao;

	}
	
	public List<Group> listGroupByIds(Set<Integer> ids) throws ServiceException {
		try {
			return groupDao.listGroupByIds(ids);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}



	@Override
	public Group getByName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		return groupDao.getByName(name);
	}

	@Override
	@Transactional
	public void saveOrUpdate(Group group) throws ServiceException{
		
		if(group.getPermissions()!=null){
			Set<Permission> gp = group.getPermissions();
			Set<Permission> dbPs = new HashSet<Permission>();
			//从前台获取所有的permission
			for(Permission p :gp){
				int pid=-1;
				try{
					pid=Integer.parseInt(p.getPermissionName());
				}catch (Exception e){
					pid=p.getId().intValue();
				}
				Permission pp = permissionService.getById(pid);
				dbPs.add(pp);
			}
			if(group.getId() !=null){
				//判断权限是否减少了，如果是去掉了查看权限，相应的编辑权限也应该去掉
				Group dbGroup = this.getById(group.getId());
				if(dbGroup!=null && dbGroup.getPermissions()!=null && dbGroup.getPermissions().size()>0){
					gp = removeEdit(dbPs,dbGroup.getPermissions());
				}
				
			}
			boolean gtype=false;
			Set<Permission> ps = new HashSet<Permission>();
			for(Permission p :dbPs){
				//存在eidt，view权限设置gruop的type
				if(p.getPermissionName().indexOf("-PROJECT") !=-1){
					if(!group.getGroupName().equalsIgnoreCase("SUPERADMIN") && !group.getGroupName().equalsIgnoreCase("ADMIN")){
						gtype=true;
					}
					//存在edit权限加上view权限,存在eidt
					if(p.getPermissionName().startsWith("EDIT-")){
						Permission ppp = permissionService.getByName("VIEW-"+p.getPermissionName().substring(5));
						if(ppp!=null && this.isadd(ps, ppp)){
							ps.add(ppp);
						}
					}
				}
				if(this.isadd(ps, p)){
					ps.add(p);
				}
			}
			if(gtype){
				group.setGrouptype(1);
			}else{
				group.setGrouptype(0);
			}
			//加上基本权限
			Permission au = permissionService.getByName("AUTH");
			if(this.isadd(ps, au)){
				ps.add(au);
			}
			group.setPermissions(ps);
		}
		
		if(group.getId()==null){
			this.save(group);
		}else{
			this.update(group);
		}
	}

	private Set<Permission> removeEdit(Set<Permission> ps ,Set<Permission> dbs){
		List<Permission> temp = new ArrayList<Permission>();
		for(Permission p:dbs){
			boolean find=false;
			for(Permission pp:ps){
				if(pp.getId().intValue()==p.getId().intValue()){
					find=true;
					break;
				}
			}
			if(find==false){
				temp.add(p);
			}
		}
		//所有删除的,有view权限的
		if(temp.size()>0){
			for(Permission p:temp){
				if(p.getPermissionName().startsWith("VIEW-")){
					//查找ps中是否有EDIT,有则删除
					for(Permission pp:ps){
						if(pp.getPermissionName().startsWith("EDIT-") && pp.getPermissionName().equalsIgnoreCase("EDIT-"+pp.getPermissionName().substring(5))){
							ps.remove(pp);
							break;
						}
					}
				}
			}
		}
		return ps;
	}

	private boolean isadd(Set<Permission> ps,Permission p){
		for(Permission pp:ps){
			if(pp.getId().intValue()==p.getId().intValue()){
				return false;
			}
		}
		return true;
	}


	@Override
	@Transactional
	public void deleteById(Integer id) throws ServiceException {
		// TODO Auto-generated method stub
		Group group = this.getById(id);
		ArrayList<String> delUserGroup = new ArrayList<>();
		delUserGroup.add("DELETE FROM USER_GROUP WHERE GROUP_ID = "+id+" ");
		this.excuteByNativeSql(delUserGroup);
		group.setPermissions(null);
		this.delete(group);
	}

	@Override
	public List<Group> listGroupWithPermission() throws ServiceException {
		// TODO Auto-generated method stub
		return this.groupDao.listGroupWithPermission();
	}

	/**
	 * 根据权限组名称和权限组类型，获取对应的权限组集合
	 * @param type 0:一般权限组，1：部门项目权限组，对应PNODE表关联
	 */
	@Override
	public List<Group> listGroupByPermissionName(String name,int type) throws ServiceException {
		// TODO Auto-generated method stub
		return this.groupDao.listGroupByPermissionName(name,type);
	}

}
