package com.kekeinfo.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2127431825841368054L;
	private String groupName;
	List<User> users = new ArrayList<User>();
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

}
