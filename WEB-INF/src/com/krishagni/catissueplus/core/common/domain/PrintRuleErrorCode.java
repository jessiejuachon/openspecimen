package com.krishagni.catissueplus.core.common.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum PrintRuleErrorCode implements ErrorCode {
	CP_NOT_FOUND,

	VISIT_SITE_NOT_FOUND,

	SPMN_CLASS_NOT_FOUND,

	SPMN_TYPE_NOT_FOUND,

	USER_NOT_FOUND,

	CMD_FILE_FMT_NOT_FOUND,

	LINEAGE_NOT_FOUND,

	CMD_FILE_DIR_REQUIRED,

	LABEL_TOKENS_REQUIRED;

	@Override
	public String code() {
		return "PRINT_RULE_" + this.name();
	}
}
