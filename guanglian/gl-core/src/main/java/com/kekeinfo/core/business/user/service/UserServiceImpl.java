package com.kekeinfo.core.business.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.common.model.Criteria;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.user.dao.UserDao;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.utils.PermissionUtils;

@Service("userService")
public class UserServiceImpl extends KekeinfoEntityServiceImpl<Long, User>
		implements UserService {


	private UserDao userDao;
	
	
	@Autowired DepartmentService departmentService;
	
	@Autowired DepartmentNodeService departmentNodeService;
	
	@Autowired GroupService groupService;
	
	@Autowired
	public UserServiceImpl(UserDao userDao) {
		super(userDao);
		this.userDao = userDao;

	}
	
	@Override
	public User getByUserName(String userName) throws ServiceException {
		
		return userDao.getByUserName(userName);
		
	}
	
	@Override
	public void delete(User user) throws ServiceException {
		
		User u = this.getById(user.getId());
		super.delete(u);
		
	}

	@Override
	public List<User> listUser() throws ServiceException {
		try {
			return userDao.listUser();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@Transactional
	public void saveOrUpdate(User user,Set<DepartmentNode> delnode) throws ServiceException {
		if(user.getId()==null || user.getId().longValue()==0) {
			this.save(user);
		} else {
			this.update(user);
		}
		/**
		if(delnode!=null){
			for(DepartmentNode dn:delnode){
				departmentNodeService.delete(dn);
			}
		}*/
	}
	
	@Override
	public User getByUserId(long id) {
		return userDao.getById(id);
	}

	@Override
	public List<Object[]> getPinYin(String where) throws ServiceException {
		// TODO Auto-generated method stub
		return userDao.getPinYin(where);
	}


	@Override
	@Transactional
	public void deleteUser(User user) throws ServiceException {
		//先清除order的外键
		/**
		StringBuffer remove_where = new StringBuffer("").append(" where USER_ID=").append(user.getId()).append(" ");
		List<String> remove_attr = new ArrayList<String>();
		remove_attr.add(" SET USER_ID=null ");
		this.updateBySql(remove_attr, " ORDERS", remove_where.toString());
		/**OrderCriteria oc = new OrderCriteria();
		oc.setLogonid(user.getId());
		Entitites<Order> orders= orderService.getByCriteria(oc);
		if(orders!=null && orders.getEntites().size()>0){
			for(Order o:orders.getEntites()){
				o.setUser(null);
				orderService.saveOrUpdate(o);
			}
		}*/
		//清除客户外键
		/*StoreCriteria sc = new StoreCriteria();
		sc.setUserid(user.getId());
		Entitites<MerchantStore> stores= merchantStoreService.getByCriteria(sc);
		if(stores!=null && stores.getEntites().size()>0){
			for(MerchantStore m:stores.getEntites()){
				m.setUser(null);
				merchantStoreService.saveOrUpdate(m);
			}
		}
		*/
		//remove_where = new StringBuffer("").append(" where USER_ID=").append(user.getId()).append(" ");
		//remove_attr = new ArrayList<String>();
		//remove_attr.add(" SET USER_ID=null ");
		//this.updateBySql(remove_attr, " MERCHANT_STORE", remove_where.toString());
		this.delete(user);
	}
	
	@Override
	public Entitites<User> getByCriteria(Criteria criteria) throws ServiceException{
		return this.userDao.getByCriteria(criteria);
	}

	@Override
	public User getWithdepartmentNode(Long id,Integer gid) throws ServiceException {
		// TODO Auto-generated method stub
		return this.userDao.getWithdepartmentNode(id,gid);
	}

	@Transactional
	@Override
	public void saveOrUpdateWithRight(User user) throws ServiceException {
		// TODO Auto-generated method stub
		//保存pnode
		if(user.getpNodes()!=null && user.getpNodes().size()>0){
			User dbUser = null;
			if(user.getId()!=null && user.getId()>0){
				dbUser = userDao.getById(user.getId());
			}
//					自动增加编辑权限
			dbUser.getpNodes().clear();
			user.getpNodes().addAll(dbUser.getpNodes());
			user=this.addview(user,dbUser);
		}
		if(user.getId()==null || user.getId().longValue()==0) {
			userDao.save(user);
		} else {
			userDao.update(user);
		}
	}
	/**
	 * 编辑权限的要自动加上查看权限,半选状态要改成查看权限
	 * @param dpns
	 * @return
	 * @throws ServiceException 
	 */
	private User addview(User user,User dbUser) throws ServiceException{
		//Group egroup = null;
		Group vgroup = PermissionUtils.getViewGroup(groupService);
		//List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT");
		//有查看所有的权限
		boolean hasAll=false;
		//编辑中有所有的权限
		boolean editAll=false;
		
		if( vgroup!=null){
			Set<DepartmentNode> ndps = new HashSet<>();
			//查找以前的编辑项
			if(dbUser!=null){
				boolean hasView =false;
				boolean hasEdit =false;
				for(DepartmentNode dp:user.getpNodes()){
					if(dp.getGroupid()==vgroup.getId()){
						hasView=true;
						break;
					}
				}
				for(DepartmentNode dp:user.getpNodes()){
					if(dp.getGroupid()!=vgroup.getId()){
						hasEdit=true;
						break;
					}

				}
				//原来的数据库加入编辑的选项
				if(!hasEdit){
					for(DepartmentNode dp:dbUser.getpNodes()){
						if(dp.getGroupid()!=vgroup.getId() && dp.getDepartmentid()!=-2){
							dp.setUser(user);
							ndps = this.add(ndps, dp);;
						}
					}
				}else{
					//查找新增项是不是有编辑所有
					for(DepartmentNode dp:user.getpNodes()){
						if(dp.getDepartmentid()==-1 && dp.isAll()==true && dp.getGroupid()!=vgroup.getId()){
							editAll=true;
							break;
						}
					}
				}
					
				//原来的数据库加入查看的选项
				if(!hasView && !editAll){
					for(DepartmentNode dp:dbUser.getpNodes()){
						if(dp.getGroupid()==vgroup.getId()){
							dp.setUser(user);
							ndps = this.add(ndps, dp);
							if(dp.getDepartmentid()==-1 && dp.isAll()==true){
								hasAll=true;
							}
						}
					}
				}else{
					for(DepartmentNode dp:user.getpNodes()){
						if(dp.getGroupid()==vgroup.getId() && dp.getDepartmentid()!=-2){
							dp.setUser(user);
							ndps = this.add(ndps, dp);
							if(dp.getDepartmentid()==-1 && dp.isAll()==true){
								hasAll=true;
							}
						}
					}
				}
				
			}
			
			//是否需要加入编辑组
			boolean hasAddGroup =false;
			
			for(DepartmentNode dp:user.getpNodes()){
				if(dp.getDepartmentid()!=-2){
					//是编辑权限
					if(dp.getGroupid()!=vgroup.getId()){
						//半选状态,编辑变查看
						if(dp.isAll()==false){
							dp.setGroupid(vgroup.getId());
						//新增编辑权限
						}else if(!hasAll){
							
							//比较ndps是否已经有了该权限
							boolean hasView =false;
							for(DepartmentNode dnp:ndps){
								if(dnp.getGroupid()==vgroup.getId() && dnp.getDepartmentid()==dp.getDepartmentid() && dnp.getType().equalsIgnoreCase(dp.getType())){
									hasView=true;
								}
							}
							if(!hasView){
								DepartmentNode nd = new DepartmentNode();
								nd.setDepartmentid(dp.getDepartmentid());
								nd.setGroupid(vgroup.getId());
								nd.setType(dp.getType());
								nd.setUser(user);
								nd.setAll(dp.isAll());
								ndps = this.add(ndps, nd);;
								hasAddGroup=true;
							}
							
						}
					}
					
					dp.setUser(user);
					ndps = this.add(ndps, dp);
				}
			}
			//return ndps;
			if(hasAddGroup){
				user.getGroups().add(vgroup);
			}
			user.setpNodes(ndps);
		}
		return user;
	}
	
	//去掉重复的项
	private Set<DepartmentNode> add(Set<DepartmentNode> ndps,DepartmentNode np){
		if(ndps.isEmpty()) ndps = new HashSet<>();
		boolean isFind =false;
		for(DepartmentNode dp :ndps){
			if(dp.getDepartmentid().equals(np.getDepartmentid()) && dp.getGroupid()==np.getGroupid() && dp.getType().equalsIgnoreCase(np.getType()) && np.isAll()==dp.isAll()){
				isFind=true;
				break;
			}
		}
		if(!isFind){
			ndps.add(np);
		}
		return ndps;
	}

	@Override
	public List<User> getByPermission(List<String> ps) throws ServiceException {
		// TODO Auto-generated method stub
		return userDao.getByPermission(ps);
	}

	@Override
	public User getName(String name) throws ServiceException {
		// TODO Auto-generated method stub
		return userDao.getName(name);
	}

	@Override
	public List<Object[]> getPinYinUser() throws ServiceException {
		// TODO Auto-generated method stub
		return userDao.getPinYinUser();
	}
}
