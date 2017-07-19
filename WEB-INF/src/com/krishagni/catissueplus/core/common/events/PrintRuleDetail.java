package com.krishagni.catissueplus.core.common.events;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.domain.PrintRule;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PrintRuleDetail {
	private Long id;

	private Long cpId;

	private String cpTitle;

	private String cpShortTitle;

	private String visitSite;

	private String specimenClass;

	private String specimenType;

	private UserSummary userSummary;

	private String ipRange;

	private String labelType;

	private String labelDesign;

	private String printer;

	private String cmdFileDir;

	private String cmdFileFmt;

	private String lineage;

	private Set<String> labelTokens = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getCpTitle() {
		return cpTitle;
	}

	public void setCpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public String getVisitSite() {
		return visitSite;
	}

	public void setVisitSite(String visitSite) {
		this.visitSite = visitSite;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public UserSummary getUserSummary() {
		return userSummary;
	}

	public void setUserSummary(UserSummary userSummary) {
		this.userSummary = userSummary;
	}

	public String getIpRange() {
		return ipRange;
	}

	public void setIpRange(String ipRange) {
		this.ipRange = ipRange;
	}

	public String getLabelType() {
		return labelType;
	}

	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

	public String getLabelDesign() {
		return labelDesign;
	}

	public void setLabelDesign(String labelDesign) {
		this.labelDesign = labelDesign;
	}

	public String getPrinter() {
		return printer;
	}

	public void setPrinter(String printer) {
		this.printer = printer;
	}

	public String getCmdFileDir() {
		return cmdFileDir;
	}

	public void setCmdFileDir(String cmdFileDir) {
		this.cmdFileDir = cmdFileDir;
	}

	public String getCmdFileFmt() {
		return cmdFileFmt;
	}

	public void setCmdFileFmt(String cmdFileFmt) {
		this.cmdFileFmt = cmdFileFmt;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public Set<String> getLabelTokens() {
		return labelTokens;
	}

	public void setLabelTokens(Set<String> labelTokens) {
		this.labelTokens = labelTokens;
	}

	public static PrintRuleDetail from(PrintRule printRule) {
		PrintRuleDetail detail = new PrintRuleDetail();
		CollectionProtocol cp = printRule.getCollectionProtocol();

		detail.setId(printRule.getId());
		if (cp != null) {
			detail.setCpId(cp.getId());
			detail.setCpTitle(cp.getTitle());
			detail.setCpShortTitle(cp.getShortTitle());
		}

		detail.setVisitSite(printRule.getVisitSite() != null ? printRule.getVisitSite().getName() : null);
		detail.setSpecimenClass(printRule.getSpecimenClass() != null ? printRule.getSpecimenClass().getValue() : null);
		detail.setSpecimenType(printRule.getSpecimenType() != null ? printRule.getSpecimenType().getValue() : null);
		detail.setUserSummary(printRule.getUser() != null ? UserSummary.from(printRule.getUser()) : null);
		detail.setIpRange(printRule.getIpRange());
		detail.setLabelType(printRule.getLabelType());
		detail.setLabelDesign(printRule.getLabelDesign());
		detail.setPrinter(printRule.getPrinter());
		detail.setCmdFileDir(printRule.getCmdFileDir());
		detail.setCmdFileFmt(printRule.getCmdFileFmt() != null ? printRule.getCmdFileFmt().name() : null);
		detail.setLineage(printRule.getLineage() != null ? printRule.getLineage().name() : null);
		detail.setLabelTokens(printRule.getLabelTokens());
		return detail;
	}

	public static List<PrintRuleDetail> from(Collection<PrintRule> printRules) {
		return printRules.stream().map(PrintRuleDetail::from).collect(Collectors.toList());
	}
}
