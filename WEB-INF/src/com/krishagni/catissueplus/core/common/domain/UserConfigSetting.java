package com.krishagni.catissueplus.core.common.domain;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class UserConfigSetting extends ConfigSetting {
	private User configUser;

	public User getConfigUser() {
		return configUser;
	}

	public void setConfigUser(User configUser) {
		this.configUser = configUser;
	}
}