package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.krishagni.catissueplus.core.common.domain.ConfigSetting;
import com.krishagni.catissueplus.core.common.domain.UserConfigSetting;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.UserConfigSettingDao;

public class UserConfigSettingDaoImpl extends AbstractDao<ConfigSetting> implements UserConfigSettingDao {

	@Override
	public Class<UserConfigSetting> getType() {
		return UserConfigSetting.class;
	}
	
   
	@SuppressWarnings("unchecked")
	@Override
	public List<UserConfigSetting> getAllSettings() {
      return getCurrentSession().getNamedQuery(GET_ALL).list();
	}

	@SuppressWarnings("unchecked")
	public List<UserConfigSetting> getAllSettingsByModule(String moduleName) {
      return getCurrentSession().getNamedQuery(GET_ALL_BY_MODULE)
    		  .setString("name", moduleName)
    		  .list();
	}

    private static final String FQN = UserConfigSetting.class.getName();
	
	private static final String GET_ALL = FQN + ".getAll";
	
	private static final String GET_ALL_BY_MODULE = FQN + ".getAllByModule";
}
