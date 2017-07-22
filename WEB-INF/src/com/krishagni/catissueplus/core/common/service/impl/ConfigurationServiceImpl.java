package com.krishagni.catissueplus.core.common.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.event.ContextRefreshedEvent;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PluginManager;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.domain.ConfigErrorCode;
import com.krishagni.catissueplus.core.common.domain.ConfigProperty;
import com.krishagni.catissueplus.core.common.domain.ConfigProperty.Level;
import com.krishagni.catissueplus.core.common.domain.ConfigSetting;
import com.krishagni.catissueplus.core.common.domain.Module;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.ConfigSettingDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class ConfigurationServiceImpl implements ConfigurationService, InitializingBean, ApplicationListener<ContextRefreshedEvent> {
	
	private Map<String, List<ConfigChangeListener>> changeListeners = new ConcurrentHashMap<>();

	private Map<String, ConfigProperty> configProps;

	private Map<String, Map<String, ConfigSetting>> systemSettings;

	private DaoFactory daoFactory;
	
	private MessageSource messageSource;
	
	private Properties appProps = new Properties();
		
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public void setAppProps(Properties appProps) {
		this.appProps = appProps;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<ConfigSettingDetail>> getSettings(RequestEvent<Pair<String, String>> req) {
		Pair<String, String> input = req.getPayload();
		String module = input.first();
		String level  = input.second();

		ConfigProperty.Level propLevel = ConfigProperty.Level.SYSTEM;
		if (StringUtils.isNotBlank(level)) {
			try {
				propLevel = ConfigProperty.Level.valueOf(level);
			} catch (Exception e) {
				return ResponseEvent.userError(ConfigErrorCode.INVALID_SETTING_LEVEL, level);
			}
		}

		List<ConfigSetting> settings = new ArrayList<>();
		if (StringUtils.isBlank(module)) {
			if (propLevel == ConfigProperty.Level.SYSTEM) {
				for (Map<String, ConfigSetting> moduleSettings : systemSettings.values()) {
					settings.addAll(moduleSettings.values());
				}
			} else {
				settings = daoFactory.getConfigSettingDao().getAllSettings(level, AuthUtil.getCurrentUser().getId());
			}
		} else {
			if (propLevel == ConfigProperty.Level.SYSTEM) {
				Map<String, ConfigSetting> moduleSettings = systemSettings.get(module);
				if (moduleSettings != null) {
					settings.addAll(moduleSettings.values());
				}
			} else {
				settings = daoFactory.getConfigSettingDao().getAllSettingsByModule(module, level, AuthUtil.getCurrentUser().getId());
			}
		}
		return ResponseEvent.response(ConfigSettingDetail.from(settings));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ConfigSettingDetail> getSetting(RequestEvent<Pair<String, String>> req) {
		try {
			Pair<String, String> input = req.getPayload();
			return ResponseEvent.response(ConfigSettingDetail.from(getSetting(input.first(), input.second())));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ConfigSettingDetail> saveSetting(RequestEvent<ConfigSettingDetail> req) {
		ConfigSettingDetail detail = req.getPayload();
		ConfigSetting existing = null;
		boolean successful = false;
		String prop = detail.getName();
		Map<String, ConfigSetting> moduleSettings = null;
		try {
			User user = AuthUtil.getCurrentUser();
			String setting = null;
			String module = detail.getModule();
			String level = detail.getLevel();
			if (level.equals(ConfigProperty.Level.SYSTEM.name())) {
				AccessCtrlMgr.getInstance().ensureUserIsAdmin();
				moduleSettings = systemSettings.get(detail.getModule());
				if (moduleSettings.isEmpty() || moduleSettings == null) {
					return ResponseEvent.userError(ConfigErrorCode.MODULE_NOT_FOUND);
				}

				existing = moduleSettings.get(detail.getName());
				if (existing == null) {
					return ResponseEvent.userError(ConfigErrorCode.SETTING_NOT_FOUND);
				}
			} else if (level.equals(ConfigProperty.Level.USER.name())) {
				if (!user.getId().equals(detail.getObjectId())) {
					return ResponseEvent.userError(RbacErrorCode.ACCESS_DENIED);
				}

				existing = daoFactory.getConfigSettingDao().getSettingByModuleAndProperty(module, prop,
					ConfigProperty.Level.USER.name(), user.getId());
			}

			ConfigProperty property = existing == null ? configProps.get(module + ":" + prop) : existing.getProperty();
			if (property == null) {
				return ResponseEvent.userError(ConfigErrorCode.PROPERTY_NOT_FOUND, prop);
			}

			setting = detail.getValue();
			if (!isValidSetting(property, setting)) {
				return ResponseEvent.userError(ConfigErrorCode.INVALID_SETTING_VALUE);
			}

			ConfigSetting newSetting  = createSetting(property, setting, level, user.getId());
			if (existing != null) {
				existing.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
			}
			
			daoFactory.getConfigSettingDao().saveOrUpdate(newSetting);
			if (level.equals(ConfigProperty.Level.SYSTEM.name())) {
				moduleSettings.put(prop, newSetting);
				notifyListeners(module, prop, setting);
			}

			successful = true;
			return ResponseEvent.response(ConfigSettingDetail.from(newSetting));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			if (successful && existing != null) {
				deleteOldSettingFile(existing);
			} else if (existing != null) {
					existing.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
					moduleSettings.put(prop, existing);
			}
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<File> getSettingFile(RequestEvent<Pair<String, String>> req) {
		try {
			Pair<String, String> payload = req.getPayload();
			return ResponseEvent.response(getSettingFile(payload.first(), payload.second()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public ResponseEvent<String> uploadSettingFile(RequestEvent<FileDetail> req) {
		OutputStream out = null;

		try {
			FileDetail detail = req.getPayload();
			String filename = UUID.randomUUID() + "_" + detail.getFilename();

			out = new FileOutputStream(new File(getSettingFilesDir() + filename));
			IOUtils.copy(detail.getFileIn(), out);

			return ResponseEvent.response(filename);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	@Override
	@PlusTransactional
	public Integer getIntSetting(String module, String name, Integer... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}
		
		return Integer.parseInt(value);
	}

	@Override
	@PlusTransactional
	public Double getFloatSetting(String module, String name, Double... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}
		
		return Double.parseDouble(value);
	}

	@Override
	@PlusTransactional
	public String getStrSetting(String module, String name,	String... defValue) {
		ConfigSetting setting = getSetting(module, name);

		String value = null;
		if (setting != null) {
			value = setting.getValue();
			if (setting.getProperty().isSecured() && value != null) {
				value = Utility.decrypt(value);
			}
		}

		if (StringUtils.isBlank(value)) {
			value = defValue != null && defValue.length > 0 ? defValue[0] : null;
		} 
		
		return value;
	}
	
	@Override
	@PlusTransactional	
	public Character getCharSetting(String module, String name, Character... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}
		
		return value.charAt(0);
	}

	@Override
	@PlusTransactional	
	public Boolean getBoolSetting(String module, String name, Boolean ... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}
		
		return Boolean.parseBoolean(value);		
	}

	@Override
	public File getSettingFile(String module, String name, File... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}

		File file = new File(getSettingFilesDir() + value);
		if (!file.exists()) {
			value = value.split("_", 2)[1];
			throw OpenSpecimenException.userError(ConfigErrorCode.FILE_MOVED_OR_DELETED, value);
		}

		return file;
	}

	@Override
	public String getFileContent(String module, String name, File... defValue) {
		FileDetail fileDetail = null;
		try {
			fileDetail = getFileDetail(module, name, defValue);
			if (fileDetail != null && fileDetail.getFileIn() != null) {
				return IOUtils.toString(fileDetail.getFileIn());
			}

			return StringUtils.EMPTY;
		} catch (OpenSpecimenException ose) {
			throw ose;
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			if (fileDetail != null) {
				IOUtils.closeQuietly(fileDetail.getFileIn());
			}
		}
	}

	@Override
	public FileDetail getFileDetail(String module, String name, File... defValue) {
		try {
			InputStream in = null;
			String contentType = null;
			String filename = null;

			String value = getStrSetting(module, name, (String)null);
			if (StringUtils.isBlank(value)) {
				if (defValue != null && defValue.length > 0 && defValue[0] != null) {
					contentType = Utility.getContentType(defValue[0]);
					filename = defValue[0].getName();
					in = new FileInputStream(defValue[0]);
				}
			} else if (value.startsWith("classpath:")) {
				filename = value.substring(value.lastIndexOf(File.separator) + 1);
				in = this.getClass().getResourceAsStream(value.substring(10));
			} else {
				File file = new File(value); // taking a chance whether setting was a string based before
				if (!file.exists()) {
					file = new File(getSettingFilesDir() + value);
				}

				contentType = Utility.getContentType(file);
				filename = file.getName().substring(file.getName().indexOf("_") + 1);
				in = new FileInputStream(file);
			}

			if (in == null) {
				return null;
			}

			FileDetail fileDetail = new FileDetail();
			fileDetail.setContentType(contentType);
			fileDetail.setFilename(filename);
			fileDetail.setFileIn(in);
			return fileDetail;
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public void reload() {
		configProps    = loadProperties();
		systemSettings = loadSystemSettings();

		for (List<ConfigChangeListener> listeners : changeListeners.values()) {
			for (ConfigChangeListener listener : listeners) {
				listener.onConfigChange(StringUtils.EMPTY, StringUtils.EMPTY);
			}			
		}
	}

	@Override
	public void registerChangeListener(String module, ConfigChangeListener callback) {
		List<ConfigChangeListener> listeners = changeListeners.get(module);
		if (listeners == null) {
			listeners = new ArrayList<>();
			changeListeners.put(module, listeners);
		}
		
		listeners.add(callback);
	}
	
	@Override
	public Map<String, Object> getLocaleSettings() {
		Map<String, Object> result = new HashMap<>();

		Locale locale = Locale.getDefault();
		result.put("locale", locale.toString());
		result.put("dateFmt", messageSource.getMessage("common_date_fmt", null, locale));
		result.put("timeFmt", messageSource.getMessage("common_time_fmt", null, locale));
		result.put("deFeDateFmt", messageSource.getMessage("common_de_fe_date_fmt", null, locale));
		result.put("deBeDateFmt", messageSource.getMessage("common_de_be_date_fmt", null, locale));
		result.put("utcOffset", Utility.getTimezoneOffset());

		return result;
	}
		
	@Override
	public String getDateFormat() {
		return messageSource.getMessage("common_date_fmt", null, Locale.getDefault());
	}
	
	@Override
	public String getDeDateFormat() {		
		return messageSource.getMessage("common_de_be_date_fmt", null, Locale.getDefault());
	}

	@Override
	public String getTimeFormat() {
		return messageSource.getMessage("common_time_fmt", null, Locale.getDefault());
	}

	@Override
	public String getDeDateTimeFormat() {
		return getDeDateFormat() + " " + getTimeFormat();
	}
	
	@Override
	public Map<String, String> getWelcomeVideoSettings() {
		Map<String, String> result = new HashMap<>();
		result.put("welcome_video_source", getStrSetting("common", "welcome_video_source", (String) null));
		result.put("welcome_video_url",    getStrSetting("common", "welcome_video_url", (String) null));
		return result;
	}
	
	@Override
	public Map<String, Object> getAppProps() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("plugins",                 PluginManager.getInstance().getPluginNames());
		props.put("build_version",           appProps.getProperty("buildinfo.version"));
		props.put("build_date",              appProps.getProperty("buildinfo.date"));
		props.put("build_commit_revision",   appProps.getProperty("buildinfo.commit_revision"));
		props.put("cp_coding_enabled",       getBoolSetting("biospecimen", "cp_coding_enabled", false));
		props.put("auto_empi_enabled",       isAutoEmpiEnabled());
		props.put("uid_mandatory",           getBoolSetting("biospecimen", "uid_mandatory", false));
		props.put("feedback_enabled",        getBoolSetting("common", "feedback_enabled", true));
		props.put("mrn_restriction_enabled", getBoolSetting("biospecimen", "mrn_restriction_enabled", false));
		props.put("deploy_env",              getStrSetting("common", "deploy_env"));
		props.put("user_sign_up",            getBoolSetting("administrative", "user_sign_up", true));
		props.put("forgot_password",         getBoolSetting("auth", "forgot_password", true));
		props.put("toast_disp_time",         getIntSetting("common", "toast_disp_time", 5));
		return props;
	}

	@Override
	public String getDataDir() {		
		String dataDir = appProps.getProperty("app.data_dir");
		if (StringUtils.isBlank(dataDir)) {
			dataDir = ".";
		}
		
		return getStrSetting("common", "data_dir", dataDir);
	}
	
	@Override
	public Map<String, String> getPasswordSettings() {
		Map<String, String> result = new HashMap<>();
		result.put("pattern", getStrSetting("auth", "password_pattern", (String) null));
		result.put("desc",    getStrSetting("auth", "password_rule", (String) null));
		return result;
	}

	@Override
	public boolean isOracle() {
		return "oracle".equalsIgnoreCase(appProps.getProperty("database.type"));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		reload();
		
		setLocale();
		registerChangeListener("common", new ConfigChangeListener() {			
			@Override
			public void onConfigChange(String name, String value) {				
				if (name.equals("locale")) {
					setLocale();
				}
			}
		});
	}

	@Override
	@PlusTransactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		reload();
	}

	@Override
	public Map<String, String> getDeploymentSiteAssets() {
		Map<String, String> result = new HashMap<>();
		result.put("siteUrl",  getDeploymentSiteUrl());
		result.put("siteLogo", getDeploymentSiteLogo());
		return result;
	}

	private Map<String, ConfigProperty> loadProperties() {
		List<ConfigProperty> props = daoFactory.getConfigSettingDao().getAllProperties();
		return props.stream().collect(Collectors.toMap(p -> p.getModule().getName() + ":" + p.getName(), p -> p));
	}

	private Map<String, Map<String, ConfigSetting>> loadSystemSettings() {
		Map<String, Map<String, ConfigSetting>> settingsMap = new ConcurrentHashMap<>();

		List<ConfigSetting> settings = daoFactory.getConfigSettingDao().getAllSettings(ConfigProperty.Level.SYSTEM.name(), null);
		for (ConfigSetting setting : settings) {
			ConfigProperty prop = setting.getProperty();
			Hibernate.initialize(prop.getAllowedValues()); // pre-init

			Module module = prop.getModule();

			Map<String, ConfigSetting> moduleSettings = settingsMap.get(module.getName());
			if (moduleSettings == null) {
				moduleSettings = new ConcurrentHashMap<>();
				settingsMap.put(module.getName(), moduleSettings);
			}

			moduleSettings.put(prop.getName(), setting);
		}

		return settingsMap;
	}

	private ConfigSetting getSetting(String moduleName, String propName) {
		ConfigSetting setting = null;
		ConfigProperty property = configProps.get(moduleName + ":" + propName);
		if (property == null) {
			throw OpenSpecimenException.userError(ConfigErrorCode.SETTING_NOT_FOUND);
		}

		User currentUser = AuthUtil.getCurrentUser();
		if (currentUser != null && property.getLevels().contains(ConfigProperty.Level.USER.name())) {
			setting = daoFactory.getConfigSettingDao().getSettingByModuleAndProperty(
				moduleName, propName, ConfigProperty.Level.USER.name(), currentUser.getId());
		}

		if (setting == null) {
			Map<String, ConfigSetting> moduleSettings = systemSettings.get(moduleName);
			if (moduleSettings == null) {
				throw OpenSpecimenException.userError(ConfigErrorCode.MODULE_NOT_FOUND);
			}

			setting = moduleSettings.get(propName);
		}

		if (setting == null) {
			throw OpenSpecimenException.userError(ConfigErrorCode.SETTING_NOT_FOUND);
		}

		return setting;
	}

	private boolean isValidSetting(ConfigProperty property, String setting) {
		if (StringUtils.isBlank(setting)) {
			return true;
		}
		
		Set<String> allowedValues = property.getAllowedValues();
		if (CollectionUtils.isNotEmpty(allowedValues) && !allowedValues.contains(setting)) {
			return false;
		}
				
		try {
			switch (property.getDataType()) {
				case BOOLEAN:
					Boolean.parseBoolean(setting);
					break;
					
				case FLOAT:
					Double.parseDouble(setting);
					break;
					
				case INT:
					Integer.parseInt(setting);
					break;
					
				case FILE:
					String path = getSettingFilesDir() + setting;
					if (!new File(path).exists()) {
						throw new FileNotFoundException("File at path " + path + " does not exists");
					}
					break;

				default:
					break;
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private ConfigSetting createSetting(ConfigProperty property, String value, String level, long objectId) {
		ConfigSetting newSetting = new ConfigSetting();
		newSetting.setProperty(property);
		newSetting.setActivatedBy(AuthUtil.getCurrentUser());
		newSetting.setActivationDate(Calendar.getInstance().getTime());
		newSetting.setLevel(ConfigProperty.Level.valueOf(level));
		newSetting.setObjectId(level.equals(ConfigProperty.Level.SYSTEM.name()) ? null : objectId);
		newSetting.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		if (value != null && property.isSecured()) {
			value = Utility.encrypt(value);
		}
		
		newSetting.setValue(value);
		return newSetting;
	}

	private void notifyListeners(String module, String property, String setting) {
		List<ConfigChangeListener> listeners = changeListeners.get(module);
		if (listeners == null) {
			return;
		}
		
		for (ConfigChangeListener listener : listeners) {
			listener.onConfigChange(property, setting);
		}
	}
	
	private void setLocale() {
		Locale existingLocale = Locale.getDefault();
		String setting = getStrSetting("common", "locale", existingLocale.toString());
		Locale newLocale = LocaleUtils.toLocale(setting);

		if (!existingLocale.equals(newLocale)) {
			Locale.setDefault(newLocale);
		}
	}
	
	private boolean isAutoEmpiEnabled() {
		return StringUtils.isNotBlank(getStrSetting("biospecimen", "mpi_format", "")) || 
				StringUtils.isNotBlank(getStrSetting("biospecimen", "mpi_generator", ""));
	}

	private String getSettingFilesDir() {
		String dir = getDataDir() + File.separator + "config-setting-files";
		new File(dir).mkdirs();
		return dir + File.separator;
	}

	private void deleteOldSettingFile(ConfigSetting oldSetting) {
		if (!oldSetting.getProperty().isFile() || StringUtils.isBlank(oldSetting.getValue())) {
			return;
		}


		File file = new File(getSettingFilesDir() + oldSetting.getValue());
		if (file.exists()) {
			file.delete(); // Very dangerous to do! Should we just rename the file?
		}
	}

	private String getDeploymentSiteUrl() {
		return getStrSetting("common", "deployment_site_url", (String) null);
	}

	private String getDeploymentSiteLogo() {
		String result = null;
		String logo = getStrSetting("common", "deployment_site_logo", (String) null);
		if (StringUtils.isNotBlank(logo)) {
			String appUrl = getStrSetting("common", "app_url", (String) null);
			result = (appUrl != null ? appUrl : "") + "/rest/ng/config-settings/deployment-site-logo";
		}

		return result;
	}
}