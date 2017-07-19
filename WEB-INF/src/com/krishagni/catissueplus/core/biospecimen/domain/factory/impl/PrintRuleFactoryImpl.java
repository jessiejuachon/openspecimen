package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.PrintRule;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.PrintRuleErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.PrintRuleFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.PrintRuleDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class PrintRuleFactoryImpl implements PrintRuleFactory {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public PrintRule createPrintRule(PrintRuleDetail detail) {
		PrintRule rule = new PrintRule();

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		setCollectionProtocol(detail, rule, ose);
		setVisitSite(detail, rule, ose);
		setSpecimenClass(detail, rule, ose);
		setSpecimenType(detail, rule, ose);
		setUser(detail, rule, ose);
		setIpRange(detail, rule, ose);
		setLabelType(detail, rule, ose);
		setLabelDesign(detail, rule, ose);
		setPrinter(detail, rule, ose);
		setCmdFileDir(detail, rule, ose);
		setCmdFileFmt(detail, rule, ose);
		setLineage(detail, rule, ose);
		setLabelTokens(detail, rule, ose);

		ose.checkAndThrow();
		return rule;
	}

	private void setCollectionProtocol(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		CollectionProtocol cp = null;
		Object key = null;

		if (input.getCpId() != null) {
		    cp = daoFactory.getCollectionProtocolDao().getById(input.getCpId());
		    key = input.getCpId();
		} else if (StringUtils.isNotBlank(input.getCpTitle())) {
		    cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(input.getCpTitle());
		    key = input.getCpTitle();
		} else if (StringUtils.isNoneBlank(input.getCpShortTitle())) {
		    cp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(input.getCpShortTitle());
		    key = input.getCpShortTitle();
		}

		if (cp == null && key != null) {
		    ose.addError(CpErrorCode.NOT_FOUND);
		}

		rule.setCollectionProtocol(cp);
	}

	private void setVisitSite(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		String visitSite = input.getVisitSite();
		if (StringUtils.isBlank(visitSite)) {
			return;
		}

		Site site = daoFactory.getSiteDao().getSiteByName(input.getVisitSite());
		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);
			return;
		}
		rule.setVisitSite(site);
	}

	private void setSpecimenClass(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		PermissibleValue spmnClass = getPermissibleValue(input.getSpecimenClass());

		if (spmnClass == null && StringUtils.isNotBlank(input.getSpecimenClass())) {
			ose.addError(SrErrorCode.INVALID_SPECIMEN_CLASS);
			return;
		}

		rule.setSpecimenClass(spmnClass);
	}

	private void setSpecimenType(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		PermissibleValue spmnType = getPermissibleValue(input.getSpecimenType());

		if (spmnType == null && StringUtils.isNotBlank(input.getSpecimenType())) {
			ose.addError(SrErrorCode.INVALID_SPECIMEN_TYPE);
			return;
		}

		rule.setSpecimenType(spmnType);
	}

	private void setUser(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		UserSummary userSummary = input.getUserSummary();
		if (userSummary == null) {
			return;
		}

		User user = getUser(userSummary);
		if(user == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}

		rule.setUser(user);
	}

	private void setIpRange(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		String ipRange = input.getIpRange();
		if (StringUtils.isBlank(ipRange)) {
			return;
		}

		rule.setIpRange(ipRange);
	}

	private void setLabelType(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		String labelType = input.getLabelType();
		if (StringUtils.isBlank(labelType)) {
			return;
		}

		rule.setLabelType(labelType);
	}

	private void setLabelDesign(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		String labelDesign = input.getLabelDesign();
		if (StringUtils.isBlank(labelDesign)) {
			return;
		}

		rule.setLabelDesign(labelDesign);
	}

	private void setPrinter(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		String printer = input.getPrinter();
		if (StringUtils.isBlank(printer)) {
			return;
		}

		rule.setPrinter(printer);
	}

	private void setCmdFileDir(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		String CmdFileDir = input.getCmdFileDir();
		if (StringUtils.isBlank(CmdFileDir)) {
			ose.addError(PrintRuleErrorCode.CMD_FILE_DIR_REQUIRED);
			return;
		}

		rule.setCmdFileDir(CmdFileDir);
	}

	private void setCmdFileFmt(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		String cmdFileFmt = input.getCmdFileFmt();
		if (StringUtils.isBlank(cmdFileFmt)) {
			return;
		}

		if(!EnumUtils.isValidEnum(PrintRule.CmdFileFmt.class, cmdFileFmt)) {
			ose.addError(PrintRuleErrorCode.CMD_FILE_FMT_NOT_FOUND);
			return;
		}
		rule.setCmdFileFmt(PrintRule.CmdFileFmt.valueOf(cmdFileFmt));
	}

	private void setLineage(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		String lineage = input.getLineage();
		if (StringUtils.isBlank(lineage)) {
			return;
		}

		if (!Specimen.isValidLineage(lineage)) {
			ose.addError(SpecimenErrorCode.INVALID_LINEAGE);
			return;
		}

		rule.setLineage(lineage);
	}

	private void setLabelTokens(PrintRuleDetail input, PrintRule rule, OpenSpecimenException ose) {
		Set<String> labelTokens = input.getLabelTokens();
		if (CollectionUtils.isEmpty(labelTokens)) {
			ose.addError(PrintRuleErrorCode.LABEL_TOKENS_REQUIRED);
			return;
		}

		rule.setLabelTokens(labelTokens);
	}

	private PermissibleValue getPermissibleValue(String input) {
		if (StringUtils.isBlank(input)) {
			return null;
		}

		PermissibleValue pvs = daoFactory.getPermissibleValueDao().getByValue("specimen_type", input);
		return pvs;
	}

	private User getUser(UserSummary userDetail) {
		if (userDetail == null) {
			return null;
		}

		User user = null;
		if (userDetail.getId() != null) {
			user = daoFactory.getUserDao().getById(userDetail.getId());
		} else if (StringUtils.isNotBlank(userDetail.getLoginName()) && StringUtils.isNotBlank(userDetail.getDomain())) {
			user = daoFactory.getUserDao().getUser(userDetail.getLoginName(), userDetail.getDomain());
		} else if (StringUtils.isNotBlank(userDetail.getEmailAddress())) {
			user = daoFactory.getUserDao().getUserByEmailAddress(userDetail.getEmailAddress());
		}

		return user;
	}
}
