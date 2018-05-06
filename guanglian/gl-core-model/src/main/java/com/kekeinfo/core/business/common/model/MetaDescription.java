package com.kekeinfo.core.business.common.model;

import java.io.Serializable;

import javax.persistence.Column;


public class MetaDescription extends Description implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1252756716545768599L;

	
	@Column(name="SEF_URL", length=120)
	private String seUrl;
	
	@Column(name="META_KEYWORDS")
	private String metatagKeywords;
	
	@Column(name="META_TITLE")
	private String metatagTitle;
	
	public String getMetatagTitle() {
		return metatagTitle;
	}
	
	@Column(name = "HIGHLIGHT")
	private String highlight;

	public void setMetatagTitle(String metatagTitle) {
		this.metatagTitle = metatagTitle;
	}

	@Column(name="META_DESCRIPTION")
	private String metatagDescription;
	
	public MetaDescription() {
	}
	
	public MetaDescription(String name) {
		this.setName(name);	
		
	}

	public String getSeUrl() {
		return seUrl;
	}

	public void setSeUrl(String seUrl) {
		this.seUrl = seUrl;
	}


	public String getMetatagKeywords() {
		return metatagKeywords;
	}

	public void setMetatagKeywords(String metatagKeywords) {
		this.metatagKeywords = metatagKeywords;
	}

	public String getMetatagDescription() {
		return metatagDescription;
	}

	public void setMetatagDescription(String metatagDescription) {
		this.metatagDescription = metatagDescription;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
	
	/**
	 * 通过对象名设定对应的Meta属性，例如key，title，description
	 * @param name
	 */
	public void setMetaTagsByName(String name) {
		if (name == null || name.trim().equals("")) {
			name = getName();
		}
		this.metatagTitle = name;
		this.metatagKeywords = name;
		this.metatagDescription = name;
	}

}
