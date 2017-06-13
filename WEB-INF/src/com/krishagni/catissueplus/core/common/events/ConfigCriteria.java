package com.krishagni.catissueplus.core.common.events;

public class ConfigCriteria {
	private String module;
	
	private String property;
	
	private String propertyType;
	
	public ConfigCriteria module(String module){
		this.module = module;
		return this;
	}
	
	public ConfigCriteria property(String property ){
		this.property = property ;
		return this;
	}
	
	public ConfigCriteria propertyType(String propertyType){
		this.propertyType = propertyType ;
		return this;
	}

	public String module() {
		return module;
	}

	public String property() {
		return property;
	}

	public String propertyType() {
		return propertyType;
	}
 }