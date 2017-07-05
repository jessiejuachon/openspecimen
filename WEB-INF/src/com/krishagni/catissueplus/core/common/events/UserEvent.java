package com.krishagni.catissueplus.core.common.events;

public enum UserEvent implements EventCode {
	CREATED;

	@Override
	public String code() {
		return "USER_" + name();
	}
}
