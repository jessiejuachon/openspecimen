package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.common.domain.ConfigProperty;
import com.krishagni.catissueplus.core.common.domain.ConfigSetting;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.ConfigSettingDao;

public class ConfigSettingDaoImpl extends AbstractDao<ConfigSetting> implements ConfigSettingDao {

	@Override
	public Class<ConfigSetting> getType() {
		return ConfigSetting.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigProperty> getAllProperties() {
		return getCurrentSession().getNamedQuery(GET_ALL_PROPS).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetting> getAllSettings(String level, Long objectId) {
		return getAllSettingsByModule(null, level, objectId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetting> getAllSettingsByModule(String moduleName, String level, Long objectId) {
		if (level == null) {
			return Collections.emptyList();
		}

		List<Object[]> propSettings = getCurrentSession().getNamedQuery(GET_ALL)
			.setParameter("level", level)
			.setParameter("objectId", objectId)
			.setParameter("moduleName", moduleName)
			.list();

		List<ConfigSetting> result = new ArrayList<>();
		for (Object[] propSetting : propSettings) {
			ConfigProperty prop = (ConfigProperty) propSetting[0];
			ConfigSetting setting = (ConfigSetting) propSetting[1];

			if (setting == null) {
				setting = new ConfigSetting();
				setting.setProperty(prop);
				setting.setLevel(ConfigProperty.Level.valueOf(level));
				setting.setObjectId(objectId);
				setting.setValue(null);
				setting.setActivityStatus("Active");
			}

			result.add(setting);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetting> getSettingsLaterThan(Long settingId) {
		//
		// TODO: Find where is this API used. If not used, remove it
		// TODO: Check in plugins as well
		//
		return getCurrentSession().getNamedQuery(GET_ALL_LATER_THAN)
			.setParameter("settingId", settingId)
			.list();
	}

	@Override
	public ConfigSetting getSettingByModuleAndProperty(String moduleName, String propName, String level, Long objectId) {
		return (ConfigSetting) getCurrentSession().getNamedQuery(GET_SETTING_BY_MODULE_PROP)
			.setParameter("moduleName", moduleName)
			.setParameter("propName", propName)
			.setParameter("level", level)
			.setParameter("objectId", objectId)
			.uniqueResult();
	}

	private static final String FQN = ConfigSetting.class.getName();
	
	private static final String GET_ALL = FQN + ".getAll";
	
	private static final String GET_ALL_LATER_THAN = FQN + ".getAllLaterThan";

	private static final String GET_SETTING_BY_MODULE_PROP = FQN + ".getSettingByModuleAndProp";

	private static final String PROP_FQN = ConfigProperty.class.getName();

	private static final String GET_ALL_PROPS = PROP_FQN + ".getAll";
}
