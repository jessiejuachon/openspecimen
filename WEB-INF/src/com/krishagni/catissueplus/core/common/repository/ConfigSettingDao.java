package com.krishagni.catissueplus.core.common.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.ConfigProperty;
import com.krishagni.catissueplus.core.common.domain.ConfigSetting;

public interface ConfigSettingDao extends Dao<ConfigSetting> {

	List<ConfigProperty> getAllProperties();

	List<ConfigSetting> getAllSettings(String level, Long objectId);
	
	List<ConfigSetting> getAllSettingsByModule(String moduleName, String level, Long objectId);

	List<ConfigSetting> getSettingsLaterThan(Long settingId);

	ConfigSetting getSettingByModuleAndProperty(String moduleName, String propName, String level, Long objectId);
}