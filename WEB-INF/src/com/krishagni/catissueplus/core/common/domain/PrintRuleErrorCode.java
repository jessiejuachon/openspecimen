package com.krishagni.catissueplus.core.common.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum PrintRuleErrorCode implements ErrorCode {
	CMD_FILE_DIR_REQUIRED,

	LABEL_TOKENS_REQUIRED;

	@Override
	public String code() {
		return "PR_" + this.name();
	}
}
