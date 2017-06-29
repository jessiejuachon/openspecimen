package com.krishagni.catissueplus.core.events.eventcodes;

public enum OpenSpecimenEventCode implements EventCode {
	
	USER_SIGNUP,
	
	OTP_CHECK;

	@Override
	public String code() {
		return this.name();
	}
}
