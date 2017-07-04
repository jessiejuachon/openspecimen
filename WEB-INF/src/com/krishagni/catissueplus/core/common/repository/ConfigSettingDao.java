package com.krishagni.catissueplus.core.common.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.ConfigSetting;

public interface ConfigSettingDao extends Dao<ConfigSetting> {
	
	List<ConfigSetting> getAllSettings(Long objectId, String accessLevel);
	
	List<ConfigSetting> getAllSettingsByModule(Long objectId, String accessLevel, String moduleName);

	List<ConfigSetting> getSettingsLaterThan(Long settingId);

	ConfigSetting getSettingByModAndProp(Long userId, String accessLevel, String propName, String moduleName);
}