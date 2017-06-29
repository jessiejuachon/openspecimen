package com.krishagni.catissueplus.core.common.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class ConfigProperty extends BaseEntity {
	public enum DataType {
		INT,
		FLOAT,
		STRING,
		BOOLEAN,
		CHAR,
		FILE
	};
	
	public enum AccessLevel {
		System,
		User
	};
	
	private Module module;
	
	private String name;
	
	private String displayNameCode;
	
	private String descCode;
	
	private Set<String> allowedValues;
	
	private DataType dataType;
	
	private Set<AccessLevel> accessLevels = new HashSet<>();
	
	private boolean secured;

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayNameCode() {
		return displayNameCode;
	}

	public void setDisplayNameCode(String displayNameCode) {
		this.displayNameCode = displayNameCode;
	}

	public String getDescCode() {
		return descCode;
	}

	public void setDescCode(String descCode) {
		this.descCode = descCode;
	}
	
	public Set<String> getAllowedValues() {
		return allowedValues;
	}

	public void setAllowedValues(Set<String> allowedValues) {
		this.allowedValues = allowedValues;
	}
	
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

   	public Set<AccessLevel> getAccessLevels() {
		return accessLevels;
	}

	public void setAccessLevels(Set<AccessLevel> accessLevels) {
		this.accessLevels = accessLevels;
	}
	
	public boolean isSecured() {
		return secured;
	}

	public void setSecured(boolean secured) {
		this.secured = secured;
	}

	public boolean isFile() {
		return getDataType() == DataType.FILE;
	}
}
