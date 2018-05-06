package com.kekeinfo.core.business.common.model;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Description implements Serializable {
	private static final long serialVersionUID = -4335863941736710046L;
	
	@NotEmpty
	@Column(name="NAME", nullable = false, length=1000)
	private String name;
	
	@Column(name="TITLE", length=100)
	private String title;
	
	@Column(name="DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;
	
	
	@Column(name="EN_NAME", length=120)
	private String enName;
	
	@Column(name="EN_TITLE", length=100)
	private String enTitle;
	
	@Column(name="EN_DESCRIPTION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String enDescription;
	//国际化用法
	@Transient
	private Locale locale;
	
	public Description() {
	}
	
	public Description(String name) {		
		this.setName(name);
	}
	
	public String getName() {
		if(locale!=null && locale.getLanguage().equalsIgnoreCase("en")){
			return enName;
		}else{
			return name;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		if(locale!=null && locale.getLanguage().equalsIgnoreCase("en")){
			return enTitle;
		}else{
			return title;
		}
		
		
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		if(locale!=null && locale.getLanguage().equalsIgnoreCase("en")){
			return enDescription;
		}else{
			return description;
		}
		
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getEnTitle() {
		return enTitle;
	}

	public void setEnTitle(String enTitle) {
		this.enTitle = enTitle;
	}

	public String getEnDescription() {
		return enDescription;
	}

	public void setEnDescription(String enDescription) {
		this.enDescription = enDescription;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}	
}
