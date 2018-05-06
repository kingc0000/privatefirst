package com.kekeinfo.core.business.csite.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.constructionsite.model.CsiteCriteria;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.csite.dao.CSiteDao;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.image.service.ImageService;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.service.ProjectService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.PermissionUtils;

@Service("csiteService")
public class CSiteServiceImpl extends KekeinfoEntityServiceImpl<Long, ConstructionSite> 
		implements CSiteService {
	
	private CSiteDao cSiteDao;
	
	@Autowired DepartmentService departmentService;
	
	@Autowired UserService userService;
	
	@Autowired GroupService groupService;
	
	@Autowired DepartmentNodeService departmentNodeService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired ImageService imageService;
	
	@Autowired ProjectService projectService;
	
	
	@Autowired
	public CSiteServiceImpl(CSiteDao cSiteDao) {
		super(cSiteDao);
		this.cSiteDao = cSiteDao;
	}

	
	@Override
	public void saveOrUpdate(ConstructionSite constructionSite) throws ServiceException {
		
		if(constructionSite.getId()==null) {
			super.save(constructionSite);
		} else {
			super.update(constructionSite);
		}
		
	}
	
	@Override
	public ConstructionSite getByUserName(String username) throws ServiceException {
		return getByUserName(username);
	}
	
	@Override
	public ConstructionSite getByCid(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getByCid(cid);
	}



	@Override
	public Entitites<ConstructionSite> getByCriteria(CsiteCriteria criteria) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getByCriteria(criteria);
	}
