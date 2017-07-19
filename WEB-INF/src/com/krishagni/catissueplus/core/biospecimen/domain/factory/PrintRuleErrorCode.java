package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum PrintRuleErrorCode implements ErrorCode {
	CMD_FILE_FMT_NOT_FOUND,

	CMD_FILE_DIR_REQUIRED,

	LABEL_TOKENS_REQUIRED;

	@Override
	public String code() {
		return "PRINT_RULE_" + this.name();
	}
}
