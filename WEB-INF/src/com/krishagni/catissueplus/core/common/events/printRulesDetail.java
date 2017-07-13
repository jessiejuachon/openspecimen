package com.krishagni.catissueplus.core.common.events;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class printRulesDetail {
	private Long id;

	private CollectionProtocol collectionProtocol;

	private Site visitSite;

	private String specimenClass;

	private String specimenType;

	private User user;

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

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public Site getVisitSite() {
		return visitSite;
	}

	public void setVisitSite(Site visitSite) {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
}
