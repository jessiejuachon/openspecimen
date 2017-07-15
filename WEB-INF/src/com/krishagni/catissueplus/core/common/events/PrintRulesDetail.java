package com.krishagni.catissueplus.core.common.events;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.krishagni.catissueplus.core.common.domain.PrintLabelTokens;
import com.krishagni.catissueplus.core.common.domain.PrintRules;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PrintRulesDetail {
	private Long id;

	private String collectionProtocol;

	private String visitSite;

	private String specimenClass;

	private String specimenType;

	private Long userId;

	private String ipRange;

	private String labelType;

	private String labelDesign;

	private String printer;

	private String cmdFileDir;

	private String cmdFileFmt;

	private String lineage;

	private String labelTokens;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(String collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getLabelTokens() {
		return labelTokens;
	}

	public void setLabelTokens(String labelTokens) {
		this.labelTokens = labelTokens;
	}

	public static PrintRulesDetail from(PrintRules printRule) {
		PrintRulesDetail detail = new PrintRulesDetail();

		detail.setId(printRule.getId());
		detail.setCollectionProtocol(printRule.getCollectionProtocol().getShortTitle());
		detail.setVisitSite(printRule.getVisitSite().getName());
		detail.setSpecimenClass(printRule.getSpecimenClass());
		detail.setSpecimenType(printRule.getSpecimenType().getValue());
		detail.setUserId(printRule.getUser().getId());
		detail.setIpRange(printRule.getIpRange());
		detail.setLabelType(printRule.getLabelType());
		detail.setLabelDesign(printRule.getLabelDesign());
		detail.setPrinter(printRule.getPrinter());
		detail.setCmdFileDir(printRule.getCmdFileDir());
		detail.setCmdFileFmt(printRule.getCmdFileFmt().toString());
		detail.setLineage(printRule.getLineage().toString());
		detail.setLabelTokens(StringUtils.join(getTokens(printRule.getLabelTokens()), ","));

		return detail;
	}

	public static List<PrintRulesDetail> from(Collection<PrintRules> printRules) {
		return printRules.stream().map(PrintRulesDetail::from).collect(Collectors.toList());
	}

	private static List<String> getTokens(Set<PrintLabelTokens> tokens) {
		List<String> labelTokens = new LinkedList<>();
		for (PrintLabelTokens pt: tokens) {
			labelTokens.add(pt.getToken());
		}
		return labelTokens;
	}
}
