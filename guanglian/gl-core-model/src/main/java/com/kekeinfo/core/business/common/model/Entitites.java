package com.kekeinfo.core.business.common.model;

import java.util.List;

public class Entitites <T> extends EntityList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 663000915173151360L;
	
	private List<T> entites;

	public List<T> getEntites() {
		return entites;
	}

	public void setEntites(List<T> entites) {
		this.entites = entites;
	}

}
