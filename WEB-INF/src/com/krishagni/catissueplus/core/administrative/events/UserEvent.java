package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventCode;

public enum UserEvent implements EventCode {
	CREATED;

	@Override
	public String code() {
		return "USER_" + name();
	}
}
