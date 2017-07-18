package com.krishagni.catissueplus.core.common.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;

public class PrintRule extends BaseEntity {
	public enum CmdFileFmt {
		CSV,
		KEY_VALUE;

	};

	public enum Lineage {
		ALIQUOTS,
		PARENT,
		DERIVATIVE
	};

	private CollectionProtocol collectionProtocol;

	private Site visitSite;

	private PermissibleValue specimenClass;

	private PermissibleValue specimenType;

	private User user;

	private String ipRange;

	private String labelType;

	private String labelDesign;

	private String printer;

	private String cmdFileDir;

	private CmdFileFmt cmdFileFmt;

	private Lineage lineage;

	private Set<String> labelTokens = new HashSet<>();

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

	public PermissibleValue getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(PermissibleValue specimenClass) {
		this.specimenClass = specimenClass;
	}

	public PermissibleValue getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(PermissibleValue specimenType) {
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

	public CmdFileFmt getCmdFileFmt() {
		return cmdFileFmt;
	}

	public void setCmdFileFmt(CmdFileFmt cmdFileFmt) {
		this.cmdFileFmt = cmdFileFmt;
	}

	public Lineage getLineage() {
		return lineage;
	}

	public void setLineage(Lineage lineage) {
		this.lineage = lineage;
	}

	public Set<String> getLabelTokens() {
		return labelTokens;
	}

	public void setLabelTokens(Set<String> labelTokens) {
		this.labelTokens = labelTokens;
	}
}
