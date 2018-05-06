package com.kekeinfo.core.utils;

import java.util.List;
import java.util.Set;

import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;

public class PermissionUtils {
	
	public static Group getViewGroup(GroupService groupService) throws ServiceException{
		Group vgroup = groupService.getByName("PROJECTVIEWGROUP");
		return vgroup;
	}
	
	public static Group getEditGroup(GroupService groupService) throws ServiceException{
		List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
		if(egroups!=null && !egroups.isEmpty()) return egroups.get(0);
		return null;
	}
	
	public static boolean findGroup(List<Group> groups,Group group){
		
		if(groups!=null && groups.size()>0){
			for(Group g:groups){
				if(g.getId().equals(group.getId())){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean findGroup(List<Group> groups,List<Group> fGroups){
		
		if(groups!=null && groups.size()>0){
			for(Group g:groups){
				for(Group f:fGroups){
					if(g.getId().equals(f.getId())){
						return true;
					}
				}
			}
		}
		return false;
	}
	public static boolean isInGroup(User user,List<Group> fGroups){
		
		List<Group> uGroups = user.getGroups();
		if(!uGroups.isEmpty()){
			for(Group ug:uGroups){
				for(Group fg:fGroups){
					if(ug.getId()==fg.getId()){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	/**
	 * 判断权限是否要加入
	 * @param dps
	 * @param fGroups
	 * @return
	 */
	public static boolean isAdd(Set<DepartmentNode> dps,List<Group> fGroups,Long depid,String type){
		if(depid!=null){
			if(dps !=null && !dps.isEmpty()){
				for(DepartmentNode dp:dps){
					if(dp.getDepartmentid()==-1 && dp.isAll()==true){
						for(Group f:fGroups){
							 if(dp.getGroupid()==f.getId()){
								return true;
							}
						}
					} else if(dp.getDepartmentid().equals(depid) && dp.getType().equalsIgnoreCase(type)){
						for(Group f:fGroups){
							 if(dp.getGroupid()==f.getId()){
								return true;
							}
						}
					}
				}
			}
		}else {
			if(dps !=null && !dps.isEmpty()){
				for(DepartmentNode dp:dps){
					if(dp.getDepartmentid()==-1 && dp.isAll()==true){
						for(Group f:fGroups){
							 if(dp.getGroupid()==f.getId()){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param dps
	 * @param fGroups
	 * @param depid
	 * @param upDepId 部门id
	 * @return
	 */
	public static boolean isAdd(Set<DepartmentNode> dps,List<Group> fGroups,Long depid,Long upDepId){
		if(dps !=null && !dps.isEmpty()){
			for(DepartmentNode dp:dps){
				for(Group f:fGroups){
					if(dp.getDepartmentid()==-1 && dp.isAll()==true){
						return true;
					}else if(dp.getGroupid()==f.getId() && dp.getDepartmentid().equals(upDepId) && dp.getType().equalsIgnoreCase("department")){
						return true;
					}
					else if(dp.getGroupid()==(f.getId()) && dp.getDepartmentid().equals(depid) && dp.getType().equalsIgnoreCase("csite")){
						return true;
					}
				}
			}
		}
		
		return false;
	}

}
