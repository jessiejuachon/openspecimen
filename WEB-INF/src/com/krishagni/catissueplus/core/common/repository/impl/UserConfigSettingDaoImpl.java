package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.UserConfigSetting;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.UserConfigSettingDao;

public class UserConfigSettingDaoImpl extends AbstractDao<UserConfigSetting> implements UserConfigSettingDao {
	
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
    		.setString("modulename", moduleName)
    		.list();
	}
   
	@Override
	public UserConfigSetting getSettingByModuleAndProperty(Long userId, String moduleName, String prop ){
	    	return (UserConfigSetting) getCurrentSession().getNamedQuery(GET_ALL_BY_MODULE_PROP)
	    			.setParameter("userId", userId)
	        		.setString("moduleName", moduleName)
	        		.setString("propName", prop)
	        		.uniqueResult();
	}
    
	@Override
	public Class<UserConfigSetting> getType() {
		return UserConfigSetting.class;
	}
 
	private static final String FQN = UserConfigSetting.class.getName();
    
	private static final String GET_ALL = FQN + ".getAll";
	
	private static final String GET_ALL_BY_MODULE = FQN + ".getAllByModule";
    
	private static final String GET_ALL_BY_MODULE_PROP = FQN + ".getAllByModuleAndProp";
}