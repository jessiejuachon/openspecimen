package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.List;

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
	public List<ConfigSetting> getAllSettings(Long objectId, String accessLevel) {
		return getCurrentSession().getNamedQuery(GET_ALL)
			.setParameter("objectId", objectId)
			.setString("accessLevel", accessLevel)
			.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetting> getAllSettingsByModule(Long objectId, String accessLevel, String moduleName) {
		return getCurrentSession().getNamedQuery(GET_ALL_BY_MODULE)
			.setParameter("objectId", objectId)
			.setString("accessLevel", accessLevel)
			.setString("name", moduleName)
			.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetting> getSettingsLaterThan(Long settingId) {
		return getCurrentSession().getNamedQuery(GET_ALL_LATER_THAN)
			.setParameter("settingId", settingId)
			.list();
	}

	@Override
	public ConfigSetting getSettingByModAndProp(Long objectId, String accessLevel, String propName, String moduleName) {
		return (ConfigSetting) getCurrentSession().getNamedQuery(GET_SETTING_BY_MODULE_PROP)
			.setParameter("objectId", objectId)
			.setString("accessLevel", accessLevel)
			.setString("propName", propName)
			.setString("moduleName", moduleName)
			.uniqueResult();
	}

	private static final String FQN = ConfigSetting.class.getName();
	
	private static final String GET_ALL = FQN + ".getAll";
	
	private static final String GET_ALL_BY_MODULE = FQN + ".getAllByModule";

	private static final String GET_ALL_LATER_THAN = FQN + ".getAllLaterThan";

	private static final String GET_SETTING_BY_MODULE_PROP = FQN + ".getSettingByModuleAndProp";
}