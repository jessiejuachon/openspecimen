package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.UserConfigSetting;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.UserConfigSettingDao;

public class UserConfigSettingDaoImpl extends AbstractDao<UserConfigSetting> implements UserConfigSettingDao {

	@Override
	public Class<UserConfigSetting> getType() {
		return UserConfigSetting.class;
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public List<UserConfigSetting> getAllSettings(Long userId) {
    	return getCurrentSession().getNamedQuery(GET_ALL)
    		.setParameter("userId", userId)
    		.list();
	}

    @SuppressWarnings("unchecked")
	@Override
	public List<UserConfigSetting> getAllSettingsByModule(Long userId, String moduleName) {
    	return getCurrentSession().getNamedQuery(GET_ALL_BY_MODULE)
    		.setParameter("userId", userId)
    		.setString("name", moduleName)
    		.list();
	}
    private static final String FQN = UserConfigSetting.class.getName();
    
    private static final String GET_ALL = FQN + ".getAll";
	
    private static final String GET_ALL_BY_MODULE = FQN + ".getAllByModule";
}
