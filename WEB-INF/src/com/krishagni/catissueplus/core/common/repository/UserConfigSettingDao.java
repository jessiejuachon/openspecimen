package com.krishagni.catissueplus.core.common.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.UserConfigSetting;

public interface UserConfigSettingDao extends Dao<UserConfigSetting> {
  List<UserConfigSetting> getAllSettings(Long id);
  
  List<UserConfigSetting> getAllSettingsByModule(Long id, String moduleName);

  UserConfigSetting getSettingByModuleAndProperty(Long id, String module, String prop);

}
