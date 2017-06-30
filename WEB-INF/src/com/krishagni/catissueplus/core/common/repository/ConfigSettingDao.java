package com.krishagni.catissueplus.core.common.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.ConfigSetting;

public interface ConfigSettingDao extends Dao<ConfigSetting> {
	
	List<ConfigSetting> getAllSettings(String accessLevel, Long userId);
	
	List<ConfigSetting> getAllSettingsByModule(String moduleName, String accessLevel, Long userId);

	List<ConfigSetting> getSettingsLaterThan(Long settingId);

	ConfigSetting getUserSettingByModAndProp(Long userId, String moduleName, String propName);
}