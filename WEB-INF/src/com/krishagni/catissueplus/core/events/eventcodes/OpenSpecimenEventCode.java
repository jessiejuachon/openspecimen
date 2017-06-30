package com.krishagni.catissueplus.core.events.eventcodes;

public enum OpenSpecimenEventCode implements EventCode {
	
	USER_SIGNUP;
	
	@Override
	public String code() {
		return this.name();
	}
}
