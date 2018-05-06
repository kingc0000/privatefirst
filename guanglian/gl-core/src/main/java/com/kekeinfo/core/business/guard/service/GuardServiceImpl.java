package com.kekeinfo.core.business.guard.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.attach.service.AttachService;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.service.ProjectService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.PermissionUtils;
import com.kekeinfo.core.business.guard.dao.GuardDao;

@Service("guardService")
public class GuardServiceImpl extends KekeinfoEntityServiceImpl<Long, Guard> implements GuardService {
	private GuardDao guardDao;
	
	@Autowired UserService userService;
	@Autowired GroupService groupService;
	@Autowired DepartmentService departmentService;
	@Autowired DepartmentNodeService departmentNodeService;
	@Autowired private ContentService contentService;
	@Autowired AttachService imageService;
	@Autowired ProjectService projectService;

	@Autowired
	public GuardServiceImpl(GuardDao guardDao) {
		super(guardDao);
		this.guardDao = guardDao;
	}

	@Transactional
	@Override
	public void saveOrUpdate(Guard monitor, User user, String delids,List<DepartmentNode> pNodes) throws ServiceException {
		//用户是否改变
		boolean isChange=true;
		List<Attach> newimgs=monitor.getImages();
		//先新增imags
		if(newimgs!=null && newimgs.size()>0){
			for(Attach img:newimgs){
				InputStream inputStream = img.getDigital();
		        InputContentFile cmsContentImage = new InputContentFile();
		        cmsContentImage.setFileName( img.getFileName());
		        cmsContentImage.setFile( inputStream );
		        cmsContentImage.setFileContentType(FileContentType.PRODUCT_DIGITAL);
		        contentService.addContentFile(cmsContentImage);
		        imageService.save(img);
				
				
			}
		}
		if(monitor.getId()==null) {
			//先保存项目
			if(monitor.getProject().getId()==null){
				projectService.save(monitor.getProject());
			}
			super.save(monitor);
		} else {
			//修改管理员的操作
			Guard dbD = this.getById(monitor.getId());
			if(dbD !=null){
				//设置附件的操作
				List<Attach> oimgs = dbD.getImages();
				if(StringUtils.isNotBlank(delids)){
					String [] diss=delids.split(",");
					for(Attach img:oimgs){
						boolean find = false;
						for(String s:diss){
							//需要删除
							if(img.getId().equals(Long.parseLong(s))){
								find=true;
								break;
							}
						}
						//没有在删除列表中
						if(!find){
							monitor.getImages().add(img);
						}
					}
				//没有删除列表要把之前的图片保存起来
				}else{
					for(Attach img:oimgs){
						monitor.getImages().add(img);
					}
				}
			
				if(dbD.getProject().getProjectOwnerid()!=null){
					User pUser = userService.getById(dbD.getProject().getProjectOwnerid());
					if(pUser!=null ){
						if(!pUser.getId().equals(monitor.getProject().getProjectOwnerid())){
							Set<DepartmentNode> dps = pUser.getpNodes();
							if(dps!=null && dps.size()>0){
								Iterator<DepartmentNode> iter = dps.iterator();
								 while (iter.hasNext()) {
									 DepartmentNode dn = (DepartmentNode) iter.next();
									 if(dn.getDepartmentid().equals(monitor.getId())){
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
			//保存项目信息
			//要获取部门信息
			Project pr =projectService.getById(monitor.getProject().getId());
			monitor.getProject().setDepartment(pr.getDepartment());
			projectService.update(monitor.getProject());
			super.update(monitor);
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
					isadd = PermissionUtils.isAdd(user.getpNodes(), groups, monitor.getProject().getDepartment().getId(), "csite");
					if(!isadd){
						isadd = PermissionUtils.isAdd(user.getpNodes(), groups,monitor.getId(),"csite");
					}
					 
					if(!isadd){
						DepartmentNode pn= new DepartmentNode();
						pn.setGroupid(groups.get(0).getId());
						pn.setDepartmentid(monitor.getId());
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
					isadd = PermissionUtils.isAdd(user.getpNodes(), vgroups,monitor.getId(),monitor.getProject().getDepartment().getId());
					if(!isadd){
						DepartmentNode pn= new DepartmentNode();
						pn.setGroupid(vgroup.getId());
						pn.setDepartmentid(monitor.getId());
						pn.setUser(user);
						pn.setType("csite");
						pn.setAll(true);
						user.getpNodes().add(pn);
						DepartmentNode dpn= new DepartmentNode();
						dpn.setGroupid(vgroup.getId());
						dpn.setDepartmentid(monitor.getProject().getDepartment().getId());
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
		
		//删除新的文件
		if(StringUtils.isNotBlank(delids)){
			String[] ids = delids.split(",");
			for(String s:ids){
				//对应的数据记录也要删除
				Attach img = imageService.getById(Long.parseLong(s));
				imageService.delete(img);
				contentService.removeFile(FileContentType.PRODUCT_DIGITAL, s);
			}
		}
		//如果项目已经结束删除相应的权限
		if(pNodes!=null && pNodes.size()>0){
			for(DepartmentNode p:pNodes){
				departmentNodeService.delete(p);
			}
		}
	}

	@Transactional
	@Override
	public void deleteByCid(Long cid) throws ServiceException {
		super.deleteById(cid);
		List<DepartmentNode> dns = departmentNodeService.getByDepartmentAndType("department", cid);
		if(!dns.isEmpty()){
			for(DepartmentNode dn:dns){
				User user = dn.getUser();
				user.getpNodes().remove(dn);
				userService.saveOrUpdate(user);
			}
		}
		
	}

	@Transactional
	@Override
	public void deleteWithPermission(Long cid) throws ServiceException {
		Guard cSite = this.getById(cid);
		List<Attach> imgs=cSite.getImages();
		StringBuffer nsql = new StringBuffer();
		ArrayList<String> delUserGroup = new ArrayList<>();
		nsql.append("DELETE FROM PNODE WHERE ").append(" (DEPARTMENT_TYPE='csite' AND DEPARTMENT_ID= ").append(cSite.getId()).append(") ");
		
		delUserGroup.add(nsql.toString());
		this.excuteByNativeSql(delUserGroup);
		/**
		for(Camera c:cSite.getCamera()){
			c.setcSite(null);
			cameraService.saveOrUpdate(c);
		}*/
		this.delete(cSite);
		for(Attach s:imgs){
			//对应的数据记录也要删除
			imageService.delete(s);
			contentService.removeFile(FileContentType.PRODUCT_DIGITAL, s.getFileName());
		}
		
	}

	@Override
	public Guard getByIdWithImg(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.guardDao.getByIdWithImg(cid);
	}

	@Override
	public Guard getByCid(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.guardDao.getByCid(cid);
	}

	@Override
	public List<Guard> withProject() throws ServiceException {
		// TODO Auto-generated method stub
		return this.guardDao.withProject();
	}

	@Override
	public List<Guard> getByPid(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return guardDao.getByPid(cid);
	}
}
