package com.krishagni.catissueplus.core.common.domain;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class PrintLabelTokens extends BaseEntity {
	private PrintRules rule;

	private String token;

	public PrintRules getRule() {
		return rule;
	}

	public void setRule(PrintRules rule) {
		this.rule = rule;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
