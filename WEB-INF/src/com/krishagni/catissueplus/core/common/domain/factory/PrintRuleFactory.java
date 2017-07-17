package com.krishagni.catissueplus.core.common.domain.factory;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.PrintRule;
import com.krishagni.catissueplus.core.common.events.PrintRuleDetail;

public class PrintRuleFactory {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public PrintRule createPrintRule(PrintRuleDetail detail) {
		PrintRule printRule = new PrintRule();

		printRule.setCollectionProtocol(getCollectionProtocol(detail));
		printRule.setVisitSite(daoFactory.getSiteDao().getSiteByName(detail.getVisitSite()));
		printRule.setSpecimenClass(daoFactory.getPermissibleValueDao().getByValue("specimen_type", detail.getSpecimenClass()));
		printRule.setSpecimenType(daoFactory.getPermissibleValueDao().getByValue("specimen_type", detail.getSpecimenType()));
		printRule.setUser(daoFactory.getUserDao().getById(detail.getUserSummary().getId()));
		printRule.setIpRange(detail.getIpRange());
		printRule.setLabelType(detail.getLabelType());
		printRule.setLabelDesign(detail.getLabelDesign());
		printRule.setPrinter(detail.getPrinter());
		printRule.setCmdFileDir(detail.getCmdFileDir());
		printRule.setCmdFileFmt(PrintRule.CmdFileFmt.get(detail.getCmdFileFmt()));
		printRule.setLineage(PrintRule.Lineage.valueOf(detail.getLineage()));
		printRule.setLabelTokens(detail.getLabelTokens());
		return printRule;
	}

	private  CollectionProtocol getCollectionProtocol(PrintRuleDetail detail) {
		CollectionProtocol cp = null;
		if (detail.getCpId() != null) {
			cp = daoFactory.getCollectionProtocolDao().getById(detail.getCpId());
		} else if (StringUtils.isNoneBlank(detail.getCpTitle())) {
			cp = daoFactory.getCollectionProtocolDao().getCollectionProtocol(detail.getCpTitle()); 
		} else if (StringUtils.isNoneBlank(detail.getCpShortTitle())) {
			cp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(detail.getCpShortTitle());
		}

		return cp;
	}
}
