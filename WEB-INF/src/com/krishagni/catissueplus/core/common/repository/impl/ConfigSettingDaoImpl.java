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
	public List<ConfigSetting> getAllSettings(String accessLevel, Long userId) {
		return getCurrentSession().getNamedQuery(GET_ALL)
			.setString("accessLevel", accessLevel)
			.setParameter("userId", userId )
			.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetting> getAllSettingsByModule(String moduleName, String accessLevel, Long userId) {
		return getCurrentSession().getNamedQuery(GET_ALL_BY_MODULE)
			.setString("name", moduleName)
			.setString("accessLevel", accessLevel)
			.setParameter("userId", userId)
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
	public ConfigSetting getUserSettingByModAndProp(Long userId, String moduleName, String propName) {
		return (ConfigSetting) getCurrentSession().getNamedQuery(GET_USER_SETT_BY_MODULE_PROP)
			.setParameter("userId",userId)
			.setString("propName", propName)
			.setString("moduleName", moduleName)
			.uniqueResult();
	}
	
	private static final String FQN = ConfigSetting.class.getName();
	
	private static final String GET_ALL = FQN + ".getAll";
	
	private static final String GET_ALL_BY_MODULE = FQN + ".getAllByModule";

	private static final String GET_ALL_LATER_THAN = FQN + ".getAllLaterThan";
	
	private static final String GET_USER_SETT_BY_MODULE_PROP = FQN + ".getUserSettingByModuleAndProp";
	
}
