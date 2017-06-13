package com.krishagni.catissueplus.core.common.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.ConfigSetting;
import com.krishagni.catissueplus.core.common.domain.UserConfigSetting;

public interface UserConfigSettingDao extends Dao<ConfigSetting> {
  List<UserConfigSetting> getAllSettings(Long id);
  
  List<UserConfigSetting> getAllSettingsByModule(String moduleName);

}
