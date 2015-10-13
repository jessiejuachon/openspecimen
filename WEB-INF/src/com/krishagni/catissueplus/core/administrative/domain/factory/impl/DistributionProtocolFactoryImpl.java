
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocolDistSite;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;

public class DistributionProtocolFactoryImpl implements DistributionProtocolFactory {
	private DaoFactory daoFactory;
	
	private com.krishagni.catissueplus.core.de.repository.DaoFactory deDaoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setDeDaoFactory(com.krishagni.catissueplus.core.de.repository.DaoFactory deDaoFactory) {
		this.deDaoFactory = deDaoFactory;
	}

	@Override
	public DistributionProtocol createDistributionProtocol(DistributionProtocolDetail detail) {
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		distributionProtocol.setId(detail.getId());
		setTitle(detail, distributionProtocol, ose);
		setShortTitle(detail, distributionProtocol, ose);
		setInstitute(detail, distributionProtocol, ose);
		setDefReceivingSite(detail, distributionProtocol, ose);
		setPrincipalInvestigator(detail, distributionProtocol, ose);
		setIrbId(detail, distributionProtocol, ose);
		setStartDate(detail, distributionProtocol);
		setEndDate(detail, distributionProtocol);
		setActivityStatus(detail, distributionProtocol, ose);
		setReport(detail, distributionProtocol, ose);
		setDistributingSites(detail, distributionProtocol, ose);
		ose.checkAndThrow();
		return distributionProtocol;
	}
	
	private void setTitle(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getTitle())) {
			ose.addError(DistributionProtocolErrorCode.TITLE_REQUIRED);
			return;
		}
		distributionProtocol.setTitle(detail.getTitle());

	}

	private void setShortTitle(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getShortTitle())) {
			ose.addError(DistributionProtocolErrorCode.SHORT_TITLE_REQUIRED);
			return;
		}
		distributionProtocol.setShortTitle(detail.getShortTitle());
	}
	
	private void setStartDate(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol) {
		distributionProtocol.setStartDate(detail.getStartDate());
	}
	
	private void setEndDate(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol) {
		distributionProtocol.setEndDate(detail.getEndDate());
	}

	private void setInstitute(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		String instituteName = detail.getInstituteName();
		if (StringUtils.isBlank(instituteName)) {
			ose.addError(DistributionProtocolErrorCode.INSTITUTE_REQUIRED);
			return;
		}
		
		Institute institute = daoFactory.getInstituteDao().getInstituteByName(instituteName);
		if (institute == null) {
			ose.addError(InstituteErrorCode.NOT_FOUND);
			return;
		}
		
		distributionProtocol.setInstitute(institute);
	}
	
	private void setDefReceivingSite(DistributionProtocolDetail detail, DistributionProtocol dp, OpenSpecimenException ose) {
		String defReceivingSiteName = detail.getDefReceivingSiteName();
		if (StringUtils.isBlank(defReceivingSiteName)) {
			return;
		}
		
		Site defReceivingSite = daoFactory.getSiteDao().getSiteByName(defReceivingSiteName);
		if (defReceivingSite == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);
			return;
		}
		
		if (!defReceivingSite.getInstitute().equals(dp.getInstitute())) {
			ose.addError(SiteErrorCode.INVALID_SITE_INSTITUTE, defReceivingSite.getName(), dp.getInstitute().getName());
			return;
		}
		
		dp.setDefReceivingSite(defReceivingSite);
	}
	
	private void setPrincipalInvestigator(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		
		if (detail.getPrincipalInvestigator() == null || detail.getPrincipalInvestigator().getId() == null) {
			ose.addError(DistributionProtocolErrorCode.PI_REQUIRED);
			return;
		}
		
		Long piId = detail.getPrincipalInvestigator().getId();
		User pi = daoFactory.getUserDao().getById(piId);
		if (pi == null) {
			ose.addError(DistributionProtocolErrorCode.PI_NOT_FOUND);
			return;
		}
		
		if (!pi.getInstitute().equals(distributionProtocol.getInstitute())) {
			ose.addError(DistributionProtocolErrorCode.PI_DOES_NOT_BELONG_TO_INST, pi.formattedName(), distributionProtocol.getInstitute().getName());
			return;
		}
		
		distributionProtocol.setPrincipalInvestigator(pi);
	}
	
	private void setIrbId(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		distributionProtocol.setIrbId(detail.getIrbId());
	}

	private void setActivityStatus(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		} else if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		distributionProtocol.setActivityStatus(activityStatus);
	}

	private void setReport(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		if (detail.getReport() == null || detail.getReport().getId() == null) {
			return;
		}

		SavedQuery report = deDaoFactory.getSavedQueryDao().getQuery(detail.getReport().getId());
		if (report == null) {
			ose.addError(SavedQueryErrorCode.NOT_FOUND);
			return;
		}

		distributionProtocol.setReport(report);
	}
		
	private void setDistributingSites(DistributionProtocolDetail detail, DistributionProtocol dp, OpenSpecimenException ose) {
		if (CollectionUtils.isEmpty(detail.getDistributingSites().entrySet())) {
			ose.addError(DistributionProtocolErrorCode.DISTRIBUTING_SITES_REQUIRED);
			return;
		}
		
		List<String> distSiteNames = new ArrayList<String>();
		List<String> distInstituteNames = new ArrayList<String>();
		for (Map.Entry<String, List<String>> site : detail.getDistributingSites().entrySet()) {
			if (CollectionUtils.isNotEmpty(site.getValue())) {
				distSiteNames.addAll(site.getValue());
			} else if (site.getKey() != null) {
				distInstituteNames.add(site.getKey());
			}
		}
		
		Set<DistributionProtocolDistSite> distSites = new HashSet<DistributionProtocolDistSite>();
		
		if (CollectionUtils.isNotEmpty(distSiteNames)) {
			List<Site> distributingSites = daoFactory.getSiteDao().getSitesByNames(distSiteNames);
			if (distributingSites.size() != distSiteNames.size()) {
				ose.addError(DistributionProtocolErrorCode.INVALID_DISTRIBUTING_SITES);
				return;
			}
			
			for (Site site : distributingSites) {
				DistributionProtocolDistSite distSite = new DistributionProtocolDistSite();
				distSite.setDistributionProtocol(dp);
				distSite.setInstitute(site.getInstitute());
				distSite.setSite(site);
				distSites.add(distSite);
			}
		}
		
		if (CollectionUtils.isNotEmpty(distInstituteNames)) {
			List<Institute> distInstitutes = daoFactory.getInstituteDao().getInstituteByNames(distInstituteNames);
			if (distInstitutes.size() != distInstituteNames.size()) {
				ose.addError(DistributionProtocolErrorCode.INVALID_DISTRIBUTING_SITES);
				return;
			}
			
			for (Institute inst : distInstitutes) {
				DistributionProtocolDistSite distSite = new DistributionProtocolDistSite();
				distSite.setDistributionProtocol(dp);
				distSite.setInstitute(inst);
				distSites.add(distSite);
			}
		}
		
		dp.setDistributingSites(distSites);
	}
}