/**
 * 
 * @param cuids
 * @param users
 * @param type
 * @throws ServiceException 
 */
	private Set<User> updateprojec(String[] cuids,Set<User> users,Project project,int type) throws ServiceException{
		boolean isChange=false;
		Set<User> cuser =new HashSet<>();
		
		List<User> decusers =new ArrayList<>();
		//找出删除的
		if(users!=null){
			for(User du:users){
				isChange=false;
				for(String ci:cuids){
					if(du.getId().equals(Long.parseLong(ci))){
						isChange=true;
						break;
					}
				}
				if(!isChange){
					decusers.add(du);
				}
			}
			//找出之前已有的不添加
			for(String ci:cuids){
				isChange=false;
				for(User du:users){
					if(du.getId().equals(Long.parseLong(ci))){
						isChange=true;
						break;
					}
				}
				if(!isChange){
					User cu = userService.getById(Long.parseLong(ci));
					if(cu!=null){
						if(type==0){
							cu.setCproject(project);
						}else{
							cu.setWproject(project);
						}
						cuser.add(cu);
					}
				}
			}
		//之前没有，全部新增	
		}else{
			for(String ci:cuids){
				User cu = userService.getById(Long.parseLong(ci));
				if(cu!=null){
					if(type==0){
						cu.setCproject(project);
					}else{
						cu.setWproject(project);
					}
					cuser.add(cu);
				}
			}
		}
		if(decusers.size()>0){
			for(User du:decusers){
				du.setCproject(null);
				userService.update(du);
			}
		}
		
		return cuser;
	}
	
	@Transactional
	@Override
	public void saveOrUpdateWithRight(ConstructionSite constructionSite, User user,String delids,String[] cuids,String[] wuids,List<DepartmentNode> pNodes) throws ServiceException {
		// TODO Auto-generated method stub
		//用户是否改变
		boolean isChange=false;
		List<Images> newimgs=constructionSite.getImages();
		//先新增imags
		if(newimgs!=null && newimgs.size()>0){
			for(Images img:newimgs){
				imageService.save(img);
				InputContentFile cmsContentFile = new InputContentFile();
				cmsContentFile.setFileName(img.getName());
				cmsContentFile.setFileContentType( FileContentType.PRODUCT_DIGITAL );
				cmsContentFile.setFile(img.getDigital() );
				contentService.addContentFile(cmsContentFile);
				if(img.getJdigital()!=null){
					InputContentFile cmsContentFilej = new InputContentFile();
					cmsContentFilej.setFileName(img.getJpeg());
					cmsContentFilej.setFileContentType( FileContentType.PRODUCT_DIGITAL );
					cmsContentFilej.setFile(img.getJdigital() );
					contentService.addContentFile(cmsContentFilej);
				}
			}
		}
		if(constructionSite.getId()==null) {
			Department de =departmentService.getMerchantStore("water");
			constructionSite.getProject().setDepartment(de);
			super.save(constructionSite);
		} else {
			
			Project project =null;
			//是否有cuids
			if(cuids !=null){
				project = projectService.withUserGroup(constructionSite.getProject().getId());
				project.setcUsers(this.updateprojec(cuids, project.getcUsers(),  project,0));
				isChange =true;
			}
			//是否有wuids
			if(wuids !=null){
				if(project==null){
					project = projectService.withUserGroup(constructionSite.getProject().getId());
				}
				project.setwUsers(this.updateprojec(wuids, project.getwUsers(),  project,1));
				isChange =true;
				
			}
			if(isChange){
				projectService.update(project);
			}
			isChange=true;
			//修改管理员的操作
			ConstructionSite dbD = this.getById(constructionSite.getId());
			if(dbD !=null){
				/**
				//重新设置projectbase的id
				if(constructionSite.getPbase()!=null && dbD.getPbase()!=null){
					constructionSite.getPbase().setId(dbD.getPbase().getId());
				}
				*/
				//设置附件的操作
				List<Images> oimgs = dbD.getImages();
				if(StringUtils.isNotBlank(delids)){
					String [] diss=delids.split(",");
					for(Images img:oimgs){
						boolean find = false;
						for(String s:diss){
							//需要删除
							if(s.equalsIgnoreCase(img.getName())){
								find=true;
								break;
							}
						}
						//没有在删除列表中
						if(!find){
							constructionSite.getImages().add(img);
						}
					}
				//没有删除列表要把之前的图片保存起来
				}else{
					for(Images img:oimgs){
						constructionSite.getImages().add(img);
					}
				}
			
				if(dbD.getProject().getProjectOwnerid()!=null){
					User pUser = userService.getById(dbD.getProject().getProjectOwnerid());
					if(pUser!=null ){
						if(!pUser.getId().equals(constructionSite.getProject().getProjectOwnerid())){
							Set<DepartmentNode> dps = pUser.getpNodes();
							if(dps!=null && dps.size()>0){
								Iterator<DepartmentNode> iter = dps.iterator();
								 while (iter.hasNext()) {
									 DepartmentNode dn = (DepartmentNode) iter.next();
									 if(dn.getDepartmentid().equals(constructionSite.getId())){
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
			
			super.update(constructionSite);
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
					isadd = PermissionUtils.isAdd(user.getpNodes(), groups, constructionSite.getProject().getDepartment().getId(), "csite");
					if(!isadd){
						isadd = PermissionUtils.isAdd(user.getpNodes(), groups,constructionSite.getId(),"csite");
					}
					 
					if(!isadd){
						DepartmentNode pn= new DepartmentNode();
						pn.setGroupid(groups.get(0).getId());
						pn.setDepartmentid(constructionSite.getId());
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
					isadd = PermissionUtils.isAdd(user.getpNodes(), vgroups,constructionSite.getId(),constructionSite.getProject().getDepartment().getId());
					if(!isadd){
						DepartmentNode pn= new DepartmentNode();
						pn.setGroupid(vgroup.getId());
						pn.setDepartmentid(constructionSite.getId());
						pn.setUser(user);
						pn.setType("csite");
						pn.setAll(true);
						user.getpNodes().add(pn);
						DepartmentNode dpn= new DepartmentNode();
						dpn.setGroupid(vgroup.getId());
						dpn.setDepartmentid(constructionSite.getProject().getDepartment().getId());
						dpn.setUser(user);
						dpn.setType("department");
						dpn.setAll(false);
						user.getpNodes().add(dpn);
					}
					
					isadd=PermissionUtils.findGroup(user.getGroups(), vgroup);
					if(!isadd){
						user.getGroups().add(vgroup);
					}
					/**
					Department de = constructionSite.getDepartment();
					
					//给部门设置权限
					Set<DepartmentNode> upn= user.getpNodes();
					if(!upn.isEmpty()){
						boolean find = false;
						for(DepartmentNode dp:upn){
							if(dp.getId()==de.getId()){
								find =true;
								break;
							}
						}
						if(!find){
							if(!vgroups.isEmpty()){
								for(Group gp:vgroups){
									if(gp.getPermissions().size()==1){
										Permission p = gp.getPermissions().iterator().next();
										if(p.getPermissionName().equalsIgnoreCase("VIEW-PROJECT")){
											user = setDeparmentRight(user, de, "department", gp);
											break;
										}
									}
								}
								//user = setDeparmentRight(user,de,"department",vgroups);
							}
						}
					}else{
						if(!vgroups.isEmpty()){
							for(Group gp:vgroups){
								if(gp.getPermissions().size()==1){
									Permission p = gp.getPermissions().iterator().next();
									if(p.getPermissionName().equalsIgnoreCase("VIEW-PROJECT")){
										user = setDeparmentRight(user, de, "department", gp);
										break;
									}
								}
							}
							//user = setDeparmentRight(user,de,"department",vgroups);
						}
					}
					
					
					List<Group> dbGroups = user.getGroups();
					boolean isfind=false;
					if(dbGroups!=null && dbGroups.size()>0){
						for(Group g:dbGroups){
							if(g.getId().equals(groups.get(0).getId())){
								isfind=true;
								break;
							}
						}
					}
					if(!isfind){
						user.getGroups().add(groups.get(0));
					}*/
					userService.saveOrUpdate(user);
				}
			}
		}
		
		//删除新的文件
		if(StringUtils.isNotBlank(delids)){
			String[] ids = delids.split(",");
			for(String s:ids){
				//对应的数据记录也要删除
				Images img = imageService.getByName(s);
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
		// TODO Auto-generated method stub
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


	@Override
	@Transactional
	public void deleteWithPermission(Long cid) throws ServiceException {
		ConstructionSite cSite = this.getById(cid);
		List<Images> imgs=cSite.getImages();
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
		for(Images s:imgs){
			//对应的数据记录也要删除
			imageService.delete(s);
			contentService.removeFile(FileContentType.PRODUCT_DIGITAL, s.getName());
		}
	}


	@Override
	public ConstructionSite getById(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getById(cid);
	}


	@Override
	public ConstructionSite getByIdWithDepartment(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getByIdWithDepartment(cid);
	}


	@Override
	public Entitites<ConstructionSite> getByCsite(ConstructionSite csite,long offset) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getByCsite(csite,offset);
	}


	@Override
	public List<ConstructionSite> getByIds(List<Long> ids) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getByIds(ids);
	}


	@Override
	public ConstructionSite getByCidWithWell(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getByCidWithWell(cid);
	}


	@Override
	public ConstructionSite getByCidWithALLWell(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getByCidWithALLWell(cid);
	}


	@Override
	public ConstructionSite getByIdWithImg(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getByIdWithImg(cid);
	}

	/**
	 * 查询项目下所有测点信息，包括测点的扩展信息
	 * @param cid
	 * @return
	 * @throws ServiceException
	 */
	public ConstructionSite getByCidForwells(long cid) throws ServiceException {
		return cSiteDao.getByCidForwells(cid);
	}


	@Override
	public List<ConstructionSite> withZone() throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.withZone();
	}


	@Override
	public List<ConstructionSite> withDepartment() throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.withDepartment();
	}


	@Override
	public List<ConstructionSite> shengtong(Date date) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.shengtong(date);
	}


	@Override
	public List<ConstructionSite> getByPid(Long pid) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getByPid(pid);
	}


	@Override
	public ConstructionSite withProjectGroup(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.withProjectGroup(cid);
	}


	@Override
	@Transactional
	public void saveQcode(ConstructionSite csite) throws ServiceException {
		InputContentFile cmsContentFile = new InputContentFile();
		cmsContentFile.setFileName(csite.getqCode());
		cmsContentFile.setFileContentType( FileContentType.QCODE );
		cmsContentFile.setFile(csite.getDigital() );
		contentService.addContentFile(cmsContentFile);
		this.update(csite);
	}


	@Override
	public ConstructionSite getBypId(long cid) throws ServiceException {
		// TODO Auto-generated method stub
		return cSiteDao.getBypId(cid);
	}
}
