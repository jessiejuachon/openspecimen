package com.krishagni.catissueplus.core.common.domain.factory;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.PrintRule;
import com.krishagni.catissueplus.core.common.domain.PrintRuleErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.PrintRuleDetail;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class PrintRuleFactory {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public PrintRule createPrintRule(PrintRuleDetail detail) {
		PrintRule printRule = new PrintRule();

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		setCollectionProtocol(detail, printRule);
		setVisitSite(detail, printRule);
		setSpecimenClass(detail, printRule);
		setSpecimenType(detail, printRule);
		setUser(detail, printRule);
		setIpRange(detail, printRule);
		setLabelType(detail, printRule);
		setLabelDesign(detail, printRule);
		setPrinter(detail, printRule);
		setCmdFileDir(detail, printRule, ose);
		setCmdFileFmt(detail, printRule);
		setLineage(detail, printRule);
		setLabelTokens(detail, printRule, ose);

		ose.checkAndThrow();
		return printRule;
	}

	private void setCollectionProtocol(PrintRuleDetail input, PrintRule result) {
		Long cpId = input.getCpId();
		String cpTitle = input.getCpTitle();
		String cpShortTitle = input.getCpShortTitle();

		if (cpId == null && StringUtils.isBlank(cpTitle) && StringUtils.isBlank(cpShortTitle)) {
			return;
		}

		CollectionProtocol cp = null;
		if (cpId != null) {
			cp = daoFactory.getCollectionProtocolDao().getById(cpId);
		} else if (StringUtils.isNoneBlank(cpTitle)) {
			cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(cpTitle);
		} else if (StringUtils.isNoneBlank(cpShortTitle)) {
			cp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(cpShortTitle);
		}

		if (cp == null) {
			throw OpenSpecimenException.userError(PrintRuleErrorCode.CP_NOT_FOUND);
		}
		result.setCollectionProtocol(cp);
	}

	private void setVisitSite(PrintRuleDetail input, PrintRule result) {
		String visitSite = input.getVisitSite();
		if (StringUtils.isBlank(visitSite)) {
			return;
		}

		Site site = daoFactory.getSiteDao().getSiteByName(input.getVisitSite());
		if (site == null) {
			throw OpenSpecimenException.userError(PrintRuleErrorCode.VISIT_SITE_NOT_FOUND);
		}
		result.setVisitSite(site);
	}

	private void setSpecimenClass(PrintRuleDetail input, PrintRule result) {
		String specimenClass = input.getSpecimenClass();
		if (StringUtils.isBlank(specimenClass)) {
			return;
		}

		PermissibleValue spmnClass = daoFactory.getPermissibleValueDao().getByValue("specimen_type", specimenClass);
		if (spmnClass == null) {
			throw OpenSpecimenException.userError(PrintRuleErrorCode.SPMN_CLASS_NOT_FOUND);
		}
		result.setSpecimenClass(spmnClass);
	}

	private void setSpecimenType(PrintRuleDetail input, PrintRule result) {
		String specimenType = input.getSpecimenType();
		if (StringUtils.isBlank(specimenType)) {
			return;
		}

		PermissibleValue spmnType = daoFactory.getPermissibleValueDao().getByValue("specimen_type", specimenType);
		if (spmnType == null) {
			throw OpenSpecimenException.userError(PrintRuleErrorCode.SPMN_TYPE_NOT_FOUND);
		}
		result.setSpecimenType(spmnType);
	}

	private void setUser(PrintRuleDetail input, PrintRule result) {
		UserSummary userSummary = input.getUserSummary();
		if (userSummary == null) {
			return;
		}

		User user = daoFactory.getUserDao().getById(userSummary.getId());
		if(user == null) {
			throw OpenSpecimenException.userError(PrintRuleErrorCode.USER_NOT_FOUND);
		}
		result.setUser(user);
	}

	private void setIpRange(PrintRuleDetail input, PrintRule result) {
		String ipRange = input.getIpRange();
		if (StringUtils.isBlank(ipRange)) {
			return;
		}

		result.setIpRange(ipRange);
	}

	private void setLabelType(PrintRuleDetail input, PrintRule result) {
		String labelType = input.getLabelType();
		if (StringUtils.isBlank(labelType)) {
			return;
		}

		result.setLabelType(labelType);
	}

	private void setLabelDesign(PrintRuleDetail input, PrintRule result) {
		String labelDesign = input.getLabelDesign();
		if (StringUtils.isBlank(labelDesign)) {
			return;
		}

		result.setLabelDesign(labelDesign);
	}

	private void setPrinter(PrintRuleDetail input, PrintRule result) {
		String printer = input.getPrinter();
		if (StringUtils.isBlank(printer)) {
			return;
		}

		result.setPrinter(printer);
	}

	private void setCmdFileDir(PrintRuleDetail input, PrintRule result, OpenSpecimenException ose) {
		String CmdFileDir = input.getCmdFileDir();
		if (StringUtils.isBlank(CmdFileDir)) {
			ose.addError(PrintRuleErrorCode.CMD_FILE_DIR_REQUIRED);
			return;
		}

		result.setCmdFileDir(CmdFileDir);
	}

	private void setCmdFileFmt(PrintRuleDetail input, PrintRule result) {
		String cmdFileFmt = input.getCmdFileFmt();
		if (StringUtils.isBlank(cmdFileFmt)) {
			return;
		}

		if(!EnumUtils.isValidEnum(PrintRule.CmdFileFmt.class, cmdFileFmt)) {
			throw OpenSpecimenException.userError(PrintRuleErrorCode.CMD_FILE_FMT_NOT_FOUND);
		}
		result.setCmdFileFmt(PrintRule.CmdFileFmt.valueOf(cmdFileFmt));
	}

	private void setLineage(PrintRuleDetail input, PrintRule result) {
		String lineage = input.getLineage();
		if (StringUtils.isBlank(lineage)) {
			return;
		}

		if(!EnumUtils.isValidEnum(PrintRule.Lineage.class, lineage)) {
			throw OpenSpecimenException.userError(PrintRuleErrorCode.LINEAGE_NOT_FOUND);
		}
		result.setLineage(PrintRule.Lineage.valueOf(lineage));
	}

	private void setLabelTokens(PrintRuleDetail input, PrintRule result, OpenSpecimenException ose) {
		Set<String> labelTokens = input.getLabelTokens();
		if (CollectionUtils.isEmpty(labelTokens)) {
			ose.addError(PrintRuleErrorCode.LABEL_TOKENS_REQUIRED);
			return;
		}

		result.setLabelTokens(labelTokens);
	}
}
