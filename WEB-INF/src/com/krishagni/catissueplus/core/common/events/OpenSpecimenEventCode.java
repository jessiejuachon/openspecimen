package com.krishagni.catissueplus.core.common.events;

public enum OpenSpecimenEventCode implements EventCode {
	
	USER_CREATED;

	@Override
	public String code() {
		return name();
	}
}
