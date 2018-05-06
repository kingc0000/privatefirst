package com.kekeinfo.web.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.Permission;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.PermissionService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.web.constants.Constants;


/**
 * 
 * @author casams1
 *         http://stackoverflow.com/questions/5105776/spring-security-with
 *         -custom-user-details
 */
@SuppressWarnings("deprecation")
@Service("userDetailsService")
public class UserServicesImpl implements WebUserServices{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServicesImpl.class);

	@Autowired
	private UserService userService;
	
	//@Autowired private PNodeUtils pNodeUtils;
	@Autowired private SessionRegistry sessionRegistry;  
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	
	@Autowired
	protected PermissionService  permissionService;
	
	@Autowired
	protected GroupService   groupService;
	
	
	
	
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {

		com.kekeinfo.core.business.user.model.User user = null;
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String strtoken="";
		String userAgent="";
		try {
			if(userName.indexOf(",")!=-1){
				String[] str =userName.split(",");
				userName=str[0];
				if(str.length>1){
					strtoken=str[1];
					userAgent=str[2];
				}
			}
			user = userService.getName(userName);

			if(user==null) {
				return null;
			}else{
				//用户是否切换了手机，切换后要重新设置闹钟
				boolean ischang=false;
				//设置token
				if(StringUtils.isNotBlank(userAgent)){
					
					if(strtoken!=null &&StringUtils.isNotBlank(strtoken) ){
						if(user.getDevice_token()==null || StringUtils.isBlank(user.getDevice_token())){
							user.setDevice_token(strtoken);
							ischang=true;
						}else if( !user.getDevice_token().equalsIgnoreCase(strtoken)){
							user.setDevice_token(strtoken);
							ischang=true;
						}
						
					//	userService.saveOrUpdate(user);
					}else if(!user.getAtype().equalsIgnoreCase(userAgent)){
						ischang=true;
					}
					
					user.setuAgent(userAgent);
					user.setAtype(userAgent);
				}
				else{
					user.setuAgent(null);
					ischang=false;
				}
				Date lastAccess = user.getLoginTime();
				  if(lastAccess==null) {
					  lastAccess = new Date();
				  }
				  user.setLastAccess(lastAccess);
				  user.setLoginTime(new Date());
				//重新设置用户的工作安排
				  if(ischang){
					  user.setTemp("change");
				  }
				  userService.saveOrUpdate(user);
				  
			}

			GrantedAuthority role = new GrantedAuthorityImpl(Constants.PERMISSION_AUTHENTICATED);//required to login
			authorities.add(role);
	
			List<Integer> groupsId = new ArrayList<Integer>();
			List<Group> groups = user.getGroups();
			for(Group group : groups) {
				
				
				groupsId.add(group.getId());
				
			}
			
	
	    	if(groupsId!=null && groupsId.size()>0){
	    		List<Permission> permissions = permissionService.getPermissions(groupsId);
		    	for(Permission permission : permissions) {
		    		GrantedAuthority auth = new GrantedAuthorityImpl(permission.getPermissionName());
		    		authorities.add(auth);
		    	}
	    	}
	    	
    	
		} catch (Exception e) {
			LOGGER.error("Exception while querrying user",e);
			throw new SecurityDataAccessException("Exception while querrying user",e);
		}
		
		
		
	
		
		User secUser = new User(userName, user.getAdminPassword(), user.isActive(), true,
				true, true, authorities);
		return secUser;
	}
	
	
	public void createDefaultAdmin() throws Exception {
		
		  //TODO create all groups and permissions
		
		  //MerchantStore store = merchantStoreService.getMerchantStore("DEFAULT");

		  String password = passwordEncoder.encodePassword("password", null);
		  
		  List<Group> groups = groupService.list();
		  
		  //creation of the super admin admin:password)
		  com.kekeinfo.core.business.user.model.User user = new com.kekeinfo.core.business.user.model.User("admin",password,"admin@shopizer.com");
		  user.setTelephone("88888888");
		  user.setFirstName("Administrator");
		  
		  for(Group group : groups) {
			  if(group.getGroupName().equals(Constants.GROUP_SUPERADMIN) || group.getGroupName().equals(Constants.GROUP_ADMIN)) {
				  user.getGroups().add(group);
			  }
		  }

		  //user.setMerchantStore(store);		  
		  userService.create(user);
		
		
	}


	@Override
	public void removeSession(com.kekeinfo.core.business.user.model.User user) {
		// TODO Auto-generated method stub
		//sessionRegistry=this.getSessionRegistry();
		for (Object userDetail : sessionRegistry.getAllPrincipals()) {  
            String userName = ((org.springframework.security.core.userdetails.User) userDetail).getUsername();  
            if (userName.equalsIgnoreCase(user.getAdminName())) {  
                this.removeSession(userDetail);  
                break;
            }  
        }  

	}

    private void removeSession(Object principal) {
        List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(principal, false);  
        for (SessionInformation sessionInformation : sessionInformations) {  
            sessionInformation.expireNow();  
        }  
    }

}
